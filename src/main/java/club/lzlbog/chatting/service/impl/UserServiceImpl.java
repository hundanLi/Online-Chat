package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.dto.RegisterDTO;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.entity.UserGroup;
import club.lzlbog.chatting.entity.UserRole;
import club.lzlbog.chatting.mapper.UserMapper;
import club.lzlbog.chatting.service.*;
import club.lzlbog.chatting.util.Encrypto;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *      服务实现类
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@Service
@PropertySource("classpath:/config.properties")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${user.avatarDefaultUrl}")
    private String avatarDefaultUrl;
    @Value("${user.avatarDir}")
    private String avatarDir;
    @Value("${user.avatarUrlPrefix}")
    private String avatarUrlPrefix;


    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final UserGroupService userGroupService;
    private final SessionService sessionService;

    @Autowired
    public UserServiceImpl(UserRoleService userRoleService, UserMapper userMapper, UserGroupService userGroupService, SessionService sessionService) {
        this.userRoleService = userRoleService;
        this.userMapper = userMapper;
        this.userGroupService = userGroupService;
        this.sessionService = sessionService;
    }

    @Override
    public User register(RegisterDTO registerDTO) {
        //检查用户名是否存在
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("username", registerDTO.getUsername());
        int count = super.selectCount(wrapper);
        if (count > 0) {
            return null;
        }
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(Encrypto.encrypto(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setAvatarUrl(avatarDefaultUrl);
        boolean insert = user.insert();
        if (insert) {
            //添加角色
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            //普通用户
            userRole.setRoleId("user");
            boolean link = userRoleService.link(userRole);
            if (!link) {
                throw new RuntimeException("用户注册失败！");
            }
            return user;
        } else {
            return null;
        }
    }

    @Override
    public List<User> getFriends(String username) {
        return userMapper.selectFriends(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("email", email);
        int count = super.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public User findByUsername(String username) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("username", username);
        return super.selectOne(wrapper);
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        User user = new User();
        user.setPassword(Encrypto.encrypto(newPassword));
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("email", email);
        return super.update(user, wrapper);
    }

    @Override
    public List<User> searchByUsername(String username) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.like("username", username);
        wrapper.setSqlSelect("user_id, username, avatar_url as avatarUrl");
        List<User> users = super.selectList(wrapper);
        users.forEach(user -> {
            if (sessionService.isOnline(user.getUsername())) {
                user.setOnline(true);
            }
        });
        return users;
    }

    @Override
    public String updateAvatar(String username, MultipartFile multipartFile) {
        //构造文件名
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = username + suffix;
        File file = new File(avatarDir);
        if (!file.isDirectory() && !file.isFile()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                file = new File(avatarDir, filename);
            }
        } else {
            file = new File(avatarDir, filename);
        }
        //转存文件，更新数据库
        try {
            multipartFile.transferTo(file);
            String avatarUrl = avatarUrlPrefix + filename;
            //更新数据库
            User user = new User();
            user.setAvatarUrl(avatarUrl);
            EntityWrapper<User> wrapper = new EntityWrapper<>();
            wrapper.eq("username", username);
            super.update(user, wrapper);
            return avatarUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int dropGroup(String username, String groupId) {
        String id = userMapper.selectIdByUsername(username);
        EntityWrapper<UserGroup> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", id)
                .and()
                .eq("group_id", groupId);
        boolean delete = userGroupService.delete(wrapper);
        if (delete) {
            return ServiceStatus.SUCCESS;
        }
        return ServiceStatus.NOT_FOUND;
    }

    @Override
    public User getProfile(String username) {
        return userMapper.selectUserByUsername(username);
        //  return sessionService.getUserInfo(username);
    }

}

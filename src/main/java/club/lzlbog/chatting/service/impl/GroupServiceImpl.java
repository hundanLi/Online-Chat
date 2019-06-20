package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.dto.GroupMemberDTO;
import club.lzlbog.chatting.entity.Group;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.entity.UserGroup;
import club.lzlbog.chatting.mapper.GroupMapper;
import club.lzlbog.chatting.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {


    private final UserService userService;
    private final UserGroupService userGroupService;
    private final SessionService sessionService;
    private static final int MAX_SIZE = 2000;

    @Autowired
    public GroupServiceImpl(UserService userService, UserGroupService userGroupService, SessionService sessionService) {
        this.userService = userService;
        this.userGroupService = userGroupService;
        this.sessionService = sessionService;
    }


    @Override
    public int createGroup(String username, Group group) {
        //1.设置群主为该用户名
        group.setGroupOwner(username);
        //设置id和创建时间
        group.setGroupId(UUID.randomUUID().toString());
        group.setGroupCreatedTime(LocalDateTime.now());

        //2.插入数据库
        boolean insert = super.insert(group);
        //3.添加自己为群成员
        User user = userService.findByUsername(username);
        UserGroup userGroup = new UserGroup(user.getUserId(), group.getGroupId());
        boolean insert1 = userGroupService.insert(userGroup);
        if (insert && insert1) {
            return ServiceStatus.SUCCESS;
        }
        return ServiceStatus.ERROR;
    }

    @Override
    public int deleteGroup(String username, String groupId) {
        //1.验证群主信息是否匹配
        Group group = super.selectById(groupId);
        if (group == null || !Objects.equals(username, group.getGroupOwner())) {
            return ServiceStatus.FORBIDDEN;
        }
        //2.删除群组
        boolean dg = super.deleteById(groupId);
        //3.删除成员与群组的关联
        EntityWrapper<UserGroup> wrapper = new EntityWrapper<>();
        wrapper.eq("group_id", groupId);
        boolean dug = userGroupService.delete(wrapper);
        if (dg && dug) {
            return ServiceStatus.SUCCESS;
        }
        return ServiceStatus.ERROR;
    }

    @Override
    public int addMembers(GroupMemberDTO dto) {

        //1.验证群组是否存在/验证群主身份
        @NotBlank String groupId = dto.getGroupId();
        EntityWrapper<Group> gWrapper = new EntityWrapper<>();
        gWrapper.eq("group_id", groupId).and().eq("group_owner", dto.getGroupOwner());
        Group group = super.selectOne(gWrapper);
        if (group == null) {
            return ServiceStatus.NOT_FOUND;
        }
        //2.过滤群组中已经存在的成员id
        Set<String> userIdSet = userGroupService.selectUserIdByGroupId(dto.getGroupId());
        List<String> memberIds = dto.getMembers();
        memberIds.removeIf(userIdSet::contains);

        //3.判断成员数量是否超过最大
        if (userIdSet.size() + memberIds.size() > MAX_SIZE) {
            return ServiceStatus.FORBIDDEN;
        }


        //4.批量插入
        List<UserGroup> userGroupList = new ArrayList<>(memberIds.size());
        memberIds.forEach(uid -> userGroupList.add(new UserGroup(uid, groupId)));
        userGroupService.insertBatch(userGroupList);
        //5.更新groupSize
        group.setGroupSize(group.getGroupSize() + memberIds.size());
        super.updateById(group);

        return ServiceStatus.SUCCESS;
    }

    @Override
    public int removeMembers(GroupMemberDTO dto) {
        //1.判断群组是否存在
        EntityWrapper<Group> wrapper = new EntityWrapper<>();
        @NotBlank String groupId = dto.getGroupId();
        wrapper.eq("group_id", groupId).and().eq("group_owner", dto.getGroupOwner());
        int count = super.selectCount(wrapper);
        if (count < 1) {
            return ServiceStatus.NOT_FOUND;
        }

        //批量删除
        @NotEmpty List<String> members = dto.getMembers();
        EntityWrapper<UserGroup> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("group_id", groupId).and().in("user_id", members);
        boolean delete = userGroupService.delete(entityWrapper);
        if (delete) {
            return ServiceStatus.SUCCESS;
        }
        return ServiceStatus.ERROR;
    }

    @Override
    public List<User> getMembers(String username, String groupId) {
        //验证群组是否存在
        //验证user是否属于该群组
        User user = userService.findByUsername(username);
        EntityWrapper<UserGroup> ugWrapper = new EntityWrapper<>();
        ugWrapper.eq("user_id", user.getUserId()).and().eq("group_id", groupId);
        int i = userGroupService.selectCount(ugWrapper);
        if (i < 1) {
            return Collections.emptyList();
        }
        //获取所有群组成员id
        ugWrapper = new EntityWrapper<>();
        ugWrapper.eq("group_id", groupId);
        List<UserGroup> userGroups = userGroupService.selectList(ugWrapper);
        List<String> userIds = new ArrayList<>(userGroups.size());
        userGroups.forEach(ug -> userIds.add(ug.getUserId()));

        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.in("user_id", userIds);
        userEntityWrapper.setSqlSelect("user_id", "username", "avatar_url", "created_time");
        List<User> users = userService.selectList(userEntityWrapper);
        //设置在线状态
        users.forEach(u -> {
            if (sessionService.isOnline(u.getUsername())) {
                u.setOnline(true);
            }
        });
        return users;
    }


    @Override
    public List<Group> getGroups(String userId) {
        //1.查找group ids
        EntityWrapper<UserGroup> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        List<UserGroup> userGroups = userGroupService.selectList(wrapper);

        //2.查找groups
        List<String> groupIds = new ArrayList<>(userGroups.size());
        userGroups.forEach(userGroup -> {
            groupIds.add(userGroup.getGroupId());
        });
        EntityWrapper<Group> groupEntityWrapper = new EntityWrapper<>();
        groupEntityWrapper.in("group_id", groupIds);
        return super.selectList(groupEntityWrapper);
    }
}

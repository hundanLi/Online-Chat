package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.entity.Friendship;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.mapper.FriendshipMapper;
import club.lzlbog.chatting.service.FriendshipService;
import club.lzlbog.chatting.service.ServiceStatus;
import club.lzlbog.chatting.service.SessionService;
import club.lzlbog.chatting.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class FriendshipServiceImpl extends ServiceImpl<FriendshipMapper, Friendship> implements FriendshipService {

    private final UserService userService;

    private final SessionService sessionService;

    @Autowired
    public FriendshipServiceImpl(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Override
    public int addFriendship(Friendship friendship) {

        //判断好友关系是否存在
        EntityWrapper<Friendship> few = new EntityWrapper<>();
        few.eq("username1", friendship.getUsername1()).and().eq("username2", friendship.getUsername2());
        int fCount = super.selectCount(few);
        if (fCount > 0) {
            return ServiceStatus.EXISTENCE;
        }
        //更新friendship表，添加好友关系
        Friendship friendship2 = new Friendship(friendship.getUsername2(), friendship.getUsername1());
        boolean insertBatch = super.insertBatch(Arrays.asList(friendship, friendship2));
        if (insertBatch) {
            return ServiceStatus.SUCCESS;
        }
        return ServiceStatus.ERROR;
    }

    @Override
    public int deleteFriendship(Friendship friendship, Principal principal) {
        //判断用户id是否合法
        if (!friendship.getUsername1().equals(principal.getName())) {
            return ServiceStatus.FORBIDDEN;
        }

        //判断好友关系是否存在
        EntityWrapper<Friendship> few = new EntityWrapper<>();
        few.eq("username1", friendship.getUsername1()).and().eq("username2", friendship.getUsername2());
        int count = super.selectCount(few);
        if (count < 1) {
            return ServiceStatus.NOT_FOUND;
        }
        //删除好友关系
        EntityWrapper<Friendship> few2 = new EntityWrapper<>();
        few2.eq("username1", friendship.getUsername2()).and().eq("username2", friendship.getUsername1());
        boolean delete1 = super.delete(few);
        boolean delete2 = super.delete(few2);
        if (delete1 && delete2) {
            return ServiceStatus.SUCCESS;
        }
        return ServiceStatus.ERROR;
    }

    @Override
    public List<User> getAllFriends(Principal principal) {
        String name = principal.getName();
        List<User> friends = userService.getFriends(name);
        if (friends == null) {
            return Collections.emptyList();
        } else {
            //设置在线状态
            friends.forEach(user -> {
                if (sessionService.isOnline(user.getUsername())) {
                    user.setOnline(true);
                }
            });
            return friends;
        }
    }

    @Override
    public boolean existsFriendship(String username1, String username2) {
        EntityWrapper<Friendship> wrapper = new EntityWrapper<>();
        wrapper.eq("username1", username1).and().eq("username2", username2);
        int count = super.selectCount(wrapper);
        return count > 0;
    }

}

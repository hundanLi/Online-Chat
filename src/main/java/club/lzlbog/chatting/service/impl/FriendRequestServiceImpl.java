package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.dto.FriendRequestDTO;
import club.lzlbog.chatting.entity.FriendRequest;
import club.lzlbog.chatting.entity.Friendship;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.mapper.FriendRequestMapper;
import club.lzlbog.chatting.service.FriendRequestService;
import club.lzlbog.chatting.service.FriendshipService;
import club.lzlbog.chatting.service.ServiceStatus;
import club.lzlbog.chatting.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author li
 * @since 2019-04-26
 */
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements FriendRequestService {

    private final UserService userService;
    private final FriendshipService friendshipService;

    private static final int NOT_PROCESS = 0;
    private static final int ACCEPT = 1;
    private static final int REJECT = 2;

    @Autowired
    public FriendRequestServiceImpl(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @Override
    public int addRequest(FriendRequestDTO friendRequestDTO) {
        //1.验证用户是否存在
        String requestedUsername = friendRequestDTO.getRequested();
        User user = userService.findByUsername(requestedUsername);
        if (user == null) {
            return ServiceStatus.NOT_FOUND;
        }
        //2.验证好友关系是否已经存在
        boolean exists = friendshipService.existsFriendship(friendRequestDTO.getRequester(), friendRequestDTO.getRequested());
        if (exists) {
            return ServiceStatus.EXISTENCE;
        }
        //3.将好友申请插入数据库
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFriendRequester(friendRequestDTO.getRequester());
        friendRequest.setFriendRequested(friendRequestDTO.getRequested());
        friendRequest.setFriendRequestStatus(NOT_PROCESS);
        super.insert(friendRequest);
        return ServiceStatus.SUCCESS;
    }

    @Override
    public List<FriendRequest> getRequest(String username, int processed) {

        EntityWrapper<FriendRequest> wrapper = new EntityWrapper<>();
        wrapper.eq("friend_requested", username);
        if (processed != -1) {
            wrapper.and().eq("friend_request_status", processed);
        }
        return super.selectList(wrapper);
    }

    @Override
    public int acceptRequest(String requested, int requestId) {
        FriendRequest friendRequest = updateStatus(requested, requestId, ACCEPT);
        if (friendRequest != null) {
            //跟新friendship表
            Friendship friendship = new Friendship();
            friendship.setUsername1(friendRequest.getFriendRequester());
            friendship.setUsername2(friendRequest.getFriendRequested());
            return friendshipService.addFriendship(friendship);
        } else {
            return ServiceStatus.NOT_FOUND;
        }
    }

    @Override
    public int rejectRequest(String requested, int requestId) {
        FriendRequest friendRequest = updateStatus(requested, requestId, REJECT);
        if (friendRequest == null) {
            return ServiceStatus.NOT_FOUND;
        } else {
            return ServiceStatus.SUCCESS;
        }
    }

    private FriendRequest updateStatus(String requested, int requestId, int status) {
        //1.校验该requestId和username是否对应
        EntityWrapper<FriendRequest> wrapper = new EntityWrapper<>();
        wrapper.eq("friend_request_id", requestId)
                .and()
                .eq("friend_requested", requested)
                .and()
                .eq("friend_request_status", NOT_PROCESS);
        FriendRequest friendRequest = super.selectOne(wrapper);
        if (friendRequest == null) {
            return null;
        }
        //2.更新状态
        friendRequest.setFriendRequestStatus(status);
        friendRequest.updateById();
        return friendRequest;
    }

}

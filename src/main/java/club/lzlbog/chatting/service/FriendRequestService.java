package club.lzlbog.chatting.service;

import club.lzlbog.chatting.dto.FriendRequestDTO;
import club.lzlbog.chatting.entity.FriendRequest;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2019-04-26
 */
@Transactional(rollbackFor = Exception.class)
public interface FriendRequestService extends IService<FriendRequest> {

    /** 添加好友申请
     * @param friendRequestDTO  好友申请DTO
     * @return  int
     */
    int addRequest(FriendRequestDTO friendRequestDTO);

    /** 查询好友申请
     * @param username 用户名
     * @param processed 是否未处理的
     * @return  list
     */
    List<FriendRequest> getRequest(String username, int processed);

    /** 接受申请
     * @param requested  收到申请的用户名
     * @param requestId id
     * @return  int
     */
    int acceptRequest(String requested, int requestId);

    /** 拒绝好友请求
     * @param requested 收到申请的用户名
     * @param requestId id
     * @return  int
     */
    int rejectRequest(String requested, int requestId);
}

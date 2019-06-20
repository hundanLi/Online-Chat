package club.lzlbog.chatting.service;

import club.lzlbog.chatting.entity.Friendship;
import club.lzlbog.chatting.entity.User;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Transactional(rollbackFor = Exception.class)
public interface FriendshipService extends IService<Friendship> {

    /** 添加好友
     * @param friendship    好友关系映射
     * @return  操作正常或错误信息
     */
    int addFriendship(Friendship friendship);

    /** 删除好友
     * @param friendship    好友关系映射
     * @param principal     发起请求的用户信息
     * @return  操作正常或错误信息
     */
    int deleteFriendship(Friendship friendship, Principal principal);

    /** 查看所有好友
     * @param principal 登录用户信息
     * @return  好友列表
     */
    List<User> getAllFriends(Principal principal);


    /** 判断是否好友关系
     * @param username1 用户1
     * @param username2 用户2
     * @return  存在与否
     */
    boolean existsFriendship(String username1, String username2);
}

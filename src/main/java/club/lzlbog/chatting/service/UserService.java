package club.lzlbog.chatting.service;

import club.lzlbog.chatting.dto.RegisterDTO;
import club.lzlbog.chatting.entity.User;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@Transactional(rollbackFor = {Exception.class})
public interface UserService extends IService<User> {

    /** 注册服务，当注册失败时回滚事务
     * @param registerDTO 注册信息
     * @return  注册成功用户信息
     */
    User register(RegisterDTO registerDTO);

    /** 根据用户id找出所有好友
     * @param username    用户名
     * @return  好友列表
     */
    List<User> getFriends(String username);

    /** 查询邮箱是否已经注册
     * @param email 邮箱
     * @return  是否已经注册
     */
    boolean isEmailExists(String email);


    /** 查询用户名是否存在
     * @param username  用户名
     * @return  user
     */
    User findByUsername(String username);

    /** 修改用户密码
     * @param email 用户注册邮箱
     * @param newPassword   新密码
     * @return  是否修改成功
     */
    boolean updatePassword(String email, String newPassword);


    /** 根据用户名关键字查找用户
     * @param username 查询关键字
     * @return list
     */
    List<User> searchByUsername(String username);


    /** 修改用户头像
     * @param username  用户名
     * @param multipartFile 图片文件
     * @return    url
     */
    String updateAvatar(String username, MultipartFile multipartFile);

    /** 退出群
     * @param username 用户名
     * @param groupId  群组id
     * @return  int
     */
    int dropGroup(String username, String groupId);


    /** 获取个人资料（在线用户）
     * @param username  用户名
     * @return  user
     */
    User getProfile(String username);

}

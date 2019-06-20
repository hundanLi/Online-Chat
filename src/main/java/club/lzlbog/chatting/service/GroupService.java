package club.lzlbog.chatting.service;

import club.lzlbog.chatting.dto.GroupMemberDTO;
import club.lzlbog.chatting.entity.Group;
import club.lzlbog.chatting.entity.User;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Transactional(rollbackFor = Exception.class)
public interface GroupService extends IService<Group> {

    /**
     * 创建群组服务
     *
     * @param username 发起请求的用户名
     * @param group     群组信息
     * @return 操作完成或异常标识
     */
    int createGroup(String username, Group group);

    /** 删除群组
     * @param username  群主
     * @param groupId   群组id
     * @return  操作完成或异常标识
     */
    int deleteGroup(String username, String groupId);

    /** 添加成员
     * @param dto 新增成员id和群组信息
     * @return  操作完成或异常标识
     */
    int addMembers(GroupMemberDTO dto);

    /** 删除成员
     * @param dto 新增成员id和群组信息
     * @return  操作完成或异常标识
     */
    int removeMembers(GroupMemberDTO dto);

    /** 用户查看群成员
     * @param username  请求的用户
     * @param groupId   群组id
     * @return  List<User>
     */
    List<User> getMembers(String username, String groupId);

    /** 为用户获取所在群信息
     * @param userId id
     * @return list
     */
    List<Group> getGroups(String userId);
}

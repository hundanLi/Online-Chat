package club.lzlbog.chatting.service;

import club.lzlbog.chatting.entity.UserGroup;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Transactional(rollbackFor = Exception.class)
public interface UserGroupService extends IService<UserGroup> {

    /** 查找某群组的所有成员id
     * @param groupId groupId
     * @return  Set
     */
    Set<String> selectUserIdByGroupId(String groupId);
}

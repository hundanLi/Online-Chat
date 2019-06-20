package club.lzlbog.chatting.service;

import club.lzlbog.chatting.entity.UserRole;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Transactional(rollbackFor = Exception.class)
public interface UserRoleService {

    /** 关联用户与角色
     * @param userRole 关联对象
     * @return 操作成功与否
     */
    boolean link(UserRole userRole);
}

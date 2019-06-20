package club.lzlbog.chatting.mapper;

import club.lzlbog.chatting.entity.UserRole;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Repository
public interface UserRoleMapper {

    /** 关联用户与角色
     * @param userRole  用户角色关联记录
     * @return 修改行数
     */
    Integer insert(UserRole userRole);

}

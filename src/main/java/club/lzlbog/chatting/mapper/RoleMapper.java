package club.lzlbog.chatting.mapper;

import club.lzlbog.chatting.entity.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /** 根据角色id查找role信息
     * @param roleId 角色id
     * @return role对象
     */
    Role selectByRoleId(@Param("roleId") Integer roleId);
}

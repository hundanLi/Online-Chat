package club.lzlbog.chatting.mapper;

import club.lzlbog.chatting.entity.Authority;
import club.lzlbog.chatting.entity.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@Mapper
@Repository
public interface AuthorityMapper extends BaseMapper<Authority> {

    List<Authority> selectAuthorityByRoles(@Param("roles") List<Role> roles);
}

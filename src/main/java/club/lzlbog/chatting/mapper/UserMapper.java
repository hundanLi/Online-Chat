package club.lzlbog.chatting.mapper;

import club.lzlbog.chatting.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(rollbackFor = Exception.class)
public interface UserMapper extends BaseMapper<User> {
    /** 根据用户名获取用户信息对象
     * @param username 用户名
     * @return 用户信息对象
     */
    User selectUserByUsername(@Param("username") String username);

    /** 根据用户id查询好友列表
     * @param username  用户名
     * @return  好友列表
     */
    List<User> selectFriends(@Param("username") String username);

    /** 查找id
     * @param username  用户名
     * @return string
     */
    String selectIdByUsername(@Param("username") String username);
}

package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@TableName("tbl_user_role")
@Data
@ToString
@EqualsAndHashCode
public class UserRole {

    private String userId;
    private String roleId;

}

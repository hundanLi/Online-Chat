package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  用户角色
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@EqualsAndHashCode(callSuper = true)
@TableName("tbl_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.UUID)
    private String roleId;
    private String roleName;

    public Role(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    @TableField(exist = false)
    private List<Authority> authorities;

    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}

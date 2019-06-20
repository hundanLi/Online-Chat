package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  用户
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@ApiModel(description = "用户信息")
@RedisHash(value = "chatting-online-user")
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tbl_user")
@ToString
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.UUID)
    private String userId;

    @Id
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(hidden = true)
    private String password;

    @ApiModelProperty(value = "邮件地址")
    private String email;

    @ApiModelProperty(value = "头像图片地址")
    private String avatarUrl;

    @ApiModelProperty(value = "账号创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<Role> roles;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<Authority> authorities;

    @ApiModelProperty(value = "是否在线")
    @TableField(exist = false)
    private boolean online;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}

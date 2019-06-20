package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author li
 * @since 2019-04-26
 */
@ApiModel(description = "好友申请信息")
@EqualsAndHashCode(callSuper = true)
@TableName("tbl_friend_request")
@Data
public class FriendRequest extends Model<FriendRequest> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "好友申请id标识")
    @TableId(value = "friend_request_id", type = IdType.AUTO)
    private Long friendRequestId;
    @ApiModelProperty(value = "申请者")
    private String friendRequester;
    @ApiModelProperty(value = "被申请者")
    private String friendRequested;
    /**
     * 请求处理状态:
     *         0:未处理；
     *         1:已接受好友申请；
     *         2:已拒绝好友申请；
     */
    @ApiModelProperty(value = "申请状态")
    private Integer friendRequestStatus;
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime requestTime;

    @Override
    protected Serializable pkVal() {
        return this.friendRequestId;
    }

}

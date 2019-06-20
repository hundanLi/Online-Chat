package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@ApiModel(description = "群组信息")
@TableName("tbl_group")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Group extends Model<Group> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群组id")
    @TableId(value = "group_id", type = IdType.UUID)
    private String groupId;

    @ApiModelProperty(value = "群组名称")
    @NotBlank
    @Size(min = 4, max = 16, message = "群名称长度应为4~16个字符")
    private String groupName;

    /**
     * 群主的用户名
     */
    @ApiModelProperty(value = "群主用户名")
    private String groupOwner;
    /**
     * 群组当前成员数量
     */
    @ApiModelProperty(value = "群组成员数量")
    private Integer groupSize;
    @ApiModelProperty(value = "群主最大允许成员数量")
    private Integer groupMaxsize;
    private LocalDateTime groupCreatedTime;
    private LocalDateTime groupModifyTime;

    @Override
    protected Serializable pkVal() {
        return this.groupId;
    }

}

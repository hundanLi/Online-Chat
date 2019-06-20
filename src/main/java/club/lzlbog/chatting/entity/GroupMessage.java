package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author li
 * @since 2019-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tbl_group_message")
public class GroupMessage extends Model<GroupMessage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "group_message_id", type = IdType.AUTO)
    private Long groupMessageId;
    private String groupId;
    /**
     * 发送者用户名
     */
    private String groupMessageSender;
    private String groupMessageContent;
    private Date groupMessageTime;

    @Override
    protected Serializable pkVal() {
        return this.groupMessageId;
    }

}

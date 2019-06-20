package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@TableName("tbl_message")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Message extends Model<Message> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 状态：已读，未读
     */
    private Integer messageStatus;
    /**
     * 发送者
     */
    private String messageSender;
    /**
     * 接受者
     */
    private String messageReceiver;

    /**
     * 发送时间
     */
    private LocalDateTime messageTime;

    @Override
    protected Serializable pkVal() {
        return this.messageId;
    }

}

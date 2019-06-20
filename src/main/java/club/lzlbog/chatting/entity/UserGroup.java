package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *      用户群组归属关系映射类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@TableName("tbl_user_group")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup extends Model<UserGroup> implements Serializable{

    private static final long serialVersionUID = 1L;

    private String userId;
    private String groupId;
    private LocalDateTime createdTime;

    public UserGroup(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId + " " + this.groupId;
    }

}

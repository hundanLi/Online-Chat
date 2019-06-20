package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *      用户好友关系映射类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@TableName("tbl_friendship")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Friendship extends Model<Friendship> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字符")
    private String username1;
    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字符")
    private String username2;
    private LocalDateTime createdTime;

    public Friendship(@NotBlank @Size(min = 2, max = 16, message = "2-16位字符") String username1,
                      @NotBlank @Size(min = 2, max = 16, message = "2-16位字符") String username2) {
        this.username1 = username1;
        this.username2 = username2;
    }

    @Override
    protected Serializable pkVal() {
        return this.username1;
    }

}

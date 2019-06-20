package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author li
 * @since 2019-04-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tbl_last_query")
public class LastQuery extends Model<LastQuery> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "username", type = IdType.INPUT)
    private String username;
    private Long lastQueryId;

    @Override
    protected Serializable pkVal() {
        return this.username;
    }

}

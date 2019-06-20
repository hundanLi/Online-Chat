package club.lzlbog.chatting.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@EqualsAndHashCode(callSuper = true)
@TableName("tbl_authority")
@Data
@ToString
public class Authority extends Model<Authority> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "authority_id", type = IdType.UUID)
    private String authorityId;
    private String authorityName;

    @Override
    protected Serializable pkVal() {
        return this.authorityId;
    }

}

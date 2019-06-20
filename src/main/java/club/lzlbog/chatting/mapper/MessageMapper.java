package club.lzlbog.chatting.mapper;

import club.lzlbog.chatting.entity.Message;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {
    /** 根据messageId查询消息接收方
     * @param messageId 获取消息接收者
     * @return  接受者用户名
     */
    Message selectMessageWithOwner(@Param("messageId") Integer messageId);

    /** 批量更新消息状态
     * @param ids   消息id列表
     * @param newStatus 新状态
     * @return  更改行数
     */
    Integer updateStatusByBatch(@Param("ids") List<Integer> ids, @Param("newStatus") Integer newStatus);

}

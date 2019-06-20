package club.lzlbog.chatting.service;

import club.lzlbog.chatting.dto.GroupMessageDTO;
import club.lzlbog.chatting.entity.GroupMessage;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author li
 * @since 2019-04-23
 */
@Transactional(rollbackFor = Exception.class)
public interface GroupMessageService extends IService<GroupMessage> {

    /**
     * 添加消息
     *
     * @param dto 消息数据对象
     * @return int
     */
    int addMessage(GroupMessageDTO dto);

    /**
     * 获取未读群消息
     *
     * @param username 用户名
     * @return list
     */
    List<GroupMessage> getMessage(String username);

    /**
     * 获取历史群消息
     *
     * @param username 用户名
     * @param groupId  群组id
     * @param pageNum  页码
     * @return list
     */
    Page<GroupMessage> getHistoryMessage(String username, String groupId, int pageNum);
}

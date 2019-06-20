package club.lzlbog.chatting.service;

import club.lzlbog.chatting.dto.MessageDTO;
import club.lzlbog.chatting.entity.Message;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author li
 * @date 2019-03-24
 */
@Transactional(rollbackFor = Exception.class)
public interface MessageService extends IService<Message> {

    /**
     * 新增消息
     *
     * @param messageDTO 消息收发双方及内容
     * @return 返回信息
     */
    Map<String, Object> addMessage(MessageDTO messageDTO);

    /**
     * 根据id删除消息
     *
     * @param username  用户信息
     * @param messageId 消息id
     * @return 成功与否
     */
    boolean deleteMessage(String username, int messageId);

    /**
     * 获取未读消息
     *
     * @param username 用户名
     * @return 消息列表
     */
    List<Message> getUnreadMessage(String username);

    /**
     * 获取历史消息
     *
     * @param username 用户名
     * @param friend   好友
     * @param pageNum  分页页码
     * @return 分页显示消息
     */
    Page<Message> getHistoryMessages(String username, String friend, int pageNum);
}

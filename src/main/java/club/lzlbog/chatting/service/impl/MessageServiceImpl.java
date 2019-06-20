package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.dto.MessageDTO;
import club.lzlbog.chatting.entity.Message;
import club.lzlbog.chatting.mapper.MessageMapper;
import club.lzlbog.chatting.service.FriendshipService;
import club.lzlbog.chatting.service.MessageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 消息服务实现类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private static final int UNREAD = 0;
    private static final int READ = 1;
    private static final int SENDER_REMOVING = -1;
    private static final int RECEIVER_REMOVING = -2;
    private static final int BOTH_REMOVING = -3;

    private static final int PAGE_SIZE = 10;
    private final FriendshipService friendshipService;
    private final MessageMapper messageMapper;

    /**
     * 维护用户是否收到新消息
     */
    private static ConcurrentHashMap<String, Boolean> hasUnreadMessage = new ConcurrentHashMap<>();


    @Autowired
    public MessageServiceImpl(FriendshipService friendshipService, MessageMapper messageMapper) {
        this.friendshipService = friendshipService;
        this.messageMapper = messageMapper;
    }

    @Override
    public Map<String, Object> addMessage(MessageDTO messageDTO) {
        //验证用户合法性：是否好友
        Map<String, Object> map = new HashMap<>(4);
        boolean hasFriendship = friendshipService.existsFriendship(messageDTO.getSender(), messageDTO.getReceiver());
        if (!hasFriendship) {
            map.put("status", "forbidden");
            map.put("message", "权限不足!");
            return map;
        }
        //构造消息对象
        Message message = new Message();
        message.setMessageContent(messageDTO.getContent());
        message.setMessageSender(messageDTO.getSender());
        message.setMessageReceiver(messageDTO.getReceiver());
        message.setMessageStatus(UNREAD);
        //添加消息
        boolean insert = super.insert(message);
        if (insert) {
            map.put("status", "success");
            map.put("message", "发送成功！");
            map.put("messageId", message.getMessageId());
            hasUnreadMessage.put(message.getMessageReceiver(), true);
        } else {
            map.put("status", "error");
            map.put("message", "发送失败，请稍后重试！");
        }
        return map;
    }

    @Override
    public boolean deleteMessage(String username, int messageId) {
        //校验操作合法性
        Message message = messageMapper.selectMessageWithOwner(messageId);
        if (message == null || message.getMessageStatus() == BOTH_REMOVING) {
            return true;
        }
        int oldStatus = message.getMessageStatus();
        int newStatus = oldStatus;
        if (message.getMessageSender().equals(username)) {
            if (oldStatus == RECEIVER_REMOVING) {
                //接收方已删除，则更新为双方删除
                newStatus = BOTH_REMOVING;
            } else {
                newStatus = SENDER_REMOVING;
            }
        } else if (message.getMessageReceiver().equals(username)) {
            if (oldStatus == SENDER_REMOVING) {
                //若发送方已删除，则更新为双方删除
                newStatus = BOTH_REMOVING;
            } else if (oldStatus == READ) {
                newStatus = RECEIVER_REMOVING;
            }
        } else {
            return false;
        }
        //更新状态
        //设无关字段为null减少数据库的写操作
        message.setMessageSender(null);
        message.setMessageReceiver(null);
        if (newStatus == BOTH_REMOVING) {
            //删除操作
            return message.deleteById();
        } else {
            //更新操作
            message.setMessageStatus(newStatus);
            return message.updateById();
        }

    }

    @Override
    public List<Message> getUnreadMessage(String username) {
        if (!hasUnreadMessage.getOrDefault(username, false)) {
            return Collections.emptyList();
        }
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("message_receiver", username).and().eq("message_status", UNREAD);
        List<Message> messages = super.selectList(wrapper);
        if (messages == null || messages.size() == 0) {
            return Collections.emptyList();
        }
        //更新状态
        List<Integer> ids = new ArrayList<>(messages.size());
        for (Message message : messages) {
            ids.add(message.getMessageId());
        }
        messageMapper.updateStatusByBatch(ids, READ);
        hasUnreadMessage.put(username, false);
        return messages;
    }

    @Override
    public Page<Message> getHistoryMessages(String username, String friend, int pageNum) {
        //构建分页对象
        Page<Message> page = new Page<>(pageNum, PAGE_SIZE);
        //构建wrapper对象
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("message_sender", username).and().eq("message_receiver", friend)
                .orNew()
                .eq("message_sender", friend).and().eq("message_receiver", username)
                .orderDesc(Collections.singletonList("message_id"));
        return super.selectPage(page, wrapper);
    }
}

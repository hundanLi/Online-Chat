package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.dto.GroupMessageDTO;
import club.lzlbog.chatting.entity.GroupMessage;
import club.lzlbog.chatting.entity.LastQuery;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.entity.UserGroup;
import club.lzlbog.chatting.mapper.GroupMessageMapper;
import club.lzlbog.chatting.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author li
 * @since 2019-04-23
 */
@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessage> implements GroupMessageService {

    private final UserGroupService userGroupService;
    private final UserService userService;
    private final LastQueryService lastQueryService;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public GroupMessageServiceImpl(UserGroupService userGroupService, UserService userService, LastQueryService lastQueryService) {
        this.userGroupService = userGroupService;
        this.userService = userService;
        this.lastQueryService = lastQueryService;
    }

    @Override
    public int addMessage(GroupMessageDTO dto) {
        //判断群是否存在该成员
        User user = userService.findByUsername(dto.getSender());
        EntityWrapper<UserGroup> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", user.getUserId())
                .and()
                .eq("group_id", dto.getGroupId());
        int count = userGroupService.selectCount(wrapper);
        if (count < 0) {
            return ServiceStatus.FORBIDDEN;
        }

        GroupMessage message = new GroupMessage();
        message.setGroupId(dto.getGroupId());
        message.setGroupMessageContent(dto.getContent());
        message.setGroupMessageSender(dto.getSender());
        //插入数据库
        super.insert(message);
        return ServiceStatus.SUCCESS;
    }

    @Override
    public List<GroupMessage> getMessage(String username) {
        //1.查询上次的访问记录
        EntityWrapper<LastQuery> wrapper = new EntityWrapper<>();
        wrapper.eq("username", username);
        LastQuery lastQuery = lastQueryService.selectOne(wrapper);
        if (lastQuery == null) {
            lastQuery = new LastQuery();
            lastQuery.setLastQueryId(0L);
            lastQuery.setUsername(username);
            lastQuery.insert();
        }
        //2.查询用户加入的群组
        User user = userService.findByUsername(username);
        EntityWrapper<UserGroup> ugWrapper = new EntityWrapper<>();
        ugWrapper.eq("user_id", user.getUserId());
        List<UserGroup> userGroups = userGroupService.selectList(ugWrapper);
        List<String> groupIds = new ArrayList<>(userGroups.size());
        userGroups.forEach(ug -> groupIds.add(ug.getGroupId()));

        //3.查询群消息表，将id值大于lastQuery的消息找出来
        EntityWrapper<GroupMessage> gmWrapper = new EntityWrapper<>();
        gmWrapper.gt("group_message_id", lastQuery.getLastQueryId())
                .and()
                .in("group_id", groupIds);
        List<GroupMessage> groupMessages = super.selectList(gmWrapper);

        //4.更新访问记录
        if (groupMessages.size() > 0) {
            lastQuery.setLastQueryId(groupMessages.get(groupMessages.size() - 1).getGroupMessageId());
            lastQuery.updateById();
        }

        return groupMessages;
    }

    @Override
    public Page<GroupMessage> getHistoryMessage(String username, String groupId, int pageNum) {
        //1.验证是否是群组成员
        //查找用户id
        EntityWrapper<User> uWrapper = new EntityWrapper<>();
        uWrapper.eq("username", username);
        User user = userService.findByUsername(username);
        //验证
        EntityWrapper<UserGroup> ugWrapper = new EntityWrapper<>();
        ugWrapper.eq("user_id", user.getUserId()).and().eq("group_id", groupId);
        UserGroup userGroup = userGroupService.selectOne(ugWrapper);
        if (userGroup == null) {
            return null;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }
        Page<GroupMessage> page = new Page<>(pageNum, PAGE_SIZE);
        EntityWrapper<GroupMessage> gmWrapper = new EntityWrapper<>();
        gmWrapper.eq("group_id", groupId).orderDesc(Collections.singletonList("group_message_id"));
        return super.selectPage(page, gmWrapper);
    }

}

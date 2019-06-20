package club.lzlbog.chatting.controller;


import club.lzlbog.chatting.dto.HistoryMessageDTO;
import club.lzlbog.chatting.dto.MessageDTO;
import club.lzlbog.chatting.entity.Message;
import club.lzlbog.chatting.service.MessageService;
import club.lzlbog.chatting.util.Validators;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息管理前端控制器
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Api(tags = "消息接口")
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @ApiOperation(
            value = "发送消息",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("#principal.name == #messageDTO.sender")
    @PostMapping("/send")
    public Object sendMessage(@ApiIgnore Principal principal,
                              @RequestBody @Valid MessageDTO messageDTO,
                              @ApiIgnore Errors errors) {
        //数据校验
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        return messageService.addMessage(messageDTO);
    }

    @ApiOperation(
            value = "接收消息",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @GetMapping("/receive")
    public Object receiveMessage(@ApiIgnore Principal principal) {
        List<Message> unreadMessage = messageService.getUnreadMessage(principal.getName());
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", "success");
        map.put("unread_messages", unreadMessage);
        return map;
    }

    @ApiOperation(
            value = "删除消息",
            httpMethod = "DELETE",
            authorizations = @Authorization("登录")
    )
    @DeleteMapping("/delete/{messageId}")
    public Object deleteMessage(@ApiIgnore Principal principal,
                                @PathVariable("messageId") int messageId) {
        boolean b = messageService.deleteMessage(principal.getName(), messageId);
        Map<String, Object> map = new HashMap<>(2);
        if (b) {
            map.put("status", "success");
            map.put("message", "删除成功！");
        } else {
            map.put("status", "fail");
            map.put("message", "权限不足！");
        }
        return map;
    }

    @ApiOperation(
            value = "获取历史消息，按时间倒序",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @GetMapping("/history")
    public Object getHistoryMessage(@ApiIgnore Principal principal,
                                    @RequestBody @Valid HistoryMessageDTO dto,
                                    @ApiIgnore Errors errors) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        Page<Message> messages = messageService.getHistoryMessages(principal.getName(), dto.getFriend(), dto.getPageNum());
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", "success");
        map.put("historyMessages", messages);
        return map;
    }
}


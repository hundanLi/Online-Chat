package club.lzlbog.chatting.controller;


import club.lzlbog.chatting.dto.GroupMessageDTO;
import club.lzlbog.chatting.entity.GroupMessage;
import club.lzlbog.chatting.service.GroupMessageService;
import club.lzlbog.chatting.util.ResponseUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2019-04-23
 */
@Api(tags = "群消息接口")
@RestController
@RequestMapping("/groupMessage")
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    @Autowired
    public GroupMessageController(GroupMessageService groupMessageService) {
        this.groupMessageService = groupMessageService;
    }

    @ApiOperation(
            value = "发送群消息",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("#principal.name == #dto.sender")
    @PostMapping("/send")
    public Object sendGroupMessage(@RequestBody @Valid GroupMessageDTO dto,
                                   @ApiIgnore Errors errors,
                                   @ApiIgnore Principal principal) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        //调用service层
        int status = groupMessageService.addMessage(dto);
        return ResponseUtils.getResultMap(status);
    }

    @ApiOperation(
            value = "接收群消息",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/receive")
    public Object receiveGroupMessage(@ApiIgnore HttpServletRequest request){
        String username = request.getUserPrincipal().getName();
        List<GroupMessage> messages = groupMessageService.getMessage(username);
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", "success");
        map.put("messages", messages);
        return map;
    }

    @ApiOperation(
            value = "获取历史消息",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history/{groupId}/{pageNum}")
    public Object getHistoryMessage(@ApiIgnore HttpServletRequest request,
                                    @PathVariable("groupId") String groupId,
                                    @PathVariable("pageNum") int pageNum) {
        String username = request.getUserPrincipal().getName();
        Page<GroupMessage> messages = groupMessageService.getHistoryMessage(username, groupId, pageNum);
        Map<String, Object> map = new HashMap<>(2);
        if (messages == null) {
            map.put("status", "fail");
            map.put("message", "权限不足！");
            return map;
        }
        map.put("status", "success");
        map.put("historyMessages", messages);
        return map;
    }

}


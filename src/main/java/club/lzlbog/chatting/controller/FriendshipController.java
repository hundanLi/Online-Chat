package club.lzlbog.chatting.controller;


import club.lzlbog.chatting.entity.Friendship;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.service.FriendshipService;
import club.lzlbog.chatting.service.ServiceStatus;
import club.lzlbog.chatting.util.Validators;
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
 * 好友关系前端控制器
 * </p>
 *
 * @author li
 * @date 2019-03-24
 */
@Api(tags = "好友关系管理")
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }


    @ApiOperation(
            value = "删除好友",
            httpMethod = "DELETE",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("#friendship.username1==#principal.name")
    @DeleteMapping("/deleteFriend")
    public Object deleteFriend(@Valid @RequestBody Friendship friendship,
                               @ApiIgnore Errors errors,
                               @ApiIgnore Principal principal) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        Map<String, Object> map = new HashMap<>(2);
        int status = friendshipService.deleteFriendship(friendship, principal);
        if (ServiceStatus.FORBIDDEN == status) {
            //无权限操作
            map.put("status", "forbidden");
            map.put("message", "无权限！");
        } else if (ServiceStatus.SUCCESS == status || ServiceStatus.EXISTENCE == status) {
            //删除成功或者不存在好友关系
            map.put("status", "success");
            map.put("message", "删除成功！");
        } else if (ServiceStatus.NOT_FOUND == status) {
            //用户不存在
            map.put("status", "not found");
            map.put("message", "没有好友关系！");
        } else {
            //服务器错误
            map.put("status", "error");
            map.put("message", "服务器错误！请稍后重试...");
        }
        return map;
    }

    @ApiOperation(
            value = "获取好友列表",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAllFriends")
    public Map<String, Object> getFriends(@ApiIgnore Principal principal) {
        List<User> friends = friendshipService.getAllFriends(principal);
        Map<String, Object> map = new HashMap<>(4);
        map.put("status", "success");
        map.put("message", "获取成功！");
        map.put("friends", friends);
        return map;
    }
}


package club.lzlbog.chatting.controller;


import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.service.UserService;
import club.lzlbog.chatting.util.CheckContentType;
import club.lzlbog.chatting.util.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author li
 * @date 2019-03-17
 */

@Api(tags = "用户接口", description = "个人信息，搜索用户，修改头像")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(
            value = "获取用户个人资料",
            httpMethod = "GET",
            authorizations = @Authorization("已登录")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public User getUserInfo(@ApiIgnore HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        User user = userService.findByUsername(username);
        user.setPassword(null);
        return user;
    }

    @ApiOperation(
            value = "搜索用户（含路径参数username）",
            httpMethod = "GET",
            authorizations = @Authorization("已登录")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search/{username}")
    public List<User> searchUser(@PathVariable("username") String username) {
        return userService.searchByUsername(username);
    }

    @ApiOperation(
            value = "修改用户头像",
            httpMethod = "POST",
            authorizations = @Authorization("已登录")
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changeAvatar")
    public Object changeAvatar(@ApiIgnore HttpServletRequest request,
                               @RequestParam("avatar") MultipartFile multipartFile) {
        long size = multipartFile.getSize();
        Map<String, Object> map = new HashMap<>(2);
        //检查文件大小
        if (size > MAX_FILE_SIZE) {
            map.put("status", "fail");
            map.put("message", "文件大小不可以超过" + MAX_FILE_SIZE / (1024 * 1024) + "M！");
            return map;
        }
        //检查文件格式
        boolean checkImage = CheckContentType.checkImage(multipartFile.getContentType());
        if (!checkImage) {
            map.put("status", "fail");
            map.put("message", "文件格式不正确！");
            return map;
        }
        String username = request.getUserPrincipal().getName();
        String avatar = userService.updateAvatar(username, multipartFile);
        if (avatar == null) {
            map.put("status", "fail");
            map.put("message", "更新失败，请稍后重试！");
            return map;
        }
        map.put("status", "success");
        map.put("avatarUrl", avatar);
        map.put("message", "修改成功！");
        return map;
    }


    @ApiOperation(
            value = "退出群聊",
            httpMethod = "DELETE",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/dropGroup/{groupId}")
    public Object dropGroup(HttpServletRequest request,
                            @PathVariable("groupId") String groupId) {
        String username = request.getUserPrincipal().getName();
        int status = userService.dropGroup(username, groupId);
        return ResponseUtils.getResultMap(status);
    }

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;

    private final UserService userService;

}


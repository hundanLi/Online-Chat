package club.lzlbog.chatting.controller;

import club.lzlbog.chatting.entity.Group;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.service.GroupService;
import club.lzlbog.chatting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-23 13:08
 **/
@Api(tags = "页面跳转控制接口")
@Controller
public class PageController {

    private final UserService userService;

    private final GroupService groupService;

    @Autowired
    public PageController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @ApiOperation(value = "登录页面")
    @GetMapping({"/signin"})
    public String signinPage() {
        return "signin";
    }

    @ApiOperation(value = "注册页面")
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @ApiOperation(value = "聊天主窗口")
    @GetMapping("/")
    public String mainPage(HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "signin";
        }
        //获取用户信息
        User user = userService.findByUsername(principal.getName());
        user.setPassword(null);

        //获取好友列表
        List<User> friends = userService.getFriends(user.getUsername());
        //获取群组列表
        List<Group> groups = groupService.getGroups(user.getUserId());
        model.addAttribute("userInfo", user);
        model.addAttribute("friends", friends);
        model.addAttribute("groups", groups);
        return "chat";
    }

    @ApiOperation(value = "修改密码页面")
    @GetMapping("/resetPassword")
    public String resetPwdPage() {
        return "resetpwd";
    }

}

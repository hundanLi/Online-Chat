package club.lzlbog.chatting.controller;

import club.lzlbog.chatting.dto.RegisterDTO;
import club.lzlbog.chatting.dto.ResetPasswordDTO;
import club.lzlbog.chatting.dto.ValidationCodeDTO;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.service.UserService;
import club.lzlbog.chatting.util.MailUtils;
import club.lzlbog.chatting.util.Validators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 15:37
 **/
@Api(tags = "用户认证接口", description = "注册,验证码发送,修改密码")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public AuthController(UserService userService, MailUtils mailUtils) {
        this.userService = userService;
        this.mailUtils = mailUtils;
    }

    @ApiOperation(
            value = "注册",
            httpMethod = "POST"
    )
    @PostMapping("/signup")
    public Object register(@Validated @RequestBody RegisterDTO registerDTO,
                           @ApiIgnore Errors errors,
                           @ApiIgnore HttpSession session) {

        Map<String, Object> rs = Validators.validate(errors);
        if (rs != null) {
            return rs;
        }
        Map<String, Object> map = new HashMap<>(2);
        if (!registerDTO.getPassword().equals(registerDTO.getRepeatPassword())) {
            map.put("status", "fail");
            map.put("message", "密码不一致！");
            return map;
        }
        //检查email和验证码
        String email = (String) session.getAttribute(REGISTER_EMAIL_KEY);
        String code = (String) session.getAttribute(SEND_FOR_REGISTER_CODE_KEY);
        if (!registerDTO.getEmail().equals(email) || !registerDTO.getValidationCode().equals(code)) {
            map.put("status", "fail");
            map.put("message", "邮箱被篡改或者验证码输入错误！");
            return map;
        }

        User user = userService.register(registerDTO);
        if (user != null) {
            user.setPassword(null);
            map.put("status", "success");
            map.put("userInfo", user);
        } else {
            map.put("status", "fail");
            map.put("message", "用户名已存在！");
        }
        return map;
    }

    @ApiOperation(
            value = "获取验证码",
            httpMethod = "POST"
    )
    @PostMapping("/sendCode")
    public Object sendValidationCode(@Validated @RequestBody ValidationCodeDTO validationCodeDTO,
                                     @ApiIgnore Errors errors,
                                     @ApiIgnore HttpSession session) {
        Object lastSend = session.getAttribute(MailUtils.LAST_SEND);

        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        Map<String, Object> map = new HashMap<>(4);
        if (lastSend != null && System.currentTimeMillis() - (long) lastSend < EXPIRES) {
            map.put("status", "fail");
            map.put("message", "你的请求频率过高！请稍后再操作。");
            return map;
        }

        //预处理
        validationCodeDTO.setEmail(validationCodeDTO.getEmail().toLowerCase());

        String emailKey = null;
        String sendForKey = null;
        //区分注册或修改密码
        if (validationCodeDTO.getSendFor().equals(REGISTER_FLAG)) {
            //注册验证码

            //查看有无注册
            boolean emailExists = userService.isEmailExists(validationCodeDTO.getEmail());
            if (emailExists) {
                map.put("status", "fail");
                map.put("message", "邮箱已被注册！");
                return map;
            } else {
                emailKey = REGISTER_EMAIL_KEY;
                sendForKey = SEND_FOR_REGISTER_CODE_KEY;
            }
        } else if (validationCodeDTO.getSendFor().equals(RESET_PASSWORD_FLAG)) {
            //重置密码验证码

            //查看有无注册
            boolean emailExists = userService.isEmailExists(validationCodeDTO.getEmail());
            if (!emailExists) {
                map.put("status", "fail");
                map.put("message", "邮箱未注册！");
                return map;
            } else {
                emailKey = RESET_PASSWORD_EMAIL_KEY;
                sendForKey = SEND_FOR_RESET_PASSWORD_CODE_KEY;
            }
        }
        boolean b = mailUtils.sendEmail(validationCodeDTO.getEmail(), emailKey, sendForKey, session);
        if (b) {
            map.put("status", "success");
            map.put("message", "验证码发送成功！");
        } else {
            map.put("status", "fail");
            map.put("message", "发送失败，请稍后重试...");

        }
        return map;
    }

    @ApiOperation(
            value = "修改密码",
            httpMethod = "POST"
    )
    @ApiParam(name = "errors", hidden = true)
    @PostMapping("/resetPassword")
    public Object resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO,
                                @ApiIgnore Errors errors,
                                @ApiIgnore HttpSession session) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        Map<String, Object> map = new HashMap<>(4);
        String email = (String) session.getAttribute(RESET_PASSWORD_EMAIL_KEY);
        String code = (String) session.getAttribute(SEND_FOR_RESET_PASSWORD_CODE_KEY);
        if (!resetPasswordDTO.getEmail().equals(email) || !resetPasswordDTO.getValidationCode().equals(code)) {
            map.put("status", "fail");
            map.put("message", "邮箱非法或验证码错误！");
            return map;
        }
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getRepeatNewPassword())) {
            map.put("status", "fail");
            map.put("message", "两次密码输入不一致！");
            return map;
        }
        //更新密码
        boolean isSuccess = userService.updatePassword(email, resetPasswordDTO.getNewPassword());
        if (isSuccess) {
            map.put("status", "success");
            map.put("message", "密码修改成功！");
        } else {
            map.put("status", "error");
            map.put("message", "服务器错误，请稍后重试！");
        }
        return map;
    }


    /**
     * 注册验证码的键
     */
    private static final String SEND_FOR_REGISTER_CODE_KEY = "registerValidationCode";
    /**
     * 重置密码获取验证码的键
     */
    private static final String SEND_FOR_RESET_PASSWORD_CODE_KEY = "resetPasswordValidationCode";
    /**
     * 会话中保存注册邮箱的键
     */
    private static final String REGISTER_EMAIL_KEY = "registerEmail";
    /**
     * 会话中保存重置密码邮箱的键
     */
    private static final String RESET_PASSWORD_EMAIL_KEY = "resetPasswordEmail";

    private static final String REGISTER_FLAG = "1";

    private static final String RESET_PASSWORD_FLAG = "2";

    private final UserService userService;

    private final MailUtils mailUtils;

    private static final long EXPIRES = 60 * 1000;
}

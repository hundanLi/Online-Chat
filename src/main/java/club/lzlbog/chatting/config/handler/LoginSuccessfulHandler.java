package club.lzlbog.chatting.config.handler;

import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.service.SessionService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 11:52
 **/
@Component
public class LoginSuccessfulHandler implements AuthenticationSuccessHandler {

    private SessionService sessionService;

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //获取用户信息
        User user = new User();
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("username", authentication.getName());
        user = user.selectOne(wrapper);
        user.setPassword(null);
        user.setOnline(true);

        //保存到session
        sessionService.save(request, user);

        //json提交的登录处理
        if (MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())
                || MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {

            //设置token和状态码
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> map = new HashMap<>(2);
            map.put("message", "登录成功");
            map.put("userInfo", user);
            //返回
            response.getWriter().write(JSON.toJSONString(map));
            response.getWriter().flush();
        } else {
            //表单提交的处理
            response.sendRedirect("/");
        }
    }

}

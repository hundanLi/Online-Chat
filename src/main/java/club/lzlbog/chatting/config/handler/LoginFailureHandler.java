package club.lzlbog.chatting.config.handler;

import com.alibaba.fastjson.JSON;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 12:06
 **/
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", "fail");
        map.put("message", "用户名或密码错误!");
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(JSON.toJSONString(map));
        response.getWriter().flush();

    }
}

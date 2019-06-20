package club.lzlbog.chatting.config.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-31 17:30
 **/
@Component
public class RestAccessDeniedHandler extends AccessDeniedHandlerImpl {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //请求头的ContentType = "application/json"
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())) {
            Map<String, Object> map = new HashMap<>(2);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            map.put("status", "forbidden");
            map.put("message", "权限不足！");
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString(map));
            response.getWriter().flush();
        } else {
            //web页面，其实父类也是返回类似的json串，写完才发现。。。
            super.handle(request, response, accessDeniedException);
        }
    }

}

package club.lzlbog.chatting.handler;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-27 14:48
 **/
@RestControllerAdvice
public class MethodNotSupportedExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleException(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Exception ex) {
        Map<String, Object> map = new HashMap<>(2);
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        map.put("status", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        map.put("url", request.getRequestURL());
        map.put("error", ex.getMessage());
        return map;
    }
}

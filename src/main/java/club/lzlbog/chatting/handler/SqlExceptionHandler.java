package club.lzlbog.chatting.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-27 15:10
 **/
@RestControllerAdvice
public class SqlExceptionHandler {

    @ExceptionHandler({SQLException.class})
    public Object handleException(HttpServletRequest request,
                                  Exception exception) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("error", exception.getMessage());
        map.put("url", request.getRequestURL());
        return map;
    }
}

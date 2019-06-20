package club.lzlbog.chatting.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-27 11:26
 **/
@RestControllerAdvice
public class FileUploadExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Map<String, Object> handleFileExceededException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              MaxUploadSizeExceededException exception) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("status", "forbidden");
        map.put("message", "文件大小不能超过" + exception.getMaxUploadSize() + "！");
        map.put("error", exception.getMessage());
        map.put("url", request.getRequestURL());
        return map;
    }

}

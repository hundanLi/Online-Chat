package club.lzlbog.chatting.util;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 16:23
 **/
public class Validators {
    public static Map<String, Object> validate(Errors errors) {
        if (errors.hasErrors()) {
            Map<String, Object> map = new HashMap<>(4);
            Map<String, String> errorMap = new HashMap<>(8);
            for (FieldError error : errors.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            map.put("status", "fail");
            map.put("message", "数据不合法！");
            map.put("errors", errorMap);
            return map;
        }
        return null;
    }
}

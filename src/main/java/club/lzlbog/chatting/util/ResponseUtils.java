package club.lzlbog.chatting.util;

import club.lzlbog.chatting.service.ServiceStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-23 00:23
 **/
public class ResponseUtils {
    public static Map<String, Object> getResultMap(int status) {
        Map<String, Object> map = new HashMap<>(4);
        if (status == ServiceStatus.SUCCESS) {
            map.put("status", "success");
            map.put("message", "操作成功！");
        } else if (status == ServiceStatus.FORBIDDEN) {
            map.put("status", "forbidden");
            map.put("message", "没有权限！");
        } else if (status == ServiceStatus.NOT_FOUND) {
            map.put("status", "not found");
            map.put("message", "目标不存在！");
        } else {
            map.put("status", "error");
            map.put("message", "服务器错误，请稍后重试...");
        }
        return map;
    }
}

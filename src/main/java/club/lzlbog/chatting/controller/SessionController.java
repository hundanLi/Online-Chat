package club.lzlbog.chatting.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-28 13:31
 **/
@ApiIgnore
@RestController
@RequestMapping("/session")
public class SessionController {
    private RedisOperationsSessionRepository sessionRepository;

    @Autowired
    public void setSessionRepository(RedisOperationsSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("")
    public Object session(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Map<String, Object> map = new HashMap<>(4);
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(username);
        sessions.keySet().forEach(s -> {
            Session session = sessions.get(s);
            map.put(session.getId(), session.isExpired());
            Set<String> attributeNames = session.getAttributeNames();
            attributeNames.forEach(s1 -> {
                Object attribute = session.getAttribute(s1);
                map.put(s1, attribute);
            });
        });
        return map;
    }

}

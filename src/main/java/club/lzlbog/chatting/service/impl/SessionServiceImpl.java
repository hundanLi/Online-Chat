package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.service.SessionService;
import club.lzlbog.chatting.session.SessionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-28 17:43
 **/
@Service
public class SessionServiceImpl implements SessionService {

    private static final String SESSION_DETAIL = "SESSION_DETAIL";
    @Override
    public boolean isOnline(String username) {
        return repository.findByPrincipalName(username).size() != 0;
    }

    @Override
    public void save(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        if (session != null) {
            //保存登录用户session信息
            String remoteAddr = getRemoteAddress(request);
            String accessType = request.getHeader("User-Agent");
            SessionDetail sessionDetail = new SessionDetail();
            sessionDetail.setLocation(remoteAddr);
            sessionDetail.setAccessType(accessType);
            sessionDetail.setUserInfo(user);
            session.setAttribute(SESSION_DETAIL, sessionDetail);
        }
    }

    @Override
    public void delete(String username) {
        Map<String, ?extends Session> sessionMap = redisOperationsSessionRepository.findByPrincipalName(username);
        sessionMap.keySet().forEach(s -> redisOperationsSessionRepository.deleteById(s));
    }

    @Override
    public User getUserInfo(String username) {
        Map<String, ? extends Session> sessionMap = redisOperationsSessionRepository.findByPrincipalName(username);
        Collection<? extends Session> values = sessionMap.values();
        Iterator<? extends Session> iterator = values.iterator();
        SessionDetail sessionDetail = null;
        while (iterator.hasNext()) {
            Session session = iterator.next();
            Set<String> attributeNames = session.getAttributeNames();
            for (String s : attributeNames) {
                if (s.equals(SESSION_DETAIL)) {
                    sessionDetail = session.getAttribute(s);
                    break;
                }
            }
        }
        return sessionDetail == null ? null : sessionDetail.getUserInfo();
    }


    private String getRemoteAddress(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (remoteAddr == null) {
            remoteAddr = request.getRemoteAddr();
        }
        else if (remoteAddr.contains(",")) {
            remoteAddr = remoteAddr.split(",")[0];
        }
        return remoteAddr;
    }




    private FindByIndexNameSessionRepository<? extends Session> repository;

    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @Autowired
    public void setRedisOperationsSessionRepository(RedisOperationsSessionRepository redisOperationsSessionRepository) {
        this.redisOperationsSessionRepository = redisOperationsSessionRepository;
    }

    @Autowired
    public void setRepository(FindByIndexNameSessionRepository<? extends Session> repository) {
        this.repository = repository;
    }
}

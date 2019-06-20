package club.lzlbog.chatting.service;

import club.lzlbog.chatting.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-28 17:34
 **/
public interface SessionService {

    /** 查看用户是否在线
     * @param username  用户名
     * @return  boolean
     */
    boolean isOnline(String username);

    /** 保存session数据
     * @param request 请求
     * @param user  登录用户
     */
    void save(HttpServletRequest request, User user);

    /** 删除session
     * @param username  用户名
     */
    void delete(String username);


    /** 获取用户信息
     * @param username 用户名
     * @return  User
     */
    User getUserInfo(String username);
}

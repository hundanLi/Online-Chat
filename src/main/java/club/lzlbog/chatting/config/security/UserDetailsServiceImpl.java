package club.lzlbog.chatting.config.security;

import club.lzlbog.chatting.entity.Authority;
import club.lzlbog.chatting.entity.User;
import club.lzlbog.chatting.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 13:02
 **/
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名获取用户信息
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        //分配权限
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        if (user.getAuthorities() != null) {
            for (Authority authority : user.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(authority.getAuthorityName()));
            }
        }

        //构建UserDetails返回
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}

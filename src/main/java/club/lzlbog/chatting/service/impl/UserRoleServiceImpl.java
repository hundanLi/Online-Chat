package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.entity.UserRole;
import club.lzlbog.chatting.mapper.UserRoleMapper;
import club.lzlbog.chatting.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper mapper;
    @Override
    public boolean link(UserRole userRole) {
        Integer insert = mapper.insert(userRole);
        return insert > 0;
    }
}

package club.lzlbog.chatting.service.impl;

import club.lzlbog.chatting.entity.UserGroup;
import club.lzlbog.chatting.mapper.UserGroupMapper;
import club.lzlbog.chatting.service.UserGroupService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2019-03-24
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {

    @Override
    public Set<String> selectUserIdByGroupId(String groupId) {
        EntityWrapper<UserGroup> wrapper = new EntityWrapper<>();
        wrapper.eq("group_id", groupId);
        wrapper.setSqlSelect("user_id");
        List<UserGroup> userGroupList = super.selectList(wrapper);
        HashSet<String> set = new HashSet<>();
        userGroupList.forEach(userGroup -> set.add(userGroup.getUserId()));
        return set;
    }

}

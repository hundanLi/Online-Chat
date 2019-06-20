package club.lzlbog.chatting.service;

import club.lzlbog.chatting.entity.Authority;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2019-03-17
 */
@Transactional(rollbackFor = Exception.class)
public interface AuthorityService extends IService<Authority> {

}

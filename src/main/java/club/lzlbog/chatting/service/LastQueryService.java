package club.lzlbog.chatting.service;

import club.lzlbog.chatting.entity.LastQuery;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2019-04-26
 */
@Transactional(rollbackFor = Exception.class)
public interface LastQueryService extends IService<LastQuery> {

}

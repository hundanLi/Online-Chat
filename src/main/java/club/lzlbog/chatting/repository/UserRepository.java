package club.lzlbog.chatting.repository;

import club.lzlbog.chatting.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-27 01:15
 **/
public interface UserRepository extends CrudRepository<User, String> {

}

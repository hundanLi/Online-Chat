package club.lzlbog.chatting.repository;

import club.lzlbog.chatting.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired UserRepository repository;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("li");
        repository.save(user);
        long count = repository.count();
        System.out.println(count);
        Optional<User> optional = repository.findById(user.getUsername());
        System.out.println(optional.get());
//        repository.deleteAll();
        System.out.println(repository.count());

    }
}
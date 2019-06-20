package club.lzlbog.chatting.service;

import club.lzlbog.chatting.dto.RegisterDTO;
import club.lzlbog.chatting.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Test
    public void register() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("alibaba");
        dto.setPassword("1234567");
        dto.setRepeatPassword("1234567");
        dto.setEmail("alibaba@qq.com");
        User user = userService.register(dto);
        System.out.println(user);

    }
}
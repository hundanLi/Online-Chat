package club.lzlbog.chatting.mapper;

import club.lzlbog.chatting.entity.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void selectByRoleId() {
        Role role = roleMapper.selectByRoleId(1);
        role.getAuthorities().forEach(System.out::println);
    }
}
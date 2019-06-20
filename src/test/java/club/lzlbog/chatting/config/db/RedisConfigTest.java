package club.lzlbog.chatting.config.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testConnection() {
        Set<String> keys = redisTemplate.keys("*");
        assert keys != null;
        keys.forEach(System.out::println);

    }

    @Test
    public void testListOps() {
        ListOperations<String, Object> opsForList = redisTemplate.opsForList();
        opsForList.leftPush("list", 1000);
        Object o = opsForList.leftPop("list");
        assert o != null;
        assert o.equals(1000);
    }

    @Test
    public void testSetOps() {
        SetOperations<String, Object> opsForSet = redisTemplate.opsForSet();
        opsForSet.add("set", 1000, 2000, 3000);
        Set<Object> members = opsForSet.members("set");
        assert members != null;
        members.forEach(System.out::println);
    }

    @Test
    public void testHashOps() {
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put("hash1", 1, 1);
        opsForHash.put("hash1", 2, 2);
        opsForHash.put("hash1", 3, 3);

        opsForHash.put("hash2", 4, 4);
        opsForHash.put("hash2", 5, 5);

        Object o = opsForHash.get("hash1", 1);
        assert o != null;
        assert o.equals(1);
        Object o1 = opsForHash.get("hash2", 5);
        assert o1 != null;
        assert o1.equals(5);

    }

    @Test
    public void testStringRedisTemplate() {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        opsForHash.put("string", "1", "1000");
        String s = opsForHash.get("string", "1");
        assert s != null;
        assert s.equals("1000");
    }

    @Test
    public void testFlushAll() {

    }
}
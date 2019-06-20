package club.lzlbog.chatting.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 16:01
 **/
public class Encrypto {
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static String encrypto(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }


}

package club.lzlbog.chatting.session;

import club.lzlbog.chatting.entity.User;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-28 13:52
 **/
@Data
@ToString
public class SessionDetail implements Serializable {
    private String location;
    private String accessType;
    private User userInfo;
}

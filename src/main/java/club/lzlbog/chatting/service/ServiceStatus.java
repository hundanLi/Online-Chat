package club.lzlbog.chatting.service;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-24 17:15
 **/
public interface ServiceStatus {
    int SUCCESS = 200;
    int ERROR = 500;
    int EXISTENCE = 400;
    int NOT_FOUND = 404;
    int FORBIDDEN = 403;
}

package club.lzlbog.chatting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-23 00:13
 **/
@Data
@ToString
@EqualsAndHashCode
public class GroupMessageDTO {

    @NotBlank
    private String content;

    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字符")
    private String sender;

    @NotBlank
    private String groupId;

}

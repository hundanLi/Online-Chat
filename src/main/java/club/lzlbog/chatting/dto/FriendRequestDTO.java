package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-26 15:52
 **/
@ApiModel(description = "添加好友请求的数据传输对象")
@Data
public class FriendRequestDTO {

    @ApiModelProperty(value = "发起好友请求的用户名", required = true)
    @Size(min = 2, max = 16, message = "2-16位字符")
    @NotBlank
    private String requester;

    @ApiModelProperty(value = "接受好友请求的用户名", required = true)
    @Size(min = 2, max = 16, message = "2-16位字符")
    @NotBlank
    private String requested;

}

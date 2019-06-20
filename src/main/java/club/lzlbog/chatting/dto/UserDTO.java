package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-26 12:49
 **/
@ApiModel(description = "用户信息")
@Data
public class UserDTO {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像url")
    private String avatarUrl;

    @ApiModelProperty("账号创建时间")
    private LocalDateTime createdTime;
}

package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-31 13:01
 **/
@ApiModel(description = "接收验证码的邮箱和验证码类型的数据对象")
@Data
public class ValidationCodeDTO {
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确！")
    private String email;
    @ApiModelProperty("验证码类型：{1: 用于注册; 2: 用于修改密码}")
    @NotBlank(message = "验证码类型不能为空！")
    @Size(min = 1,max = 1)
    private String sendFor;
}

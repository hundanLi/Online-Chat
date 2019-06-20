package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 15:39
 **/
@Data
@ApiModel(description = "用于注册的用户信息数据对象")
public class RegisterDTO {
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮件格式不正确！")
    private String email;
    @ApiModelProperty(value = "验证码")
    @NotBlank
    @Size(min = 6, max = 6, message = "验证码不正确！")
    private String validationCode;
    @ApiModelProperty("用户名")
    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字母、汉字或数字")
    @Pattern(regexp = "[^.#(\\[$]", message = "用户名不能包含 .#([$ 等特殊字符！")
    private String username;
    @ApiModelProperty(value = "密码")
    @NotBlank
    @Size(min = 8, max = 16, message = "8-16位字符")
    private String password;
    @ApiModelProperty(value = "再次输入密码")
    @NotBlank
    @Size(min = 8, max = 16, message = "8-16位字符")
    private String repeatPassword;

}


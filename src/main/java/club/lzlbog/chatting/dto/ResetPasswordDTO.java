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
 * @date 2019-03-31 15:27
 **/
@ApiModel(description = "用于修改密码的数据对象")
@Data
public class ResetPasswordDTO {
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮件格式不正确！")
    private String email;

    @ApiModelProperty(value = "验证码")
    @NotBlank
    @Size(min = 6, max = 6, message = "验证码不正确！")
    private String validationCode;

    @ApiModelProperty(value = "新的密码")
    @NotBlank
    @Size(min = 8, max = 16, message = "8-16位字符")
    private String newPassword;

    @ApiModelProperty("再次输入新的密码")
    @NotBlank
    @Size(min = 8, max = 16, message = "8-16位字符")
    private String repeatNewPassword;

}

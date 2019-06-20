package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-24 18:10
 **/
@ApiModel(description = "消息数据传输对象")
@Data
@EqualsAndHashCode
@ToString
public class MessageDTO {
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank
    private String content;

    @ApiModelProperty(value = "发送者用户名", required = true)
    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字符")
    private String sender;

    @ApiModelProperty(value = "接受者用户名", required = true)
    @NotBlank
    @Size(min = 2, max = 16, message = "2-16位字符")
    private String receiver;
}

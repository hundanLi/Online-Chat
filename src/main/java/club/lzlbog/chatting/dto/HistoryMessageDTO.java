package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-01 00:15
 **/
@ApiModel(description = "获取历史消息的请求信息数据传输对象")
@Data
public class HistoryMessageDTO {
    @ApiModelProperty(value = "好友用户名")
    @NotBlank
    @Size(min = 2, max = 16, message = "用户名为2~16位字符！")
    private String friend;
    @ApiModelProperty(value = "分页获取的页码")
    @NotNull
    private Integer pageNum;
}

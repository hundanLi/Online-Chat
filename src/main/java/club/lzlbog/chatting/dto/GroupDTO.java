package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author li
 * @version 1.0
 * @date 2019-04-26 14:21
 **/
@ApiModel(description = "群组信息")
@Data
public class GroupDTO {

    @ApiModelProperty(value = "群组名称")
    @NotBlank
    @Size(min = 4, max = 16, message = "群名称长度应为4~16个字符")
    private String groupName;

}

package club.lzlbog.chatting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-24 17:39
 **/
@ApiModel(description = "群组成员数据传输对象")
@Data
public class GroupMemberDTO {

    @ApiModelProperty(value = "群组id", required = true)
    @NotBlank
    private String groupId;

    @ApiModelProperty(value = "群名称")
    private String groupOwner;

    @ApiModelProperty(value = "新增成员的id列表", required = true)
    @NotEmpty
    private List<String> members;
}

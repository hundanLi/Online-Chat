package club.lzlbog.chatting.controller;


import club.lzlbog.chatting.dto.GroupDTO;
import club.lzlbog.chatting.dto.GroupMemberDTO;
import club.lzlbog.chatting.entity.Group;
import club.lzlbog.chatting.service.GroupService;
import club.lzlbog.chatting.util.Validators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

import static club.lzlbog.chatting.util.ResponseUtils.getResultMap;

/**
 * <p>
 *  群组前端控制器
 * </p>
 *
 * @author li
 * @date 2019-03-24
 */
@Api(tags = "群组管理")
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @ApiOperation(
            value = "创建群组",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public Object createGroup(@ApiIgnore Principal principal,
                              @Valid @RequestBody GroupDTO groupDTO,
                              @ApiIgnore Errors errors) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());

        //调用Service层服务
        int status = groupService.createGroup(principal.getName(), group);
        Map<String, Object> resultMap = getResultMap(status);
        resultMap.put("group", group);
        return resultMap;
    }


    @ApiOperation(
            value = "删除群组",
            httpMethod = "DELETE",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{groupId}")
    public Object deleteGroup(@ApiIgnore Principal principal,
                              @PathVariable("groupId") String groupId) {
        int status = groupService.deleteGroup(principal.getName(), groupId);
        return getResultMap(status);
    }

    @ApiOperation(
            value = "添加群成员",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addMembers")
    public Object addMembers(@ApiIgnore Principal principal,
                             @RequestBody @Valid GroupMemberDTO groupMemberDTO,
                             @ApiIgnore Errors errors) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        groupMemberDTO.setGroupOwner(principal.getName());
        int status = groupService.addMembers(groupMemberDTO);
        return getResultMap(status);
    }

    @ApiOperation(
            value = "删除群成员",
            httpMethod = "DELETE",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/removeMembers")
    public Object removeMembers(@ApiIgnore Principal principal,
                                @RequestBody @Valid GroupMemberDTO groupMemberDTO,
                                @ApiIgnore Errors errors) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        groupMemberDTO.setGroupOwner(principal.getName());
        int status = groupService.removeMembers(groupMemberDTO);
        return getResultMap(status);
    }

    @ApiOperation(
            value = "获取群成员列表",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getMembers/{groupId}")
    public Object getMembers(@ApiIgnore Principal principal,
                             @PathVariable("groupId") String groupId) {
        return groupService.getMembers(principal.getName(), groupId);
    }

}


package club.lzlbog.chatting.controller;


import club.lzlbog.chatting.dto.FriendRequestDTO;
import club.lzlbog.chatting.entity.FriendRequest;
import club.lzlbog.chatting.service.FriendRequestService;
import club.lzlbog.chatting.util.ResponseUtils;
import club.lzlbog.chatting.util.Validators;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2019-04-26
 */
@Api(tags = "好友申请接口")
@RestController
@RequestMapping("/friendRequest")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @ApiOperation(
            value = "好友申请",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated() && #friendRequestDTO.requester == #request.userPrincipal.name")
    @PostMapping("/request")
    public Map<String, Object> friendRequest(@Validated @RequestBody FriendRequestDTO friendRequestDTO,
                                @ApiIgnore Errors errors,
                                @ApiIgnore HttpServletRequest request) {
        Map<String, Object> validateMap = Validators.validate(errors);
        if (validateMap != null) {
            return validateMap;
        }
        int status = friendRequestService.addRequest(friendRequestDTO);
        return ResponseUtils.getResultMap(status);
    }


    @ApiOperation(
            value = "查看好友申请",
            httpMethod = "GET",
            authorizations = @Authorization("登录")
    )
    @ApiParam(name = "status", value = "好友申请的状态（0：未处理；1：已接受；2：已拒绝；-1：任意状态")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/query/{status}")
    public List<FriendRequest> queryRequest(@ApiIgnore HttpServletRequest request,
                                            @PathVariable("status") int status) {
        String username = request.getUserPrincipal().getName();
        return friendRequestService.getRequest(username, status);
    }

    @ApiOperation(
            value = "接受好友申请",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/accept/{requestId}")
    public Map<String, Object> acceptRequest(@ApiIgnore HttpServletRequest request,
                                @PathVariable("requestId") int requestId) {
        String requested = request.getUserPrincipal().getName();
        int status = friendRequestService.acceptRequest(requested, requestId);
        return ResponseUtils.getResultMap(status);
    }

    @ApiOperation(
            value = "拒绝好友申请",
            httpMethod = "POST",
            authorizations = @Authorization("登录")
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reject/{requestId}")
    public Map<String, Object> rejectRequest(@ApiIgnore HttpServletRequest request,
                                @PathVariable("requestId") int requestId) {
        String requested = request.getUserPrincipal().getName();
        int status = friendRequestService.rejectRequest(requested, requestId);
        return ResponseUtils.getResultMap(status);
    }
}


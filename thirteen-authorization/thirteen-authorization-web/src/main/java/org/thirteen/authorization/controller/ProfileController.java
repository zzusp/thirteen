package org.thirteen.authorization.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.service.ProfileService;
import org.thirteen.authorization.web.ResponseResult;

/**
 * @author Aaron.Sun
 * @description 个人中心接口
 * @date Created in 13:30 2019/4/4
 * @modified by
 */
@Api(tags = {"个人中心接口"})
@RestController
@RequestMapping(value = "/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @ApiOperation(value = "更新用户头像", notes = "上传更新用户头像", response = ResponseResult.class)
    @PostMapping(value = "/uploadAvatar")
    public ResponseResult<String> uploadAvatar(
        @ApiParam(required = true, value = "用户头像") @RequestParam("avatar") MultipartFile avatar) {
        profileService.uploadAvatar(avatar);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "个人信息修改", notes = "个人信息修改", response = ResponseResult.class)
    @PostMapping(value = "/profileSetting")
    public ResponseResult<String> profileSetting(
        @ApiParam(required = true, value = "个人信息") @RequestBody SysUserVO model) {
        profileService.profileSetting(model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "密码修改", notes = "密码修改", response = ResponseResult.class)
    @PostMapping(value = "/passwordEdit")
    public ResponseResult<String> passwordEdit(
        @ApiParam(required = true, value = "旧密码") @RequestParam("oldPassword") String oldPassword,
        @ApiParam(required = true, value = "新密码") @RequestParam("newPassword") String newPassword,
        @ApiParam(required = true, value = "密码确认") @RequestParam("confirm") String confirm) {
        profileService.passwordEdit(oldPassword, newPassword, confirm);
        return ResponseResult.ok();
    }

}

package org.thirteen.datamation.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.service.DmAuthUserService;
import org.thirteen.datamation.web.ResponseResult;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化配置平台-用户模块接口
 * @date Created in 17:11 2021/2/13
 * @modified By
 */
@Api(tags = "数据化配置平台-用户模块接口")
@RestController
@RequestMapping(value = "/dmAuthUser")
public class DmAuthUserController {

    private final DmAuthUserService dmAuthUserService;

    public DmAuthUserController(DmAuthUserService dmAuthUserService) {
        this.dmAuthUserService = dmAuthUserService;
    }

    @ApiOperation(value = "新增", notes = "新增", response = ResponseResult.class)
    @PostMapping(value = "/insert")
    public ResponseResult<String> insert(
            @ApiParam(required = true, value = "新增参数对象") @RequestBody DmAuthInsert dmAuthInsert) {
        this.dmAuthUserService.insert(dmAuthInsert);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "更新用户头像", notes = "上传更新用户头像", response = ResponseResult.class)
    @PostMapping(value = "/uploadAvatar")
    public ResponseResult<String> uploadAvatar(
            @ApiParam(required = true, value = "用户头像") @RequestParam("avatar") MultipartFile avatar) {
        this.dmAuthUserService.uploadAvatar(avatar);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "个人信息修改", notes = "个人信息修改", response = ResponseResult.class)
    @PostMapping(value = "/profileSetting")
    public ResponseResult<String> profileSetting(
            @ApiParam(required = true, value = "个人信息") @RequestBody Map<String, Object> model) {
        this.dmAuthUserService.profileSetting(model);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "密码修改", notes = "密码修改", response = ResponseResult.class)
    @PostMapping(value = "/passwordEdit")
    public ResponseResult<String> passwordEdit(
            @ApiParam(required = true, value = "旧密码") @RequestParam("oldPassword") String oldPassword,
            @ApiParam(required = true, value = "新密码") @RequestParam("newPassword") String newPassword,
            @ApiParam(required = true, value = "密码确认") @RequestParam("confirm") String confirm) {
        this.dmAuthUserService.passwordEdit(oldPassword, newPassword, confirm);
        return ResponseResult.ok();
    }
}

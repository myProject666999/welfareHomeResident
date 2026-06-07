package com.welfare.controller;

import com.welfare.common.Result;
import com.welfare.entity.SysUser;
import com.welfare.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestParam String username, @RequestParam String password) {
        SysUser user = sysUserService.login(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }
}

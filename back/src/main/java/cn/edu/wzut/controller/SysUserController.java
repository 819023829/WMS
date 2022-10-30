package cn.edu.wzut.controller;


import cn.edu.wzut.dto.PassDto;
import cn.edu.wzut.mbp.entity.SysUser;
import cn.edu.wzut.mbp.entity.SysUserRole;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zcz
 * @since 2022-07-02
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController{
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/info/{id}")
    public JsonResult<SysUser> info(@PathVariable(name = "id") Long id){
        SysUser sysUser=sysUserService.getById(id);
        Assert.notNull(sysUser,"找不到该用户");
        sysUser.setSysRoles(sysRoleService.getByUserId(id));
    return new JsonResult<>(sysUser);
    }

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/list")
    public JsonResult<Page<SysUser>> list(String name){
        Page<SysUser> pageData=sysUserService.page(getPage(),new QueryWrapper<SysUser>().like(StrUtil.isNotBlank(name),"username",name));
        pageData.getRecords().forEach(item -> {
            item.setSysRoles(sysRoleService.getByUserId(item.getId()));
        });
        return new JsonResult<>(pageData);
    }
    @PreAuthorize("hasAuthority('sys:user:save')")
    @PostMapping("/save")
    public JsonResult<Object> save(@Validated @RequestBody SysUser sysUser){
        sysUser.setPassword(bCryptPasswordEncoder.encode("888888"));
        sysUser.setAvatar("https://img2.woyaogexing.com/2022/07/04/d0ea6080a5db0695!400x400.jpg");
        sysUser.setCreated(LocalDateTime.now());
        sysUserService.save(sysUser);
        return new JsonResult<>();
    }
    @Transactional
    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping("/update")
    public JsonResult<Object> update(@Validated @RequestBody SysUser sysUser){
        sysUser.setUpdated(LocalDateTime.now());
        sysUserService.updateById(sysUser);
        return new JsonResult<>();
    }
    @Transactional
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping("/delete")
    public JsonResult<Object> delete(@RequestBody Long[] ids){
        sysUserService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id",Arrays.asList(ids)));
        return new JsonResult<>();
    }

    @PreAuthorize("hasAuthority('sys:user:role')")

    @Transactional
    @PostMapping("/role/{id}")
    public JsonResult<Object> rolePerm(@PathVariable(name = "id") Long id,@RequestBody Long[] roleIds){
        List<SysUserRole> sysUserRoles=new ArrayList<>();
        Arrays.stream(roleIds).forEach(r->{
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setRoleId(r);
            sysUserRole.setUserId(id);

            sysUserRoles.add(sysUserRole);
        });
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",id));
        sysUserRoleService.saveBatch(sysUserRoles);
        sysUserService.clearUserAuthorityInfo(sysUserService.getById(id).getUsername());
        return new JsonResult<>();
    }
    @PreAuthorize("hasAuthority('sys:user:repass')")
    @PostMapping("/repass/{id}")
    public JsonResult<Object> repass(@PathVariable(name = "id") Long id){
        SysUser sysUser =sysUserService.getById(id);
        sysUser.setPassword(bCryptPasswordEncoder.encode("888888"));
        sysUser.setUpdated(LocalDateTime.now());
        sysUserService.updateById(sysUser);
        return new JsonResult<>();
    }


    @PostMapping("/updatePass")
    public JsonResult<Object> updatePass(@Validated @RequestBody PassDto passDto,Principal principal){
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        boolean matches = bCryptPasswordEncoder.matches(passDto.getOldPass(), sysUser.getPassword());
        if (!matches) {
            return new JsonResult<>("旧密码错误");
        }

        sysUser.setPassword(bCryptPasswordEncoder.encode(passDto.getNewPass()));
        sysUser.setUpdated(LocalDateTime.now());
        sysUserService.updateById(sysUser);
        return new JsonResult<>();

    }
}

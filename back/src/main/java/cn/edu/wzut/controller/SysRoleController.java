package cn.edu.wzut.controller;

import cn.edu.wzut.mbp.entity.SysRole;
import cn.edu.wzut.mbp.entity.SysRoleMenu;
import cn.edu.wzut.mbp.entity.SysUserRole;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.javafx.image.impl.BaseIntToIntConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zcz
 * @since 2022-07-02
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
    /**
     * 根据id查找
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public JsonResult<SysRole> info(@PathVariable(name = "id")Long id){
        SysRole sysRole= sysRoleService.getById(id);

        //获取角色相关的菜单id，用于分配权限使用
        List<SysRoleMenu> roleMenus= sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id",id));
        List<Long> menuIds= roleMenus.stream().map(p->p.getMenuId()).collect(Collectors.toList());
       sysRole.setMenuIds(menuIds);
        return new JsonResult<>(sysRole);
    }
    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public JsonResult<Page<SysRole>> list(String name){

        Page<SysRole> pageData= sysRoleService.page(getPage(),new QueryWrapper<SysRole>().like(StrUtil.isNotBlank(name),"name",name));
        return new JsonResult<>(pageData);
    }
    @PreAuthorize("hasAuthority('sys:role:save')")
    @PostMapping("/save")
    public JsonResult<SysRole> save(@Validated @RequestBody SysRole sysRole){
        sysRole.setCreated(LocalDateTime.now());

        sysRoleService.save(sysRole);

        return new JsonResult<>(sysRole);
    }
    @PreAuthorize("hasAuthority('sys:role:update')")
    @PostMapping("/update")
    public JsonResult<SysRole> update(@Validated @RequestBody SysRole sysRole){
        sysRole.setCreated(LocalDateTime.now());
        sysRoleService.updateById(sysRole);
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return new JsonResult<>(sysRole);
    }
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @PostMapping("/delete")
    @Transactional
    public JsonResult<Object> delete(@RequestBody Long[] ids){
        sysRoleService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id",ids));
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id",ids));
        Arrays.stream(ids).forEach(id-> sysUserService.clearUserAuthorityInfoByRoleId(id));
        return new JsonResult<>();
    }
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @PostMapping("/perm/{roleId}")
    @Transactional
    public JsonResult<Object> perm(@PathVariable(name = "roleId")Long roleId,@RequestBody Long[] menuIds){

        List<SysRoleMenu> list=new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId-> {
           SysRoleMenu sysRoleMenu=new SysRoleMenu();
           sysRoleMenu.setRoleId(roleId);
           sysRoleMenu.setMenuId(menuId);

           list.add(sysRoleMenu);
        });
        //先删除在添加，避免重复
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        sysRoleMenuService.saveBatch(list);
//        删除原来的权限缓存
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);
        return new JsonResult<>();
    }

}

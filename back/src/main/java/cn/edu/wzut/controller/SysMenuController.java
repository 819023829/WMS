package cn.edu.wzut.controller;

import cn.edu.wzut.dto.SysMenuDto;
import cn.edu.wzut.mbp.entity.SysMenu;
import cn.edu.wzut.mbp.entity.SysRoleMenu;
import cn.edu.wzut.mbp.entity.SysUser;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController{


    /**
     *获取当前用户的菜单和权限信息
     * @param principal
     * @return
     */
    @GetMapping("/nav")
    public JsonResult<Object> nav(Principal principal){
        SysUser sysUser= sysUserService.getByUsername(principal.getName());

        //获取权限信息
        String authority= sysUserService.getUerAuthorityInfo(sysUser.getId());
        String[] authorityArray=StringUtils.tokenizeToStringArray(authority,",");

        //获取导航栏信息
        List<SysMenuDto> navs= sysMenuService.getCurrentUserNav();
        return new JsonResult<>(MapUtil.builder()
                .put("authoriys",authorityArray)
                .put("nav",navs)
                .map()
        );
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
   public JsonResult<SysMenu> info(@PathVariable(name = "id") Long id){
        return new JsonResult<>(sysMenuService.getById(id));
    }
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public JsonResult<List<SysMenu>> list(){
        List<SysMenu> lists= sysMenuService.allTree();
        return new JsonResult<>(lists);
    }
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public JsonResult<SysMenu> save (@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setCreated(LocalDateTime.now());
        sysMenuService.save(sysMenu);
        return new JsonResult<>(sysMenu);


    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public JsonResult<SysMenu> delete(@PathVariable(name = "id") Long id){
//        判断是否有子集，防止误删
         long count= sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id",id));
       if(count>0){
           return new JsonResult<>(400,"请先删除子菜单");
       }
       //清除权限缓存
       sysUserService.clearUserAuthorityInfoByMenuId(id);
       sysMenuService.removeById(id);
       //删除中间关联表
       sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id",id));
       return new JsonResult<>();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public JsonResult<SysMenu> update(@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setUpdated(LocalDateTime.now());
        sysMenuService.updateById(sysMenu);
        //清除权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
        return new JsonResult<>(sysMenu);
    }

}

package cn.edu.wzut.mbp.service.impl;

import cn.edu.wzut.mbp.entity.SysMenu;
import cn.edu.wzut.mbp.entity.SysRole;
import cn.edu.wzut.mbp.entity.SysUser;
import cn.edu.wzut.mbp.mapper.SysUserMapper;
import cn.edu.wzut.mbp.service.ISysMenuService;
import cn.edu.wzut.mbp.service.ISysRoleService;
import cn.edu.wzut.mbp.service.ISysUserService;
import cn.edu.wzut.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    ISysRoleService roleService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ISysMenuService sysMenuService;
    @Autowired
    RedisUtil redisUtil;
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    //获取权限信息列表
    @Override
    public String getUerAuthorityInfo(Long userId) {
        SysUser sysUser=sysUserMapper.selectById(userId);
        String authority="";

        if(redisUtil.hasKey("GrantedAuthority:"+ sysUser.getUsername())){
            authority=(String)redisUtil.get("GrantedAuthority:"+ sysUser.getUsername());

        }else{
//        获取角色信息

        List<SysRole> roles= roleService.list(new QueryWrapper<SysRole>().inSql(
                "id","select role_id from sys_user_role where user_id="+userId));
        if(roles.size()>0) {
            //流的方式获取
            String roleCode = roles.stream().map(r ->"ROLE_"+r.getCode()).collect(Collectors.joining(","));
            authority=roleCode.concat(",");
        }

        // 获取菜单操作权限

        List<Long> menuIds= sysUserMapper.getMenuIdsByUserId(userId);
        if(menuIds.size()>0){
            List<SysMenu> menus=sysMenuService.listByIds(menuIds);
            String menuPerms= menus.stream().map(m->m.getPerms()).collect(Collectors.joining(","));
            authority=authority.concat(menuPerms);
        }
            //加入缓存
        redisUtil.set("GrantedAuthority:"+ sysUser.getUsername(),authority,3600); }

        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:"+username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
    List<SysUser> sysUsers= this.list(new QueryWrapper<SysUser>()
            .inSql("id","select user_id from sys_user_role where role_id="+roleId));
    sysUsers.forEach(u->this.clearUserAuthorityInfo(u.getUsername()));

    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
       List<SysUser> sysUsers= sysUserMapper.listByMenuId(menuId);
        sysUsers.forEach(u->this.clearUserAuthorityInfo(u.getUsername()));
    }

    @Override
    public List<Long> getMenuIdsByUserId(Long userId) {
        return sysUserMapper.getMenuIdsByUserId(userId);
    }




}

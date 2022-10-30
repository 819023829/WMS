package cn.edu.wzut.mbp.service.impl;

import cn.edu.wzut.dto.SysMenuDto;
import cn.edu.wzut.mbp.entity.SysMenu;
import cn.edu.wzut.mbp.entity.SysUser;
import cn.edu.wzut.mbp.mapper.SysMenuMapper;
import cn.edu.wzut.mbp.service.ISysMenuService;
import cn.edu.wzut.mbp.service.ISysUserService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Lazy
    @Autowired
    ISysUserService sysUserService;


    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(username);
        List<Long> menuIds = sysUserService.getMenuIdsByUserId(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds); //获取当前用户的menus
        //转树状结构
        List<SysMenu> menuTree = builderTreeMenu(menus);
        //list转化为我们需要的dto格式
        return convert(menuTree);

    }

    @Override
    public List<SysMenu> allTree() {
        List<SysMenu> listMenu= this.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));
//        返回树状结构
        return builderTreeMenu(listMenu);
    }

    //转化为tree方法
    private List<SysMenu> builderTreeMenu(List<SysMenu> menus) {
        List<SysMenu> menuTree=new ArrayList<>();
        //找到自己的所有子类
        for(SysMenu menu:menus){
            for(SysMenu e:menus){
                if(menu.getId() == e.getParentId()){
                    menu.getChildren().add(e);
                }
            }
            if(menu.getParentId()==0L){
            menuTree.add(menu);
            }
        }
        //提取父节点

        return menuTree;
    }

    //转化为需要的dto格式方法
    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos=new ArrayList<>();
        menuTree.forEach(m->{
            SysMenuDto dto=new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setIconClass(m.getIcon());
            dto.setComponent(m.getComponent());
            dto.setRoute(m.getPath());
            if(m.getChildren().size()>0) {//子节点用递归调用来进行转换
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
            return menuDtos;
    }
}

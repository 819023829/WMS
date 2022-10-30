package cn.edu.wzut.mbp.service.impl;

import cn.edu.wzut.mbp.entity.SysRole;
import cn.edu.wzut.mbp.entity.SysUserRole;
import cn.edu.wzut.mbp.mapper.SysRoleMapper;
import cn.edu.wzut.mbp.service.ISysRoleService;
import cn.edu.wzut.mbp.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    ISysUserRoleService sysUserRoleService;


    @Override
    public List<SysRole> getByUserId(Long userId) {
        List<SysUserRole> sysUserRole=sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        List<SysRole> sysRole=new ArrayList<>();
        sysUserRole.forEach(item->{
            sysRole.add(this.getById(item.getRoleId()));
        });
        return sysRole;
    }
}

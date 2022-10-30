package cn.edu.wzut.mbp.service;

import cn.edu.wzut.mbp.entity.SysMenu;
import cn.edu.wzut.mbp.entity.SysUser;
import cn.edu.wzut.mbp.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    String getUerAuthorityInfo(Long userId);

    void clearUserAuthorityInfo(String username); //删除缓存信息
    void clearUserAuthorityInfoByRoleId(Long roleId); //删除缓存信息
    void clearUserAuthorityInfoByMenuId(Long menuId); //删除缓存信息
    List<Long> getMenuIdsByUserId(Long userId);


}

package cn.edu.wzut.security;

import cn.edu.wzut.mbp.entity.SysUser;
import cn.edu.wzut.mbp.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zcz
 * @since 2022/7/4 10:32
 * 实现数据库查询用户名、密码等信息，来进行登录
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    ISysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser= sysUserService.getByUsername(username);

        if(sysUser==null){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(sysUser.getId(),sysUser.getUsername(),sysUser.getPassword(),getUserAuthority(sysUser.getId()));
    }
//        获取用户权限信息（角色，菜单权限）
    public List<GrantedAuthority> getUserAuthority(Long userId){
        String authority= sysUserService.getUerAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}

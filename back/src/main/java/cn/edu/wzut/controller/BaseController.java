package cn.edu.wzut.controller;

import cn.edu.wzut.mbp.service.*;
import cn.edu.wzut.utils.RedisUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author zcz
 * @since 2022/7/3 10:28
 */
@RestController
public class BaseController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ISysUserService sysUserService;
    @Autowired
    ISysMenuService sysMenuService;
    @Autowired
    ISysRoleService sysRoleService;
    @Autowired
    ISysUserRoleService sysUserRoleService;
    @Autowired
    ISysRoleMenuService sysRoleMenuService;

    /**
     * 获取页码
     * @return
     */
    public Page getPage(){
    int current= ServletRequestUtils.getIntParameter(req,"current",1);
    int size= ServletRequestUtils.getIntParameter(req,"size",10);
    return new Page(current,size);
    }
}

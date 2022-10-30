package cn.edu.wzut.mbp.service;

import cn.edu.wzut.dto.SysMenuDto;
import cn.edu.wzut.mbp.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentUserNav();

    List<SysMenu> allTree();
}

package cn.edu.wzut.mbp.service;

import cn.edu.wzut.mbp.entity.SysRole;
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
public interface ISysRoleService extends IService<SysRole> {




    List<SysRole> getByUserId(Long userId);
}

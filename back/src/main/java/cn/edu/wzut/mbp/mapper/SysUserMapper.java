package cn.edu.wzut.mbp.mapper;

import cn.edu.wzut.mbp.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select distinct rm.menu_id from sys_user_role ur left join sys_role_menu rm on ur.role_id = rm.role_id  where ur.user_id =#{userId}")
    List<Long> getMenuIdsByUserId(Long userId);
@Select("SELECT DISTINCT " +
        "su.*" +
        "FROM " +
        "sys_user_role ur " +
        "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
        "LEFT JOIN sys_user su ON ur.user_id=su.id " +
        "WHERE " +
        "rm.menu_id = #{menuId}")
    List<SysUser> listByMenuId(Long menuId);
}

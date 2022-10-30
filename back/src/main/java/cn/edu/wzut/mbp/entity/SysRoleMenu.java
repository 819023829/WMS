package cn.edu.wzut.mbp.entity;

import cn.edu.wzut.mbp.entity.EntityBase;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author zcz
 * @since 2022-07-04
 */
@Getter
@Setter
@TableName("sys_role_menu")
public class SysRoleMenu extends EntityBase {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long roleId;

    private Long menuId;


}

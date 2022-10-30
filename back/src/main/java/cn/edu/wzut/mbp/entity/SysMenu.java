package cn.edu.wzut.mbp.entity;

import cn.edu.wzut.mbp.entity.EntityBase;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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
@TableName("sys_menu")
public class SysMenu extends EntityBase {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @NotBlank(message = "权限不能为空")
    private String perms;

    private String component;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */

    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */

    private Integer orderNum;

    private LocalDateTime created;

    private LocalDateTime updated;

    private Integer statu;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

}

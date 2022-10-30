package cn.edu.wzut.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zcz
 * @since 2022/7/5 0:23
 */

//         name: "SysUser",
//         title: "用户管理",
//         iconClass: "el-icon-s-custom",
//         route: "/sys/user",
//         component: "sys/User",
//         children:[]
@Data
public class SysMenuDto implements Serializable {
            private Long id;
            private String name;
            private String title;
            private String iconClass;
            private String route;
            private String component;
            private List<SysMenuDto> children=new ArrayList<>();
}

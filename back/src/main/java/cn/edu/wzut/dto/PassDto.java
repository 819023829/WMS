package cn.edu.wzut.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zcz
 * @since 2022/7/6 10:11
 */
@Data
public class PassDto implements Serializable {

    @NotBlank(message = "旧密码不能为空")
    private String oldPass;
    @NotBlank(message = "新不能为空")
    private String newPass;


}

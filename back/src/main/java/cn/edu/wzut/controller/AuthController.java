package cn.edu.wzut.controller;

import cn.edu.wzut.lang.Const;
import cn.edu.wzut.mbp.entity.SysUser;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * @author zcz
 * @since 2022/7/3 10:26
 */
@RestController
public class AuthController extends BaseController{
    @Autowired
    private Producer producer;
    @GetMapping("/code")
    public JsonResult<Object> captcha() throws IOException {
        String key= UUID.randomUUID().toString();
        String code=producer.createText();


        BufferedImage image=producer.createImage(code);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);

        BASE64Encoder encoder=new BASE64Encoder();
        String str="data:image/jpg;base64,";
        String base64Img=str+encoder.encode(outputStream.toByteArray());

        redisUtil.hset(Const.CAPTCHA_KEY,key,code,120);

        return new JsonResult<>(MapUtil.builder()
                .put("token",key)
                .put("codeImg",base64Img)
                .build());
    }

    /**
     * 获取用户信息接口
     * @param principal
     * @return
     */
    @GetMapping("/sys/userInfo")
    public JsonResult<Object> userInfo(Principal principal){
     SysUser sysUser= sysUserService.getByUsername(principal.getName());
     return new JsonResult<>(MapUtil.builder()
            .put("id",sysUser.getId())
            .put("username",sysUser.getUsername())
             .put("avatar",sysUser.getAvatar())
             .put("created",sysUser.getCreated())
            .map()
     );
}
}

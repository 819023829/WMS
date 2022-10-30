package cn.edu.wzut.security;

import cn.edu.wzut.controller.JsonResult;
import cn.edu.wzut.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zcz
 * @since 2022/7/3 12:19
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler{
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        //生成jwt,并放入请求头中
        String jwt= jwtUtil.createJwt(authentication.getName());
        response.setHeader(jwtUtil.getHeader(),jwt);

        JsonResult<String> result =new JsonResult<>();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}

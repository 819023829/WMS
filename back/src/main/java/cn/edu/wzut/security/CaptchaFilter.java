package cn.edu.wzut.security;

import cn.edu.wzut.controller.JsonResult;
import cn.edu.wzut.exception.CodeException;
import cn.edu.wzut.lang.Const;
import cn.edu.wzut.utils.RedisUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zcz
 * @since 2022/7/3 12:49
 */
@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String url=request.getRequestURI();
    if("/login".equals(url) && request.getMethod().equals("POST")){
        //校验验证码是否正确
        try {
            validate(request,response);
        }catch (CodeException e){
            //失败则交给失败处理器
            loginFailureHandler.onAuthenticationFailure(request,response,e);
        }
    }
        filterChain.doFilter(request,response);
    }
//验证码校验方法
    private void validate(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String code=request.getParameter("code");
        String key=request.getParameter("token");


        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            JsonResult<String> result =new JsonResult<>(400,"验证码错误");
            outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            throw new CodeException("验证码错误");
        }
        //一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}

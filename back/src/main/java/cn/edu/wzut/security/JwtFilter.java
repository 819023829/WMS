package cn.edu.wzut.security;


import cn.edu.wzut.mbp.service.ISysUserService;
import cn.edu.wzut.utils.JwtUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zcz
 * @since 2022/7/3 23:24
 */
public class JwtFilter extends BasicAuthenticationFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    ISysUserService sysUserService;

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt=request.getHeader(jwtUtil.getHeader());
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;

            }
        Claims claims= jwtUtil.getClaimsByBody(jwt);
        if(claims==null){
            throw new JwtException("token异常");
        }
        if(jwtUtil.isTokenExpired(claims)){
            throw new JwtException("token已过期");
        }

        String username=claims.getSubject();
       Long userId= sysUserService.getByUsername(username).getId();
        //获取用户信息，权限等封装到UsernamePasswordAuthenticationToken中，后续可以使用SecurityContextHolder获取
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(username,null,userDetailService.getUserAuthority(userId));

        SecurityContextHolder.getContext().setAuthentication(token);//
        chain.doFilter(request,response);
    }
}

package cn.edu.wzut.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author zcz
 * @since 2022/7/5 13:27
 */
public class CodeException extends AuthenticationException {
    public CodeException(String msg) {
        super(msg);
    }
}

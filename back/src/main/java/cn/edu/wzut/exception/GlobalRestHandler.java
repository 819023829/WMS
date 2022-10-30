package cn.edu.wzut.exception;

import cn.edu.wzut.controller.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zcz
 * @since 2022/7/2 20:43
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestHandler {

    // 实体校验异常捕获
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public JsonResult<String> handler(MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();

        log.error("实体校验异常：{}", objectError.getDefaultMessage());
        return new JsonResult<>(400,objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public JsonResult<String> handler(IllegalArgumentException e) {
        e.printStackTrace();
        log.error("Assert异常：{}", e.getMessage());
        return new JsonResult<>(400,e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public JsonResult<String> handler(RuntimeException e) {
        e.printStackTrace();
        log.error("运行时异常：{}", e.getMessage());
        return new JsonResult<>(400,e.getMessage());
    }
}

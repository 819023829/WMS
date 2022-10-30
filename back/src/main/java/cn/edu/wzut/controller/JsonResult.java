package cn.edu.wzut.controller;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zcz
 * @since 2022/7/2 20:23
 */




@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult<T> implements Serializable{

    private int code;
    private String msg;

    private T data;

    public JsonResult() {
        this.code = 200;
        this.msg = "操作成功";
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(T data) {
        this.data = data;
        this.code = 200;
        this.msg = "操作成功";
    }

    public JsonResult(T data, String msg) {
        this.data = data;
        this.code = 200;
        this.msg = msg;
    }

    public JsonResult(String msg) {
        this.code = 400;
        this.msg = msg;
    }
}
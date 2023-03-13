package com.wrc.cloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/6 21:39
 */
@Data
@AllArgsConstructor
public class ResponseResult<T> {

    private int code;

    private String message;

    private T data;

    public ResponseResult (T data) {

        this.data = data;

        this.code = 20000;

        this.message = "success";

    }

    public static ResponseResult error(String message){
        return new ResponseResult(500, message, null);
    }


    public static ResponseResult error(int code, String message){
        return new ResponseResult(code, message, null);
    }

    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult(data);
    }

}

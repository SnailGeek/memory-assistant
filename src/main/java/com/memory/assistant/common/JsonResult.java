package com.memory.assistant.common;

import com.memory.assistant.exception.ExceptionResolvable;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonResult<T> {

    private Integer code = 0;
    private String message = "success";
    private T data;

    @JsonIgnore
    @JSONField(serialize = false)
    private Throwable throwable;

    /**
     * 只为序列化，不能使用
     */
    public JsonResult() {
        super();
    }

    /**
     * 构造不对外
     */
    private JsonResult(Integer code, String message, T data, Throwable throwable) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
        this.throwable = throwable;

        //供应商有部分接口吃异常没记录日志，暂时放在返回结果里
        if (this.message == null && throwable != null) {
            this.message = new StringBuilder(throwable.toString()).append("; ").append(throwable.getStackTrace()).toString();
        }
    }

    public JsonResult(String message) {
        super();
        this.message = message;
    }


    /**
     * 自定义成功信息
     */
    public static <T> JsonResult<T> success(String message, T data) {
        return new JsonResult<T>(0, message, data, null);
    }

    /**
     * 默认success成功信息
     */
    public static <T> JsonResult<T> success(T result) {
        return new JsonResult<T>(0, "success", result, null);
    }

    /**
     * 默认的成功信息，data为空
     */
    public static <T> JsonResult<T> success() {
        return new JsonResult<T>(0, "success", null, null);
    }

    /**
     * 无异常信息的时候使用
     */
    public static <T> JsonResult<T> error(ExceptionResolvable exceptionCode) {
        return new JsonResult<T>(exceptionCode.getCode(),
                exceptionCode.getMessage(), null, null);
    }

    /**
     * 500
     */
    private static final Integer EXCEPTION_CODE_500 = 500;
    /**
     * 400
     */
    private static final Integer EXCEPTION_CODE_400 = 400;
    /**
     * 500 Default message
     */
    private static final String EXCEPTION_MESSAGE_500 = "Server Internal Error";
    /**
     * 400 Default message
     */
    private static final String EXCEPTION_MESSAGE_400 = "Bad request";

    /**
     * 无异常信息的时候使用
     */
    public static <T> JsonResult<T> error(Integer code, String message) {
        if (code == null)
            code = EXCEPTION_CODE_500;
        if (message == null)
            message = EXCEPTION_CODE_400.equals(code) ? EXCEPTION_MESSAGE_400 : EXCEPTION_MESSAGE_500;
        return new JsonResult<T>(code, message, null, null);
    }

    public static <T> JsonResult<T> error(Integer code, String message, T data) {
        if (code == null)
            code = EXCEPTION_CODE_500;
        if (message == null)
            message = EXCEPTION_CODE_400.equals(code) ? EXCEPTION_MESSAGE_400 : EXCEPTION_MESSAGE_500;
        return new JsonResult<T>(code, message, data, null);
    }

    /**
     * 默认错误时使用
     */
    public static <T> JsonResult<T> error() {
        return error(EXCEPTION_CODE_500, null);
    }

    /**
     * 仅指定code时使用
     */
    public static <T> JsonResult<T> error(Integer code) {
        return error(code, null);
    }

    /**
     * 仅指定message时使用
     */
    public static <T> JsonResult<T> error(String message) {
        return error(null, message);
    }

    /**
     * 有异常信息的时候使用
     */
    public static <T> JsonResult<T> error(ExceptionResolvable exceptionCode,
                                          Throwable ex) {
        return new JsonResult<T>(exceptionCode.getCode(),
                exceptionCode.getMessage(), null, ex);
    }

    /**
     * 有异常信息的时候
     */
    public static JsonResult<Void> error(Integer code, String message,
                                         Throwable ex) {
        return new JsonResult<Void>(code, message, null, ex);
    }

    /**
     * 只有异常信息的错误
     */
    public static <T> JsonResult<T> error(Exception ex) {
        return new JsonResult<T>(500, ex.getMessage(), null, ex);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
package com.talex.frame.talexframe.wrapper;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <br /> {@link com.talex.frame.talexframe.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 10:19 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ResultData<T> {

    @Getter
    @AllArgsConstructor
    static enum ResultEnum {

        /**操作成功**/
        SUCCESS(100,"OK"),
        /**操作失败**/
        FAILED(999,"FAILED"),
        /**服务限流**/
        RC200(200,"服务开启限流保护,请稍后再试!"),
        /**服务降级**/
        RC201(201,"服务开启降级保护,请稍后再试!"),
        /**热点参数限流**/
        RC202(202,"热点参数限流,请稍后再试!"),
        /**系统规则不满足**/
        RC203(203,"系统规则不满足要求,请稍后再试!"),
        /**授权规则不通过**/
        RC204(204,"授权规则不通过,请稍后再试!"),
        /**access_denied**/
        RC403(403,"无访问权限,请联系管理员授予权限"),
        /**access_denied**/
        RC401(401,"匿名用户访问无权限资源时的异常"),
        /**服务异常**/
        RC500(500,"系统异常，请稍后重试"),

        INVALID_TOKEN(2001,"访问令牌不合法"),
        ACCESS_DENIED(2003,"没有权限访问该资源"),
        CLIENT_AUTHENTICATION_FAILED(1001,"客户端认证失败"),
        USERNAME_OR_PASSWORD_ERROR(1002,"用户名或密码错误"),
        UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式");

        final int code;
        final String msg;

    }

    public ResultData<T> setStatusByEnum(ResultEnum rEnum) {

        this.status = rEnum.getCode();
        this.message = rEnum.getMsg();

        return this;

    }

    /**
     *
     * 结果状态
     *
     */
    private int status;

    /**
     *
     * 结果消息
     *
     */
    private String message;

    /**
     *
     * 数据
     *
     */
    private T data;

    /**
     *
     * 接口耗时
     *
     */
    private long timeStamp;

    public ResultData(ResultEnum rEnum, T data, long timeStamp) {

        this(rEnum.getCode(), rEnum.getMsg(), data, timeStamp);

    }

    public ResultData(ResultEnum rEnum, T data) {

        this(rEnum, data, 0);

    }

    /**
     *
     * 成功返回
     *
     * @param data 数据实例
     * @param <T> 数据类型
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> SUCCESS(T data) {

        return new ResultData<>(ResultEnum.SUCCESS, data);

    }

    /**
     *
     * 成功返回
     *
     * @param data 数据实例
     * @param <T> 数据类型
     * @param timeStamp 执行消耗
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> SUCCESS(T data, long timeStamp) {

        return new ResultData<>(ResultEnum.SUCCESS, data, timeStamp);

    }

    /**
     *
     * 失败返回
     *
     * @param data 数据实例
     * @param <T> 数据类型
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(T data) {

        return new ResultData<>(ResultEnum.FAILED, data);

    }

    /**
     *
     * 失败返回
     *
     * @param data 数据实例
     * @param <T> 数据类型
     * @param timeStamp 执行消耗
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(T data, long timeStamp) {

        return new ResultData<>(ResultEnum.FAILED, data, timeStamp);

    }


    /**
     *
     * 失败返回
     *
     * @param rEnum 结果枚举
     * @param data 数据实例
     * @param <T> 数据类型
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(ResultEnum rEnum, T data) {

        return new ResultData<>(rEnum, data);

    }

    /**
     *
     * 失败返回
     *
     * @param rEnum 结果枚举
     * @param data 数据实例
     * @param <T> 数据类型
     * @param timeStamp 执行消耗
     *
     * @return 返回包装
     */
    public static <T> ResultData<T> FAILED(ResultEnum rEnum, T data, long timeStamp) {

        return new ResultData<>(rEnum, data, timeStamp);

    }

}

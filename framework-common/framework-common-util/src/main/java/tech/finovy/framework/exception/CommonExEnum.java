package tech.finovy.framework.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.finovy.framework.result.IResultCode;

/**
 * 公共枚举
 */
@Getter
@AllArgsConstructor
public enum CommonExEnum implements IResultCode {

    //---HTTP错误------------------------
    BAD_REQUEST(400, "400"),
    FORBIDDEN(403, "403"),
    NOT_FOUND(404, "404"),
    TOO_MANY_REQUEST(429, "429"),

    //---Auth--------------------------------------------------------------------
    EXCEL_ERROR(2509, "easy excel exception");



    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;
}

package tech.finovy.framework.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * 业务代码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    SUCCESS(0, "success"),
    ERR_PARAMS_ERROR(402, "err params error"),
    ERR_ACCESS_DENIED(403, "accessdenied"),
    ERR_TOO_MANY_REQUESTS(429, "too many requests"),
    ERR_SYSTEM_FAILED(500, "system error"),
    ERR_TOKEN_ILLEGAL(501, "err token illegal"),
    ERR_APPSECRT_ILLEGAL(502, "err appsecrt illegal"),
    ERR_SMSERROR_PARAM(503, "err smserror param"),
    ERR_SMSERROR_FAILED(504, "err smserror failed"),
    ERR_USERMODIFY_FAILED(505, "err usermodify failed"),
    ERR_USERVALIDATE_FAILED(506, "err uservalidate failed"),
    ERR_USERHEAD_UPLOADFAILED(507, "err userhead uploadfailed"),
    ERR_USER_DOESNOTEXT(508, "err user doesnotext"),
    ERR_USER_PHONEEXT(509, "err user phoneext"),
    ERR_ADDRESS_DOESNOTEXT(510, "err address doesnotext"),
    ERR_LIKE_EXT(511, "err like ext"),
    ERR_LIKE_NOTEXT(512, "err like notext"),
    ERR_PRODUCT_NOTEXT(513, "err product notext"),
    ERR_ORDER_NOTEXT(514, "err order notext"),

    ERR_CANCEL_ORDER(619, "err cancel order"),
    ERR_COUPON_CODE_RANGE(620, "err coupon code range"),
    ERR_COUPON_CODE_SHOP(621, "err coupon code shop"),
    ERR_COUPON_CODE_TIME(622, "err coupon code time"),
    ERR_PLAN_NOT_EXT(623, "err plan not ext"),
    ERR_COUPON_USED(624, "err coupon used"),
    SHOP_DOMAINS_EXT(625, "shop domains ext"),
    ERR_SHOP_LOGIN(626, "err shop login"),
    ERR_BIND_UACC_EXT(627, "err bind uacc ext"),

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "bad request"),
    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized"),
    CLIENT_UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized"),
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 not found"),
    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "bad request"),
    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "method not allowed"),
    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "unsupported media type"),
    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "forbidden"),
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "internal server error"),
    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "bad request"),
    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "bad request"),
    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "bad request"),
    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "bad request"),

    ;

    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;
}

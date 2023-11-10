package tech.finovy.framework.global.interceptor;

import jakarta.validation.ConstraintViolationException;
import tech.finovy.framework.result.R;
import tech.finovy.framework.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Gloabl handler
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("NullPointerException:",e);
        return R.fail(ResultCode.MSG_NOT_READABLE);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, Exception e){
        log.error("Exception:",e);
        return R.fail(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 参数校验异常
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, ConstraintViolationException ex){
        log.error("ConstraintViolationException:",ex);
        String msg = ex.getMessage();
        String[] msgs = msg.split(": ");
        return R.fail(ResultCode.PARAM_VALID_ERROR,msgs[msgs.length-1]);
    }
    
}

package tech.finovy.framework.exception;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.finovy.framework.result.R;
import tech.finovy.framework.result.ResultCode;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalAppExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalAppExceptionHandler.class);


    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public R businessExceptionHandler(HttpServletRequest req, BusinessException e) {
        LOGGER.warn("app-utils occurs business exception：{}", e.getErrorMsg());
        return R.fail(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        return R.fail(1003, String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()));
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, BindException e) {
        LOGGER.warn("Incomplete parameters::", e);
        return R.fail(1003, e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        return R.fail(1003, e.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, HttpMessageNotReadableException e) {
        LOGGER.warn("invalid parameters:", e);
        return R.fail(1003, e.getMessage());
    }

    @ExceptionHandler(value = JsonParseException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, JsonParseException e) {
        LOGGER.warn("JsonParseException:", e);
        return R.fail("JsonParseException");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        return R.fail(1003, e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, Exception e) {
        LOGGER.warn("unknown exception:{}", JSON.toJSONString(e));
        return R.fail(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理RuntimeException异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public R runtimeExceptionHandler(HttpServletRequest req, RuntimeException e) {
        LOGGER.warn("RuntimeException", e);
        return R.fail(1003, e.getMessage());
    }

}

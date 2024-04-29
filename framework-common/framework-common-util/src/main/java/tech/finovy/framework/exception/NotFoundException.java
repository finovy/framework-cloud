package tech.finovy.framework.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *   404 处理
 *
 * @author derek
 */
@RestController
public class NotFoundException implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public JSONObject error() {
        JSONObject response = new JSONObject();
        response.put("code", 404);
        response.put("msg", "Request api not found");
        return response;
    }

}

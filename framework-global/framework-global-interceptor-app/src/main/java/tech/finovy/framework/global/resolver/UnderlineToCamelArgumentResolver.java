package tech.finovy.framework.global.resolver;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import tech.finovy.framework.global.annotations.ParameterConvert;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UnderlineToCamelArgumentResolver extends AbstractCustomizeResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ParameterConvert.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object org = handleParameterNames(methodParameter, nativeWebRequest);
        valid(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory, org);
        return org;
    }

    /**
     * handle
     *
     * @param parameter  param
     * @param webRequest request
     * @return obj
     */
    private Object handleParameterNames(MethodParameter parameter, NativeWebRequest webRequest) {
        Object obj = BeanUtils.instantiate(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        PropertyDescriptor[] propertyDescriptors = wrapper.getPropertyDescriptors();
        List<String> reqProperty = new ArrayList<>();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            reqProperty.add(propertyDescriptors[i].getName());
        }
        StringBuilder builder = new StringBuilder();
        Iterator<String> paramNames = webRequest.getParameterNames();
        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            String underParamName = underLineToCamel(paramName);
            if (reqProperty.contains(underParamName)) {
                Object o = webRequest.getParameter(paramName);
                wrapper.setPropertyValue(underParamName, o);
            } else {
                // may @Deprecated
                // 重构时前端传了字段，后端未接收，成功阶段便于确认原因
                builder.append(": " + paramName);
            }
        }
        return obj;
    }


}

package tech.finovy.framework.global.resolver;

import com.alibaba.fastjson.JSON;
import tech.finovy.framework.global.annotations.ArrayParamResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class ArrayParamHandlerMethodArgumentResolver extends AbstractCustomizeResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ArrayParamResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object obj = BeanUtils.instantiateClass(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> parameterNames = webRequest.getParameterNames();
        while (parameterNames.hasNext()) {
            // name 从 request 中获取，可能是下划线
            String name = parameterNames.next();
            // 下划线转驼峰
            String camelName = underLineToCamel(name);
            Class<?> propertyType = wrapper.getPropertyType(camelName);
            // 传参在对象中不存在时
            if (Objects.isNull(propertyType)) {
                continue;
            }
            // 属性值(从请求中获取)
            Object o = webRequest.getParameter(name);
            if (Objects.nonNull(o)) {
                // 数组
                if (propertyType.isArray()) {
                    wrapper.setPropertyValue(camelName, value2Array(o));
                } else if (Collection.class.isAssignableFrom(propertyType)) {// 集合
                    wrapper.setPropertyValue(camelName, array2Collection(propertyType, value2Array(o)));
                } else {//其他类型
                    wrapper.setPropertyValue(camelName, o);
                }
            }
        }
        // 参数校验
        valid(parameter, mavContainer, webRequest, binderFactory, obj);
        return obj;
    }

    /**
     * @param o ["a","b","c"]  或  a,b,c 格式
     * @Description: 将字符串值格式化返回为数组
     * @return: java.lang.Object[]
     */
    private Object[] value2Array(Object o) {
        assert o != null;
        // ["a","b","c"] 格式
        if (StringUtils.containsAny(o.toString(), "[", "]")) {
            return JSON.parseArray(o.toString()).toArray();
        } else {
            // a,b,c 格式
            return o.toString().split(",");
        }
    }

    /**
     * @param propertyType
     * @param array
     * @Description: 数组转集合
     * @return: java.util.Collection<java.lang.Object>
     */
    private Collection<Object> array2Collection(Class<?> propertyType, Object[] array) {
        Collection<Object> collection = CollectionFactory.createCollection(propertyType, array.length);
        Collections.addAll(collection, array);
        return collection;
    }
}

package tech.finovy.framework.gloabl.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import tech.finovy.framework.global.Constant;
import tech.finovy.framework.global.TenantContext;
import org.apache.dubbo.rpc.RpcContext;
import tech.finovy.framework.global.TenantIgnoreAspect;


@Slf4j
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
public class MybatisConfigAutoConfiguration {

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect(){
        return new TenantIgnoreAspect();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String appid0 = TenantContext.getCurrentTenant();
                if (StringUtils.hasLength(appid0)) {
                    return new StringValue(appid0);
                }
                String appid = RpcContext.getContext().getAttachment(Constant.APPID);
                if (!StringUtils.hasLength(appid)) {
                    log.warn("Appid Not Exists!");
                    throw new MybatisPlusException("Appid Not Exists! Use setAttachment at first please.");
                }
                return new StringValue(appid);
            }

            // This is the default method, it returns false by default, indicating that all tables require multi-tenancy conditions to be applied.
            @Override
            public boolean ignoreTable(String tableName) {
                return TenantContext.isIgnoreTenant();
            }

            @Override
            public String getTenantIdColumn() {
                return Constant.APPID;
            }
        }));
        // If you are using the pagination plugin, make sure to add the TenantLineInnerInterceptor first, and then add the PaginationInnerInterceptor.
        // When using the pagination plugin, it is essential to set MybatisConfiguration#useDeprecatedExecutor = false.
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}

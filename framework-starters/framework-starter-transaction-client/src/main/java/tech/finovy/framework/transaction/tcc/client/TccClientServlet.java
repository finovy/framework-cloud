package tech.finovy.framework.transaction.tcc.client;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import tech.finovy.framework.transaction.tcc.client.auth.SecurityContent;
import tech.finovy.framework.transaction.tcc.client.context.TccClientContext;
import tech.finovy.framework.transaction.tcc.client.core.DispatchHandler;
import tech.finovy.framework.transaction.tcc.client.core.InnerDispatchHandler;
import tech.finovy.framework.transaction.tcc.client.core.RpcReferenceHolder;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serial;

import static tech.finovy.framework.transaction.tcc.client.constant.Constants.RPC_CONTEXT_INFO;
import static tech.finovy.framework.transaction.tcc.client.constant.Constants.TENANT_APPID;

public class TccClientServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -7068107784271703422L;
    private final DataSource datasource;
    private final InnerDispatchHandler innerDispatcher;
    private final RpcReferenceHolder rpcReferenceHolder;

    public TccClientServlet(@Nullable DataSource datasource, InnerDispatchHandler innerDispatcher, RpcReferenceHolder rpcReferenceHolder) {
        this.datasource = datasource;
        this.innerDispatcher = innerDispatcher;
        this.rpcReferenceHolder = rpcReferenceHolder;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String[] paths = request.getPathInfo().split("/");
        String typeName = null;
        String groupName = null;
        String methodName = null;
        switch (paths.length) {
            case 4 -> {
                typeName = paths[1];
                groupName = paths[2];
                methodName = paths[3];
            }
            case 3 -> {
                typeName = paths[1];
                methodName = paths[2];
            }
        }
        final String txId = request.getHeader(TccClientContext.TX_ID);
        final String appId = request.getHeader("TENANT_SIGN");
        final long begin = System.currentTimeMillis();
        try {
            TccClientContext.setTx(new TccClientContext.TxInfo(txId, typeName));
            // 获取请求体内容
            SecurityContent content = JSON.parseObject(getRequestBody(request), SecurityContent.class);
            final boolean result = select(groupName, appId).dispatch(content, typeName, methodName);
            Log.info("{}/{}/{}", typeName, methodName, result);
            response.getWriter().write(JSON.toJSONString(new StageResult(result)));
        } catch (Exception e) {
            Log.error("error :{}", e.getMessage(), e);
            response.getWriter().write(JSON.toJSONString(new StageResult(Boolean.FALSE)));
        } finally {
            Log.info("[{}/{}] costed:{}", typeName, methodName, System.currentTimeMillis() - begin);
            TccClientContext.clear();
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    private DispatchHandler select(String groupName, String appId) {
        if (datasource != null || StringUtils.isEmpty(groupName)) {
            return innerDispatcher;
        } else {
            DispatchHandler dispatcher = rpcReferenceHolder.getDispatchReference(groupName, null);
            if (StringUtils.isNotEmpty(appId)) {
                RpcContext.getContext().setAttachment(TENANT_APPID, appId);
            }
            final String infoStr = JSON.toJSONString(TccClientContext.getTxInfo());
            RpcContext.getContext().setAttachment(RPC_CONTEXT_INFO, infoStr);
            return dispatcher;
        }
    }

    @Data
    @NoArgsConstructor
    public static class StageResult {
        boolean status;

        public StageResult(boolean status) {
            this.status = status;
        }
    }
}

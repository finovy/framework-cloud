package tech.finovy.framework.datasource.api;

import tech.finovy.framework.datasource.entity.DynamicResultSet;
import tech.finovy.framework.datasource.entity.PrepareEach;
import tech.finovy.framework.datasource.entity.PrepareEachList;

import java.util.List;
import java.util.Map;

public interface DynamicDatasourceApi {

    DynamicResultSet getDynamicResultSet(PrepareEach each, List<Map<String, Object>> input);

    DynamicResultSet getDynamicResultSet(PrepareEachList eachs, List<Map<String, Object>> input);

}

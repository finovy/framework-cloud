package tech.finovy.framework.elasticsearch.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import tech.finovy.framework.elasticsearch.entity.PageEsData;
import tech.finovy.framework.elasticsearch.entity.SearchHigthRequest;

import java.io.IOException;

@Valid
public interface ElasticSearchHigthLevelService {

//    <T> PageEsData<T> searchPage(SearchSourceBuilder sourceBuilder, SearchHigthRequest searchHigthRequest, Class<T> tClass);

//    <T> PageEsData<T> searchPage(SearchRequestEx searchRequestEx, String clientKey, Class<T> tClass);

    <T> PageEsData<T> searchPageByJson(SearchHigthRequest searchHigthRequest, @NotNull String jsonString, Class<T> tClass) throws IOException;

    <T> T searchEntityById(SearchHigthRequest searchHigthRequest, Class<T> tClass);

    <T> int saveOrUpdate(SearchHigthRequest searchHigthRequest, T t);

}

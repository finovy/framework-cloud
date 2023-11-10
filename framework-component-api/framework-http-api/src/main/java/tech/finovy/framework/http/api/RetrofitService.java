package tech.finovy.framework.http.api;

import tech.finovy.framework.http.entity.HtmlPage;
import tech.finovy.framework.http.entity.HttpHost;
import tech.finovy.framework.http.entity.HttpValue;

import java.io.File;
import java.io.InputStream;

public interface RetrofitService {
	HtmlPage postHtml(HttpHost httpHost,HttpValue httvalue);
	HtmlPage getHtml(HttpHost httpHost,HttpValue httvalue);
	HtmlPage upload(HttpHost httpHost,HttpValue httvalue,File file);
	InputStream inpuStream(HttpHost httpHost,HttpValue httvalue);
}

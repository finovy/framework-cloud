package tech.finovy.framework.logappender.push;

import com.google.common.util.concurrent.ListenableFuture;
import tech.finovy.framework.logappender.conf.ProducerConfig;
import tech.finovy.framework.logappender.conf.ProjectConfig;
import tech.finovy.framework.logappender.entry.LogItem;
import tech.finovy.framework.logappender.entry.Result;
import tech.finovy.framework.logappender.exception.ProducerException;

import java.util.List;

public interface Producer {

    ListenableFuture<Result> send(String project, String logStore, LogItem logItem) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, List<LogItem> logItems) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, LogItem logItem) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, List<LogItem> logItems) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, LogItem logItem) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, List<LogItem> logItems) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, LogItem logItem, Callback callback) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, LogItem logItem, Callback callback) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, LogItem logItem, Callback callback) throws InterruptedException, ProducerException;

    ListenableFuture<Result> send(String project, String logStore, String topic, String source, String shardHash, List<LogItem> logItems, Callback callback) throws InterruptedException, ProducerException;

    void close() throws InterruptedException, ProducerException;

    void close(long timeoutMs) throws InterruptedException, ProducerException;

    ProducerConfig getProducerConfig();

    int getBatchCount();

    int availableMemoryInBytes();

    void putProjectConfig(ProjectConfig projectConfig);

    void removeProjectConfig(ProjectConfig projectConfig);
}

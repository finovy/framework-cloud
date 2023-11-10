package tech.finovy.framework.disruptor.provider;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import tech.finovy.framework.common.core.chain.ChainSortUtil;
import tech.finovy.framework.common.core.loader.EnhancedServiceLoader;
import tech.finovy.framework.common.core.loader.LoadLevel;
import tech.finovy.framework.common.core.loader.Scope;
import tech.finovy.framework.disruptor.DisruptorConstant;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventContext;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.event.DisruptorEventTranslator;
import tech.finovy.framework.disruptor.core.exception.DisruptorException;
import tech.finovy.framework.disruptor.core.handler.DisruptorQueueEventHandler;
import tech.finovy.framework.disruptor.core.handler.DisruptorQueueExceptionHandler;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@LoadLevel(name = DisruptorConstant.DISRUPTOR, order = 1, scope = Scope.SINGLETON)
public class DefaultDisruptorEngineProvider implements DisruptorEngine {
    protected final Map<String, Map<String, ProcessInterface>> disruptorListeners;
    private final DisruptorEventConfiguration configuration;
    private final ExceptionHandler<DisruptorEvent> exceptionHandler;
    private final DisruptorEventTranslator publisher = new DisruptorEventTranslator();
    private final DisruptorEventContext disruptorEventContext = new DisruptorEventContext();
    private final AtomicInteger disruptorThreadName = new AtomicInteger();
    private final AtomicLong atomicPost = new AtomicLong();
    private final int availableProcessors = Runtime.getRuntime().availableProcessors();
    private DisruptorQueueEventHandler[] handlers;
    private Disruptor<DisruptorEvent> disruptor;

    public DefaultDisruptorEngineProvider(DisruptorEventConfiguration configuration) {
        this.configuration = configuration;
        this.exceptionHandler = new DisruptorQueueExceptionHandler(configuration, publisher, disruptorEventContext);
        List<ProcessInterface> processInterfaces = EnhancedServiceLoader.loadAll(ProcessInterface.class);
        processInterfaces.forEach(f -> f.setDisruptorEventConfiguration(configuration));
        disruptorListeners = ChainSortUtil.multiChainListenerSort(processInterfaces);
        start();
    }


    @Override
    public void start() {
        int max = availableProcessors;
        if (max > configuration.getMaxAvailableProcessors()) {
            // If the number of CPU cores exceeds the custom configuration, then use the custom configuration.
            max = configuration.getMaxAvailableProcessors();
        }
        this.handlers = new DisruptorQueueEventHandler[max];
        this.disruptor = new Disruptor<>(DisruptorEvent::new, configuration.getRingBufferSize(), r -> {
            Thread t = new Thread(r);
            t.setName("DisruptorThread" + "_" + disruptorThreadName.incrementAndGet());
            return t;
        }, ProducerType.MULTI,
                switchWaitStrategy(configuration.getWaitStrategy()));
        RingBuffer<DisruptorEvent> ringBuffer = disruptor.getRingBuffer();
        disruptorEventContext.setRingBuffer(ringBuffer);
        for (int i = 0; i < handlers.length; i++) {
            DisruptorQueueEventHandler handlerEvent = new DisruptorQueueEventHandler(disruptorListeners, i);
            handlers[i] = handlerEvent;
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        disruptor.setDefaultExceptionHandler(exceptionHandler);
        disruptor.start();
    }

    @Override
    public void shutdown() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
    }


    @Override
    public void post(DisruptorEvent event) {
        if (event == null) {
            throw new DisruptorException("DisruptorEvent IS NULL");
        }
        event.setApplication(configuration.getApplicationName());
        if (event.getDisruptorEventType() == null) {
            throw new DisruptorException("DisruptorEventType IS NULL");
        }
        if (event.getEvent() == null) {
            throw new DisruptorException("DisruptorEvent is empty,disruptorType:" + event.getDisruptorEventType());
        }
        if (event.getEventRetryCount() < 1) {
            event.setEventRetryCount(configuration.getRetryCount());
        }
        this.disruptor.getRingBuffer().publishEvent(publisher, event.getTransactionId(), atomicPost.incrementAndGet(), event);
    }

    @Override
    public void addProcessInterfaces(List<ProcessInterface> processInterfaces) {
        Map<String, Map<String, ProcessInterface>> sorListener = ChainSortUtil.multiChainListenerSort(processInterfaces);
        for (Map.Entry<String, Map<String, ProcessInterface>> item : sorListener.entrySet()) {
            Map<String, ProcessInterface> ex = disruptorListeners.get(item.getKey());
            if (ex == null) {
                disruptorListeners.put(item.getKey(), item.getValue());
            } else {
                ex.putAll(item.getValue());
                Map<String, ProcessInterface> m = ChainSortUtil.singleChainListenerSort(ex);
                ex.clear();
                ex.putAll(m);
            }
        }
    }

    @Override
    public void addProcessInterface(ProcessInterface processInterface) {
        List<ProcessInterface> processInterfaces = new ArrayList<>();
        processInterfaces.add(processInterface);
        addProcessInterfaces(processInterfaces);
    }
}

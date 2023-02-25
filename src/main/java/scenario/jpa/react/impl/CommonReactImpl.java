package scenario.jpa.react.impl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import scenario.exceptions.QueueFullException;
import scenario.jpa.react.CommonReact;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class CommonReactImpl<Rq, Rs, QR> implements CommonReact<Rq, Rs> {

    protected final int SLEEP = 100;
    protected final int SLEEP_FIRST = 10000;
    protected final int MAX = 1000;
    protected final int MAX_QUEUE = 10000;
    protected boolean run = true;

    // Map with current requests
    protected final Map<Rq, QR> map = new HashMap<>(MAX);
    // Response queue
    @Getter
    protected final BlockingQueue<Pair<Rq, QR>> blockingQueue = new
            ArrayBlockingQueue<>(MAX_QUEUE);

    protected final HasMapCollection collection = new HasMapCollection(map);

    protected AsyncService asyncService;

    private MeterRegistry meterRegistry;

    @Override
    public Mono<Rs> apply(Rq rq){
        return Mono.create(monoSink -> {
            if (blockingQueue.offer(newQueueItem(rq, monoSink))){
                monoSink.error(new QueueFullException(null, getNameThread()));
            }
        });
    }

    protected abstract  Pair<Rq, QR> newQueueItem(Rq rq, MonoSink<Rs> monoSink);

    protected abstract long next() throws InterruptedException;
    protected abstract void error(Exception exception);

    private Timer timerQueueSize;
    private Timer timerNextSize;
    private Timer timerNext;

    void thread(){
        log.info("Thread started");
        while(run){
            try {
                timerQueueSize.record(blockingQueue.size(), timerQueueSize.baseTimeUnit());
                long start = System.currentTimeMillis();
                long nextSize = next();
                timerNext.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
                timerNetSize.record(nextSize, timerNetSize.baseTimeUnit());
            } catch (InterruptedException e) {
               log.error(e.getMessage(), e);
               try {
                   error(e);
               } catch (Exception e2){
                   log.error(e2.getMessage(), e2);
                   map.clear();
               }
            }
        }
        log.info("Stop thread");
    }

    @PostConstruct
    private void init(){
        timerQueueSize = Timer.builder("jpa.react.queue")
                .description("DB queue size")
                .tag("thread", getNameThread())
                .register(meterRegistry);
        timerNext = Timer.builder("jpa.react.next")
                .description("Processing time per cycle")
                .tag("thread", getNameThread())
                .register(meterRegistry);
        timerNextSize = Timer.builder("jpa.react.next.size")
                .description("Amount of processing")
                .tag("thread", getNameThread())
                .register(meterRegistry);

        Thread thread = new Thread(this::thread, getNameThread());
        thread.setDaemon(true);
        thread.start();
    }

    protected String getNameThread(){
        return getClass().getSimpleName();
    }

    protected abstract MonoSink<Rs> getMonoSink(QR value);

    protected void sendSuccess(MonoSink<Rs> monoSink, Rs rs){
        asyncService.schedule(()-> monoSink.success(rs));
    }

    protected void sendError(MonoSink<Rs> monoSink, Exception exception){
        asyncService.schedule(()-> monoSink.error(exception));
    }

    @PreDestroy
    private void storp(){
        run=false;
    }

@RequiredArgsConstructor
public class HasMapCollection implements Collection<Pair<Rq, QR>> {
    private final Map<Rq, QR> map;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    @NonNull
    public Iterator<Pair<Rq, QR>> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    @NonNull
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return a;
    }

    @Override
    public boolean add(Pair<Rq, QR> pair) {
        if (pair == null) return false;
        if (map.containsKey(pair.getKey())) {
            if (!blockingQueue.offer(pair)) {
                getMonoSink(pair.getValue()).error(new QueueFullException(null, getNameThread()));
            }
        } else {
            map.put(pair.getKey(), pair.getValue());
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Pair<Rq, QR>> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
}
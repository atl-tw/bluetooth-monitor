package com.thoughtworks.bluetooth.monitor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by rcooper on 4/20/16.
 */
public class Watcher {

    private static final Logger LOGGER = Logger.getLogger(Watcher.class.getCanonicalName());
    private Cache<Class, CopyOnWriteArrayList<Listener>> listeners =  CacheBuilder.newBuilder()
            .concurrencyLevel(128)
            .build();
    private final Callable<CopyOnWriteArrayList<Listener>> callable = CopyOnWriteArrayList::new;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private String hostname;

    private Watcher(Stream<InputStream> inputs){
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Failed to get host name.");
            hostname = "UNKNOWN";
        }
        inputs.map((is) -> {
            return new Thread( () ->{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                StringBuilder pack = new StringBuilder();
                try {
                    while(running.get() && (line = reader.readLine()) != null){
                        //System.out.println("## "+Thread.currentThread().getId() +" "+line);
                        if(line.charAt(0) == ' '){
                            pack.append(line)
                                    .append('\n');
                        } else {
                            parseAndDispatch(pack);
                            pack = new StringBuilder(line).append("\n");
                        }
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "IO Exception reading from input stream.", e);
                }
                LOGGER.info(String.format("Thread %d exiting.", Thread.currentThread().getId()));
            });
        }).forEach((t) -> {
            t.setDaemon(true);
            t.start();
            LOGGER.info(String.format("Thread %d starting.", Thread.currentThread().getId()));
        });
        LOGGER.info("Watcher started.");
    }



    void parseAndDispatch(StringBuilder data){
        final String str = data.toString();
        Event.extractMacAddress(str)
                .ifPresent((mac) ->{
                    RSSIEvent.parsePacket(hostname, mac, str)
                            .ifPresent(this::dispatch);
                    DeviceInfoEvent.parsePacket(hostname, mac, str)
                            .ifPresent(this::dispatch);
                });

    }

    void dispatch(Event e){
        try {
            listeners.get(e.getClass(), callable)
                    .parallelStream()
                    .forEach(l -> l.onEvent(e));
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }


    public <T extends Event> void addListener(Class<T> type, Listener<T> listener){
        try {
            this.listeners.get(type, callable).add(listener);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public  <T extends Event> void removeListener(Class<T> type, Listener<T> listener){
        try {
            this.listeners.get(type, callable).remove(listener);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Watcher startWatcher(Stream<InputStream> stream){
        return new Watcher(stream);
    }



    public interface Listener<T extends Event> {

        void onEvent(T event);
    }
}

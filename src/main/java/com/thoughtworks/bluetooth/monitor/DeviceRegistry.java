package com.thoughtworks.bluetooth.monitor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by rcooper on 4/20/16.
 */
public class DeviceRegistry {

    private Cache<String, Device> devices = CacheBuilder.newBuilder()
                                                        .expireAfterAccess(1, TimeUnit.HOURS)
                                                        .build();

    public Device getDevice(String macAddress){
        try {
            return devices.get(macAddress, () -> new Device(macAddress));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}

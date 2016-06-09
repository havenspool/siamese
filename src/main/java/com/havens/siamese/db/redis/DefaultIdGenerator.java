package com.havens.siamese.db.redis;

import com.havens.siamese.db.IdGenerator;

/**
 * Created by havens on 15-8-12.
 */
public class DefaultIdGenerator implements IdGenerator {
    private final long datacenterIdShift = 3L;
    private final long timestampLeftShift = 10L;
    private final long sequenceMax = 8;
    private final long twepoch = 1288834974657L;
    private final long datacenterId;
    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;
//    private final long datacenterIdBits = 24L;

    public DefaultIdGenerator() {
        datacenterId = 1;
    }

    public String generateStringId() {
        return generateLongId().toString();
    }

    public synchronized Long generateLongId() {
//        System.out.println(System.currentTimeMillis());
        long timestamp = getLastTimestamp();
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) % sequenceMax;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        return (timestamp << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = getLastTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getLastTimestamp();
        }
        return timestamp;
    }

    private long getLastTimestamp(){
        return (System.currentTimeMillis() - twepoch);
    }

    public static void main(String[] args) {
        IdGenerator idGenerator =new DefaultIdGenerator();
        for (int i = 0; i < 20; i++) {
            System.out.println(idGenerator.generateLongId());
        }

        //770667812166303752
        //123456789012345678
        //146617543567369
        //192174233856507915
    }

}

package com.havens.siamese.entity.idGenerator;

import com.havens.siamese.db.IdGenerator;

/**
 * Created by havens on 16-5-31.
 */
public class RoleIdGenerator implements IdGenerator {
    private final long datacenterIdShift = 3L;
    private final long timestampLeftShift = 27L;
    private final long sequenceMax = 8L;
    private final long twepoch = 1288834974657L;
    private final long datacenterId;
    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    public RoleIdGenerator(int roleId) {
        this.datacenterId = roleId;
    }

    public String generateStringId() {
        return generateLongId().toString();
    }

    public synchronized Long generateLongId() {
        long timestamp = getLastTimestamp();
        if (this.lastTimestamp == timestamp) {
            this.sequence = ((this.sequence + 1L) % 8L);
            if (this.sequence == 0L)
                timestamp = tilNextMillis(this.lastTimestamp);
        }
        else {
            this.sequence = 0L;
        }
        this.lastTimestamp = timestamp;
        return Long.valueOf(timestamp << 27 | this.datacenterId << 3 | this.sequence);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = getLastTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getLastTimestamp();
        }
        return timestamp;
    }

    private long getLastTimestamp() {
        return (System.currentTimeMillis() - 1288834974657L) / 100L;
    }

    public int decodeHeroId(long id) {
        long tt = id;
        tt >>= 3;
        tt &= 16777215L;
        return (int)tt;
    }

    public static void main(String[] args) throws Exception{
        RoleIdGenerator idGenerator = new RoleIdGenerator(1133105);
        System.out.println(2147483647);

        long maxDatacenterId = 16777215L;
        long timestamp = System.currentTimeMillis();

        long TT = 370630783228834496L;
        System.out.println(TT);
        System.out.println(9223372036854775807L);
        System.out.println(maxDatacenterId);
        System.out.println(timestamp);
        System.out.println(timestamp - 1288834974657L);
        for (int i = 0; i < 20; i++) {
            Thread.sleep(5L);
            System.out.println("gen::" + idGenerator.generateLongId());
        }

        System.out.println(idGenerator.decodeHeroId(185316097062795623L));
    }
}
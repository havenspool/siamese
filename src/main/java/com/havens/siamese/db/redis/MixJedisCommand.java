package com.havens.siamese.db.redis;

import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.MultiKeyCommands;

/**
 * Created by havens on 15-8-12.
 */
public interface MixJedisCommand extends JedisCommands,MultiKeyCommands {
}

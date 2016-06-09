package com.havens.siamese.db;

/**
 *
 *
 *  ID generator
 *
 * Created by havens on 15-8-12.
 */
public interface IdGenerator {

    String generateStringId();

    Long generateLongId();
}

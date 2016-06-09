
package com.havens.siamese.service;

import org.junit.BeforeClass;

/**
 * Created by havens on 15-8-7.
 */
public class RoleServiceTest {

    public static RoleServiceConn testCon = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (testCon == null)
            testCon = new RoleServiceConn("127.0.0.1", 8090, "asan", "123456");
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

}

/*******************************************************************************
 * Copyright (c) quickfixengine.org  All rights reserved.
 *
 * This file is part of the QuickFIX FIX Engine
 *
 * This file may be distributed under the terms of the quickfixengine.org
 * license as defined by quickfixengine.org and appearing in the file
 * LICENSE included in the packaging of this file.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 * See http://www.quickfixengine.org/LICENSE for licensing information.
 *
 * Contact ask@quickfixengine.org if any conditions of this licensing
 * are not clear to you.
 ******************************************************************************/

package quickfix.test.acceptance.resynch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import quickfix.ConfigError;
import quickfix.SessionNotFound;

/**
 * This is testing the test framework rather than QFJ functionality per se
 */
public class ResynchTest {
    private Thread serverThread;

    ResynchTestServer server;

    @Test(timeout=30000)
    public void testAcceptorTimerSync() throws ConfigError, SessionNotFound, InterruptedException {
        serverThread.start();
        server.waitForInitialization();
        new ResynchTestClient().run();
    }

    @Test(timeout=30000)
    public void testAcceptorTimerUnsyncWithValidatingSequenceNumbers() throws ConfigError, SessionNotFound, InterruptedException {
        server.setUnsynchMode(true);
        server.setValidateSequenceNumbers(true);
        serverThread.start();
        server.waitForInitialization();
        ResynchTestClient client = new ResynchTestClient();
        client.setUnsynchMode(true);
        client.run();
    }

    @Test(timeout=30000)
    public void testAcceptorTimerUnsyncWithoutValidatingSequenceNumbers() throws ConfigError, SessionNotFound, InterruptedException {
        server.setUnsynchMode(true);
        server.setValidateSequenceNumbers(false);
        serverThread.start();
        server.waitForInitialization();
        ResynchTestClient client = new ResynchTestClient();
        client.setUnsynchMode(false);
        client.setForceResynch(true);
        client.run();
    }

    @Before
    public void setUp() throws Exception {
        server = new ResynchTestServer();
        serverThread = new Thread(server, "TimerTestServer");
        serverThread.setDaemon(true);
    }

    @After
    public void tearDown() throws Exception {
        serverThread.interrupt();
    }
}

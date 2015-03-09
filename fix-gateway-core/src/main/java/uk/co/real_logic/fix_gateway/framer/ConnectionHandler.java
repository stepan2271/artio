/*
 * Copyright 2015 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.fix_gateway.framer;

import uk.co.real_logic.fix_gateway.framer.session.AcceptorSession;
import uk.co.real_logic.fix_gateway.framer.session.InitiatorSession;
import uk.co.real_logic.fix_gateway.framer.session.Session;
import uk.co.real_logic.fix_gateway.framer.session.SessionProxy;
import uk.co.real_logic.fix_gateway.util.MilliClock;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Handles incoming connections including setting up framers.
 *
 * Threadsafe.
 */
public class ConnectionHandler
{
    private final AtomicLong idSource = new AtomicLong(0);

    private final MilliClock clock;
    private final SessionProxy sessionProxy;
    private final int bufferSize;
    private final int defaultInterval;
    private final MessageHandler messageHandler;

    public ConnectionHandler(
        final MilliClock clock,
        final SessionProxy sessionProxy,
        final int bufferSize,
        final int defaultInterval,
        final MessageHandler messageHandler)
    {
        this.clock = clock;
        this.sessionProxy = sessionProxy;
        this.bufferSize = bufferSize;
        this.defaultInterval = defaultInterval;
        this.messageHandler = messageHandler;
    }

    public long onConnection() throws IOException
    {
        return idSource.getAndIncrement();
    }

    public ReceiverEndPoint receiverEndPoint(
        final SocketChannel channel, final long connectionId, final Session session)
    {
        return new ReceiverEndPoint(channel, bufferSize, messageHandler, connectionId, session);
    }

    public SenderEndPoint senderEndPoint(final SocketChannel channel, final long connectionId)
    {
        return new SenderEndPoint(connectionId, channel);
    }

    public AcceptorSession acceptorSession(final long connectionId)
    {
        return new AcceptorSession(defaultInterval, connectionId, clock, sessionProxy);
    }

    public InitiatorSession initiatorSession(final long connectionId)
    {
        return new InitiatorSession(defaultInterval, connectionId, clock, sessionProxy);
    }
}

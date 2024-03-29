/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketInputStream
extends FilterInputStream {
    private Socket __socket;

    public SocketInputStream(Socket socket, InputStream stream) {
        super(stream);
        this.__socket = socket;
    }

    public void close() throws IOException {
        super.close();
        this.__socket.close();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.net.ftp;

import org.apache.commons.net.ftp.parser.RegexFTPFileEntryParserImpl;

public abstract class FTPFileListParserImpl
extends RegexFTPFileEntryParserImpl {
    public FTPFileListParserImpl(String regex) {
        super(regex);
    }
}


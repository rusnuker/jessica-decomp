/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class CPacketUpdateSign
implements Packet<INetHandlerPlayServer> {
    private BlockPos pos;
    private String[] lines;

    public CPacketUpdateSign() {
    }

    public CPacketUpdateSign(BlockPos posIn, ITextComponent[] linesIn) {
        this.pos = posIn;
        this.lines = new String[]{linesIn[0].getUnformattedText(), linesIn[1].getUnformattedText(), linesIn[2].getUnformattedText(), linesIn[3].getUnformattedText()};
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.pos = buf.readBlockPos();
        this.lines = new String[4];
        int i = 0;
        while (i < 4) {
            this.lines[i] = buf.readStringFromBuffer(384);
            ++i;
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.pos);
        int i = 0;
        while (i < 4) {
            buf.writeString(this.lines[i]);
            ++i;
        }
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processUpdateSign(this);
    }

    public BlockPos getPosition() {
        return this.pos;
    }

    public String[] getLines() {
        return this.lines;
    }
}


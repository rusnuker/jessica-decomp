/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class CPacketTabComplete
implements Packet<INetHandlerPlayServer> {
    private String message;
    private boolean hasTargetBlock;
    @Nullable
    private BlockPos targetBlock;

    public CPacketTabComplete() {
    }

    public CPacketTabComplete(String messageIn, @Nullable BlockPos targetBlockIn, boolean hasTargetBlockIn) {
        this.message = messageIn;
        this.targetBlock = targetBlockIn;
        this.hasTargetBlock = hasTargetBlockIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.message = buf.readStringFromBuffer(Short.MAX_VALUE);
        this.hasTargetBlock = buf.readBoolean();
        boolean flag = buf.readBoolean();
        if (flag) {
            this.targetBlock = buf.readBlockPos();
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(StringUtils.substring((String)this.message, (int)0, (int)Short.MAX_VALUE));
        buf.writeBoolean(this.hasTargetBlock);
        boolean flag = this.targetBlock != null;
        buf.writeBoolean(flag);
        if (flag) {
            buf.writeBlockPos(this.targetBlock);
        }
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processTabComplete(this);
    }

    public String getMessage() {
        return this.message;
    }

    @Nullable
    public BlockPos getTargetBlock() {
        return this.targetBlock;
    }

    public boolean hasTargetBlock() {
        return this.hasTargetBlock;
    }
}


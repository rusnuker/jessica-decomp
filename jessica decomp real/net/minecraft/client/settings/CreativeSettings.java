/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.settings;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreativeSettings {
    private static final Logger field_192566_b = LogManager.getLogger();
    protected Minecraft field_192565_a;
    private final File field_192567_c;
    private final HotbarSnapshot[] field_192568_d = new HotbarSnapshot[9];

    public CreativeSettings(Minecraft p_i47395_1_, File p_i47395_2_) {
        this.field_192565_a = p_i47395_1_;
        this.field_192567_c = new File(p_i47395_2_, "hotbar.nbt");
        int i = 0;
        while (i < 9) {
            this.field_192568_d[i] = new HotbarSnapshot();
            ++i;
        }
        this.func_192562_a();
    }

    public void func_192562_a() {
        try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(this.field_192567_c);
            if (nbttagcompound == null) {
                return;
            }
            int i = 0;
            while (i < 9) {
                this.field_192568_d[i].func_192833_a(nbttagcompound.getTagList(String.valueOf(i), 10));
                ++i;
            }
        }
        catch (Exception exception) {
            field_192566_b.error("Failed to load creative mode options", (Throwable)exception);
        }
    }

    public void func_192564_b() {
        try {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            int i = 0;
            while (i < 9) {
                nbttagcompound.setTag(String.valueOf(i), this.field_192568_d[i].func_192834_a());
                ++i;
            }
            CompressedStreamTools.write(nbttagcompound, this.field_192567_c);
        }
        catch (Exception exception) {
            field_192566_b.error("Failed to save creative mode options", (Throwable)exception);
        }
    }

    public HotbarSnapshot func_192563_a(int p_192563_1_) {
        return this.field_192568_d[p_192563_1_];
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.minecraftforge.registries;

import net.minecraft.util.ResourceLocation;

public interface IRegistryDelegate<T> {
    public T get();

    public ResourceLocation name();

    public Class<T> type();
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.minecraft.util.registry;

import java.util.Set;
import javax.annotation.Nullable;

public interface IRegistry<K, V>
extends Iterable<V> {
    @Nullable
    public V getObject(K var1);

    public void putObject(K var1, V var2);

    public Set<K> getKeys();
}


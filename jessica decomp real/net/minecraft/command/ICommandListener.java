/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface ICommandListener {
    public void notifyListener(ICommandSender var1, ICommand var2, int var3, String var4, Object ... var5);
}


package com.haszing.hue.actions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

/**
 * Mark a class as a sub command of the hue command.
 */
public abstract class HueAction {
    /**
     * The name of the command.
     */
    public abstract String getName();

    public abstract void run(EntityPlayer player, String[] args);

    void sendMessage(EntityPlayer player, String message) {
        TextComponentString msg = new TextComponentString(message);
        player.addChatComponentMessage(msg, false);
    }
}

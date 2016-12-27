package com.haszing.hue.actions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

/**
 * Mark a class as a sub command of the hue command.
 */
public abstract class HueAction {
    protected EntityPlayer player;

    /**
     * The name of the command.
     */
    public abstract String getName();

    public abstract void run(String[] args);

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    void sendMessage(String message) {
        TextComponentString msg = new TextComponentString(message);
        this.player.addChatComponentMessage(msg, false);
    }
}

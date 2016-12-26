package com.haszing.hue.actions;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Pair with the Philips Hue bridge.
 */
public class Pair extends HueAction {
    @Override
    public String getName() {
        return "pair";
    }

    @Override
    public void run(EntityPlayer player, String[] args) {
        this.sendMessage(player, "Looking for bridge…");
    }
}

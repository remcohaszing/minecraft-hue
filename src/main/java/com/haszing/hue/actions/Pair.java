package com.haszing.hue.actions;

import com.haszing.hue.HueManager;

/**
 * Pair with the Philips Hue bridge.
 */
public class Pair extends HueAction {
    private String id;
    private HueManager hueManager = HueManager.getInstance();

    @Override
    public String getName() {
        return "pair";
    }

    @Override
    public void run(String[] args) {
        if (args.length < 2) {
            return;
        }
        String id = "";
        for (int i = 1; i < args.length; i++) {
            id += args[i];
        }
        this.id = id;
        this.hueManager.pair(id);
    }
}

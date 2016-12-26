package com.haszing.hue;

import com.haszing.hue.commands.HueCommand;
import com.philips.lighting.hue.sdk.PHHueSDK;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = HueMod.MOD_ID, name = HueMod.NAME, version = HueMod.VERSION)
public class HueMod {
    /**
     * The id of the mod.
     */
    static final String MOD_ID = "hue";

    /**
     * The human readable name of the mod.
     */
    static final String NAME = "Minecraft Philips Hue";

    /**
     * A semantic version of the mod.
     */
    static final String VERSION = "1.0.0";

    /**
     * The command manager of the local server.
     */
    private ServerCommandManager commandManager;
    private PHHueSDK phHueSDK;

    public HueMod() {
        this.phHueSDK = PHHueSDK.getInstance();
        this.phHueSDK.setAppName(NAME);
    }

    /**
     * Save the instance of the command manager and initialize the commands.
     *
     * @param event The server starting event that was fired.
     */
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        this.commandManager = (ServerCommandManager) event.getServer().getCommandManager();
        this.initCommands();
    }

    /**
     * Initialize the hue command.
     */
    private void initCommands() {
        this.commandManager.registerCommand(new HueCommand());
    }
}

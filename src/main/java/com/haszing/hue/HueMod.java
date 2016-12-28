package com.haszing.hue;

import com.haszing.hue.commands.HueCommand;
import com.haszing.hue.events.EventListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.UnsupportedEncodingException;

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
    private EventListener listener;

    public HueMod() {
        this.phHueSDK = PHHueSDK.getInstance();
        this.phHueSDK.setAppName(NAME);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws UnsupportedEncodingException {
        HueManager.getInstance().start();
        this.listener = new EventListener();
        MinecraftForge.EVENT_BUS.register(this.listener);
        FMLCommonHandler.instance().bus().register(this.listener);
    }

    /**
     * Save the instance of the command manager and initialize the commands.
     *
     * @param event The server starting event that was fired.
     */
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        HueManager hueManager = HueManager.getInstance();
        hueManager.setMinecraftServer(server);
        this.listener.setWorld(server.getEntityWorld());
        this.commandManager = (ServerCommandManager) server.getCommandManager();
        this.initCommands();
    }

    /**
     * Initialize the hue command.
     */
    private void initCommands() {
        this.commandManager.registerCommand(new HueCommand());
    }
}

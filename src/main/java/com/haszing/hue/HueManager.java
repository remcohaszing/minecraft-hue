package com.haszing.hue;

import com.haszing.hue.models.Color;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class HueManager implements PHSDKListener {
    private static String HUE_GROUP_NAME = "minecraft";
    private PHHueSDK phHueSDK = PHHueSDK.getInstance();
    private Color color;
    private List<PHAccessPoint> accessPoints = new ArrayList<PHAccessPoint>();
    private static HueManager instance;
    private MinecraftServer minecraftServer;

    public static HueManager getInstance() {
        if (HueManager.instance == null) {
            HueManager.instance = new HueManager();
        }
        return HueManager.instance;
    }

    public PHAccessPoint[] getAccessPoints() {
        return (PHAccessPoint[]) this.accessPoints.toArray();
    }

    public void setMinecraftServer(MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    public void adjustColor(String rgb) {
        if (rgb.startsWith("#")) {
            rgb = rgb.substring(1);
        }
        int colors = Integer.parseInt(rgb, 16);
        int red = (colors & 0xff0000) / 0x10000;
        int green = (colors & 0x00ff00) / 0x100;
        int blue = colors & 0x0000ff;
        this.adjustColor(new Color(red, green, blue));
    }

    public void adjustColor(Color color) {
        if (color.equals(this.color)) {
            return;
        }
        this.color = color;
        List<PHBridge> bridges = this.phHueSDK.getAllBridges();
        if (bridges.isEmpty()) {
            return;
        }
        for (PHBridge bridge : bridges) {
            for (PHLight light : bridge.getResourceCache().getAllLights()) {
                PHLightState lightState = new PHLightState();
                float[] xy = PHUtilities.calculateXYFromRGB(color.getRed(), color.getGreen(), color.getBlue(), light.getModelNumber());
                lightState.setX(xy[0]);
                lightState.setY(xy[1]);
                bridge.updateLightState(light, lightState);
            }
        }
    }

    public void pair(String id) {
        for (PHAccessPoint accessPoint : this.accessPoints) {
            if (accessPoint.getBridgeId().endsWith(id)) {
                phHueSDK.connect(accessPoint);
            }
        }
    }

    public void start() {
        this.phHueSDK.getNotificationManager().registerSDKListener(this);
        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
    }

    private void sendMessage(String message) {
        if (this.minecraftServer != null) {
            this.minecraftServer.addChatMessage(new TextComponentString(message));
        }
    }

    @Override
    public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {

    }

    @Override
    public void onBridgeConnected(PHBridge bridge, String s) {
        this.sendMessage("Connected to bridge " + bridge);
    }

    @Override
    public void onAuthenticationRequired(PHAccessPoint accessPoint) {
        this.sendMessage("Please press the link button on the Philips Hue bridge");
        phHueSDK.startPushlinkAuthentication(accessPoint);
    }

    @Override
    public void onAccessPointsFound(List<PHAccessPoint> list) {
        this.accessPoints.addAll(list);
    }

    @Override
    public void onError(int i, String message) {
        this.sendMessage("§4" + message);
    }

    @Override
    public void onConnectionResumed(PHBridge phBridge) {

    }

    @Override
    public void onConnectionLost(PHAccessPoint phAccessPoint) {

    }

    @Override
    public void onParsingErrors(List<PHHueParsingError> list) {

    }
}

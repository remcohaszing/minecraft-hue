package com.haszing.hue.actions;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Pair with the Philips Hue bridge.
 */
public class Pair extends HueAction {
    private String id;
    private PHHueSDK phHueSDK = PHHueSDK.getInstance();
    private PHSDKListener listener = new PHSDKListener() {
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            sendMessage("Connected to Philps Hue bridge");
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
            sendMessage("Please press the link button on the Philips Hue bridge");
            phHueSDK.startPushlinkAuthentication(accessPoint);
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
            boolean found = false;
            if (id != null) {
                for (PHAccessPoint accessPoint : accessPoints) {
                    if (accessPoint.getBridgeId().endsWith(id)) {
                        phHueSDK.connect(accessPoint);
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                return;
            }
            for (PHAccessPoint accessPoint : accessPoints) {
                String bridgeId = accessPoint.getBridgeId();
                sendMessage("Found bridge id" + bridgeId.substring(bridgeId.length() - 6, bridgeId.length()));
            }
        }

        @Override
        public void onError(int i, String s) {
            sendMessage(i + " " + s);
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            sendMessage("connection resumed");
        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            sendMessage("connection lost");
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
        }
    };

    public Pair() {
        this.phHueSDK.getNotificationManager().registerSDKListener(listener);
    }

    @Override
    public String getName() {
        return "pair";
    }

    @Override
    public void run(String[] args) {
        this.sendMessage("Looking for bridge…");
        if (args.length > 1) {
            String id = "";
            for (int i = 1; i < args.length; i++) {
                this.sendMessage(id += args[i]);
            }
            this.id = id;
        }
        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
    }
}

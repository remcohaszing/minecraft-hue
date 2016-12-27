package com.haszing.hue.events;

import com.haszing.hue.models.Color;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListener {
    private World world;
    private PHHueSDK phHueSDK = PHHueSDK.getInstance();
    private Map<Class<? extends Biome>, Color> biomeMap;

    public EventListener() {
        this.biomeMap = new HashMap<Class<? extends Biome>, Color>();
        this.biomeMap.put(BiomeDesert.class, new Color(200, 180, 10));
        this.biomeMap.put(BiomeOcean.class, new Color(10, 50, 255));
        this.biomeMap.put(BiomePlains.class, new Color(60, 255, 5));
    }

    private void adjustColor(Color color) {
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

    public void setWorld(World world) {
        this.world = world;
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        this.adjustColor(new Color(255, 0, 0));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        BlockPos pos = player.getPosition();
        Chunk chunk = this.world.getChunkFromBlockCoords(pos);
        Biome biome = chunk.getBiome(pos, this.world.getBiomeProvider());
        Color color = this.biomeMap.get(biome.getClass());
        if (color != null) {
            this.adjustColor(color);
        }
    }
}

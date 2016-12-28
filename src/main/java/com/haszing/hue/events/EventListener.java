package com.haszing.hue.events;

import com.haszing.hue.HueManager;
import com.haszing.hue.models.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class EventListener {
    private World world;
    private Map<Class<? extends Biome>, Color> biomeMap;
    private HueManager hueManager = new HueManager();

    public EventListener() {
        this.biomeMap = new HashMap<Class<? extends Biome>, Color>();
        this.biomeMap.put(BiomeDesert.class, new Color(200, 180, 10));
        this.biomeMap.put(BiomeOcean.class, new Color(10, 50, 255));
        this.biomeMap.put(BiomePlains.class, new Color(60, 255, 5));
    }

    public void setWorld(World world) {
        this.world = world;
    }

//    @SubscribeEvent
//    public void onLivingHurt(LivingHurtEvent event) {
//        if (!(event.getEntity() instanceof EntityPlayer)) {
//            return;
//        }
//        EntityPlayer player = (EntityPlayer) event.getEntity();
//        this.adjustColor(new Color(255, 0, 0));
//    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        if (player.isDead) {
            this.hueManager.adjustColor(new Color(255, 0, 0));
            return;
        }
        BlockPos pos = player.getPosition();
        Chunk chunk = this.world.getChunkFromBlockCoords(pos);
        Biome biome = chunk.getBiome(pos, this.world.getBiomeProvider());
        Color color = this.biomeMap.get(biome.getClass());
        if (color != null) {
            this.hueManager.adjustColor(color);
        }
    }
}

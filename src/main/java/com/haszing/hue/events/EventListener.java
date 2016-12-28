package com.haszing.hue.events;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EventListener {
    private World world;
    private Map<String, String> biomeMap;
    private HueManager hueManager = new HueManager();

    public EventListener() throws UnsupportedEncodingException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, String>>(){}.getType();
        InputStream stream = classLoader.getResourceAsStream("biomes.json");
        this.biomeMap = gson.fromJson(new JsonReader(new InputStreamReader(stream, "UTF-8")), listType);
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
        String color = this.biomeMap.get(biome.getRegistryName().getResourcePath());
        if (color != null) {
            this.hueManager.adjustColor(color);
        }
    }
}

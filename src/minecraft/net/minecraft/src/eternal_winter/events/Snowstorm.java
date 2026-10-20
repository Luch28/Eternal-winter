package net.minecraft.src.eternal_winter.events;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import java.util.Random;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Snowstorm {
    private static boolean isInitialized = false;
    private static boolean isSnowing = true;
    private static long nextChangeTick = 0;
    private static final Random rand = new Random();

    public static void tick(World world) {
        if (world == null) return;

        if (!isInitialized) {
            setAllBiomesToSnow();
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setRainTime(1000000);
            
            int days = 30 + rand.nextInt(11);
            nextChangeTick = world.getWorldTime() + days * 24000L;
            
            isInitialized = true;
            isSnowing = true;
        }

        long currentTick = world.getWorldTime();

        if (currentTick >= nextChangeTick) {
            isSnowing = !isSnowing;
            if (isSnowing) {
                int days = 30 + rand.nextInt(11);
                nextChangeTick = currentTick + days * 24000L;
                world.getWorldInfo().setRaining(true);
                world.getWorldInfo().setRainTime(1000000);
            } else {
                int days = 1;
                nextChangeTick = currentTick + days * 24000L;
                world.getWorldInfo().setRaining(false);
                world.getWorldInfo().setRainTime(0);
            }
        }

        if (isSnowing && currentTick % 20L == 0L) {
            freezeWaterAroundPlayers(world);
        }
    }

    private static void setAllBiomesToSnow() {
        try {
            Field enableSnowField = BiomeGenBase.class.getDeclaredField("enableSnow");
            enableSnowField.setAccessible(true);
            Field enableRainField = BiomeGenBase.class.getDeclaredField("enableRain");
            enableRainField.setAccessible(true);

            for (Field f : BiomeGenBase.class.getDeclaredFields()) {
                if (f.getType() == BiomeGenBase.class && Modifier.isStatic(f.getModifiers())) {
                    try {
                        f.setAccessible(true);
                        BiomeGenBase biome = (BiomeGenBase) f.get(null);
                        if (biome != null) {
                            enableSnowField.setBoolean(biome, true);
                            enableRainField.setBoolean(biome, true);
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void freezeWaterAroundPlayers(World world) {
        for (Object obj : world.playerEntities) {
            EntityPlayer player = (EntityPlayer) obj;
            int px = (int) player.posX;
            int pz = (int) player.posZ;
            int radius = 16;

            for (int x = px - radius; x <= px + radius; x++) {
                for (int z = pz - radius; z <= pz + radius; z++) {
                    int topY = world.getHeightValue(x, z);
                    for (int y = topY; y > topY - 5 && y >= 0; y--) {
                        int blockId = world.getBlockId(x, y, z);
                        if (blockId == Block.waterStill.blockID || blockId == Block.waterMoving.blockID) {
                            int above = world.getBlockId(x, y + 1, z);
                            if (above == 0 || above == Block.snow.blockID) {
                                world.setBlock(x, y, z, Block.ice.blockID);
                            }
                        }
                    }
                }
            }
        }
    }
    public static boolean isSnowing() {
        return isSnowing;
    }
}
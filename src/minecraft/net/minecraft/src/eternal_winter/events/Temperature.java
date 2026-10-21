package net.minecraft.src.eternal_winter.events;

import net.minecraft.src.*;
import java.util.Random;

public class Temperature {
    @SuppressWarnings("unused")
	private static final Random rand = new Random();

    private static float bodyTemp = 36.6F;
    private static float envTemp = 36.6F;
    private static int damageCooldown = 0;

    private static final float BASE_CHANGE_RATE = 0.005F;
    private static final float MIN_BODY_TEMP = 25.0F;
    private static final float MAX_BODY_TEMP = 42.0F;

    private static int heatCacheTimer = 0;
    private static float cachedHeatBonus = 0.0F;
    private static boolean cachedSheltered = false;

    private static final String NBT_BODY_TEMP = "EW_BodyTemp";

    public static float getBodyTemp() { return bodyTemp; }
    public static float getEnvTemp() { return envTemp; }

    public static void tick(World world) {
        if (world == null || world.playerEntities.isEmpty()) return;

        EntityPlayer player = (EntityPlayer) world.playerEntities.get(0);
        if (player == null) return;

        if (heatCacheTimer <= 0) {
            cachedHeatBonus = calcHeatSources(world, player);
            cachedSheltered = isSheltered(world, player);
            heatCacheTimer = 20;
        }
        heatCacheTimer--;

        envTemp = calcEnvTemp(world, player, cachedHeatBonus, cachedSheltered);

        float insulation = getArmorInsulation(player);
        float changeRate = BASE_CHANGE_RATE * (1.0F - insulation * 0.5F);
        float targetTemp = envTemp + insulation * 8.0F;

        if (bodyTemp < targetTemp) {
            bodyTemp = Math.min(targetTemp, bodyTemp + changeRate);
        } else if (bodyTemp > targetTemp) {
            bodyTemp = Math.max(targetTemp, bodyTemp - changeRate);
        }

        if (cachedHeatBonus > 5.0F && bodyTemp < 36.6F) {
            bodyTemp = Math.min(36.6F, bodyTemp + 0.01F * (cachedHeatBonus / 10.0F));
        }

        if (bodyTemp > MAX_BODY_TEMP) bodyTemp = MAX_BODY_TEMP;
        if (bodyTemp < MIN_BODY_TEMP) bodyTemp = MIN_BODY_TEMP;

        if (damageCooldown > 0) damageCooldown--;

        if (bodyTemp < 30.0F && damageCooldown <= 0) {
            int damage = 1;
            if (bodyTemp < 28.0F) damage = 2;
            if (bodyTemp <= MIN_BODY_TEMP + 0.01F) damage = 3;

            player.attackEntityFrom(null, damage);
            damageCooldown = 60;
        }

        Days.saveDays(world);
    }

    public static void saveToNBT(EntityPlayer player, NBTTagCompound nbt) {
        nbt.setFloat(NBT_BODY_TEMP, bodyTemp);
    }

    public static void loadFromNBT(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt.hasKey(NBT_BODY_TEMP)) {
            bodyTemp = nbt.getFloat(NBT_BODY_TEMP);
            if (bodyTemp < MIN_BODY_TEMP) bodyTemp = MIN_BODY_TEMP;
            if (bodyTemp > MAX_BODY_TEMP) bodyTemp = MAX_BODY_TEMP;
        }
    }

    private static float calcEnvTemp(World world, EntityPlayer player, float heatBonus, boolean sheltered) {
        float temp = 36.6F;

        if (Snowstorm.isSnowing()) temp -= 20.0F;

        long dayTime = world.getWorldTime() % 24000L;
        if (dayTime >= 12000L && dayTime < 23000L) {
            temp -= 27.5F;
        }

        int y = (int) player.posY;
        if (y < 55) temp -= (55 - y) * 0.8F;

        int depth = getUndergroundDepth(world, player);
        if (depth > 0) {
            temp += Math.min(20.0F, depth * 2.0F);
        }

        if (player.isInWater()) temp -= 20.0F;

        temp += heatBonus;

        if (sheltered) temp += 5.0F;

        if (temp < -40.0F) temp = -40.0F;
        if (temp > 50.0F)  temp = 50.0F;
        return temp;
    }

    private static int getUndergroundDepth(World world, EntityPlayer player) {
        int px = (int) Math.floor(player.posX);
        int py = (int) Math.floor(player.posY) + 2;
        int pz = (int) Math.floor(player.posZ);

        int depth = 0;
        for (int i = 0; i < 20; i++) {
            int id = world.getBlockId(px, py + i, pz);
            if (id == 0) break;
            Block b = Block.blocksList[id];
            if (b == null || !b.blockMaterial.isSolid()) break;
            depth++;
        }
        return depth;
    }

    private static boolean isSheltered(World world, EntityPlayer player) {
        int px = (int) Math.floor(player.posX);
        int py = (int) Math.floor(player.posY);
        int pz = (int) Math.floor(player.posZ);

        boolean hasRoof = false;
        for (int i = 1; i <= 10; i++) {
            int id = world.getBlockId(px, py + i, pz);
            if (id != 0) {
                Block b = Block.blocksList[id];
                if (b != null && b.blockMaterial.isSolid()) {
                    hasRoof = true;
                    break;
                }
            }
        }
        if (!hasRoof) return false;

        int[][] sides = {{1,0},{-1,0},{0,1},{0,-1}};
        int solidWalls = 0;
        for (int[] s : sides) {
            for (int i = 1; i <= 3; i++) {
                int id = world.getBlockId(px + s[0]*i, py, pz + s[1]*i);
                if (id != 0) {
                    Block b = Block.blocksList[id];
                    if (b != null && b.blockMaterial.isSolid()) {
                        solidWalls++;
                        break;
                    }
                }
            }
        }
        return solidWalls >= 2;
    }

    private static float calcHeatSources(World world, EntityPlayer player) {
        int px = (int) player.posX;
        int py = (int) player.posY;
        int pz = (int) player.posZ;

        float heat = 0.0F;
        final int R = 6;

        for (int x = px - R; x <= px + R; x++) {
            for (int y2 = py - 3; y2 <= py + 3; y2++) {
                for (int z = pz - R; z <= pz + R; z++) {
                    int id = world.getBlockId(x, y2, z);
                    float strength = 0.0F;

                    if (id == Block.torchWood.blockID) {
                        strength = 10.0F;
                    } else if (id == Block.fire.blockID) {
                        strength = 25.0F;
                    } else if (id == Block.lavaStill.blockID || id == Block.lavaMoving.blockID) {
                        strength = 25.0F;
                    } else if (id == 62) {
                        strength = 8.0F;
                    }

                    if (strength > 0.0F) {
                        double dx = (px + 0.5) - (x + 0.5);
                        double dy = (py + 0.5) - (y2 + 0.5);
                        double dz = (pz + 0.5) - (z + 0.5);
                        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                        float falloff = (float) Math.max(0.0, 1.0 - dist / (R + 1));
                        heat += strength * falloff;
                    }
                }
            }
        }
        return Math.min(35.0F, heat);
    }

    private static float getArmorInsulation(EntityPlayer player) {
        float slowDown = 0.0F;

        ItemStack[] armor = player.inventory.armorInventory;
        for (int i = 0; i < armor.length; i++) {
            if (armor[i] == null) continue;
            int id = armor[i].itemID;

            if (id == 300 || id == 301 || id == 302 || id == 303) {
                slowDown += 0.625F;
            }
            else if (id == 298 || id == 299) {
                slowDown += 0.375F;
            }
            else if (id >= 304 && id <= 317) {
                slowDown += 0.10F;
            }
        }
        return Math.min(1.0F, slowDown);
    }
}
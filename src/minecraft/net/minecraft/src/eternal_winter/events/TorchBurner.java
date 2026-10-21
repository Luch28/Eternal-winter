package net.minecraft.src.eternal_winter.events;

import net.minecraft.src.Block;
import net.minecraft.src.World;

public class TorchBurner {

    private static final int MIN_BURN = 16800;
    private static final int MAX_BURN = 18000;

    public static void onTorchPlaced(World world, int x, int y, int z) {
        if (world.multiplayerWorld) return;
        world.setBlockMetadataWithNotify(x, y, z, 8);
        world.scheduleBlockUpdate(x, y, z, Block.torchWood.blockID, burnDelay(x, y, z));
    }

    public static void onTorchTick(World world, int x, int y, int z) {
        if (world.multiplayerWorld) return;
        if (world.getBlockId(x, y, z) != Block.torchWood.blockID) return;

        int meta = world.getBlockMetadata(x, y, z);

        if ((meta & 8) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, meta | 8);
            world.scheduleBlockUpdate(x, y, z, Block.torchWood.blockID, burnDelay(x, y, z));
            return;
        }
    }

    private static int burnDelay(int x, int y, int z) {
        int h = (x * 73856093) ^ (y * 19349663) ^ (z * 83492791);
        h = (h ^ (h >>> 13)) * 0x5bd1e995;
        h ^= h >>> 15;
        return MIN_BURN + (Math.abs(h) % (MAX_BURN - MIN_BURN + 1));
    }
}
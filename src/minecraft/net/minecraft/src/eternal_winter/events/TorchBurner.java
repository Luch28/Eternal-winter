package net.minecraft.src.eternal_winter.events;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.eternal_winter.Blocks;

public class TorchBurner {

    private static final Random RAND = new Random();
    private static final int MIN_BURN = 18000;
    private static final int MAX_BURN = 19200;

    public static void onTorchPlaced(World world, int x, int y, int z) {
        if (world.multiplayerWorld) return;
        int delay = MIN_BURN + RAND.nextInt(MAX_BURN - MIN_BURN + 1);
        world.scheduleBlockUpdate(x, y, z, Block.torchWood.blockID, delay);
    }

    public static void onTorchTick(World world, int x, int y, int z) {
        if (world.multiplayerWorld) return;
        if (world.getBlockId(x, y, z) == Block.torchWood.blockID) {
            int meta = world.getBlockMetadata(x, y, z);
            world.setBlockAndMetadataWithNotify(x, y, z, Blocks.torch.blockID, meta);
        }
    }
}
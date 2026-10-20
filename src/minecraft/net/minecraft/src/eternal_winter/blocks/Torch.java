package net.minecraft.src.eternal_winter.blocks;

import java.util.Random;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.World;

public class Torch extends BlockTorch {

    public Torch(int id, int tex) {
        super(id, tex);
        this.setHardness(0.0F);
        this.setLightValue(0.0F);
        this.setStepSound(soundWoodFootstep);
        this.setBlockName("burnt_torch");
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        return 100;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return 100;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
    }

    @Override
    public int idDropped(int meta, Random random) {
        return this.blockID;
    }

    @Override
    public int damageDropped(int meta) {
        return meta & 7;
    }
}
package net.minecraft.src.eternal_winter;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class Eternal_winter {
    public static final String VERSION = "0.0.1";

    public static void init() {
        Blocks.init();
        Items.init();
        Recipes.init();
        System.out.println("Eternal Winter!");
    }
}
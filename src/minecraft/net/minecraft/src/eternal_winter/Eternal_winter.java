package net.minecraft.src.eternal_winter;

import net.minecraft.src.World;
import net.minecraft.src.eternal_winter.events.*;

public class Eternal_winter {
    public static final String VERSION = "0.0.1";

    public static void init() {
        Blocks.init();
        Items.init();
        Recipes.init();
        System.out.println("Eternal Winter!");
    }

    public static void tick(World world) {
        if (world != null) {
            Snowstorm.tick(world);
            Temperature.tick(world);
        }
    }
}
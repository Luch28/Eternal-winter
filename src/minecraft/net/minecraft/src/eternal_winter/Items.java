package net.minecraft.src.eternal_winter;

import net.minecraft.src.Item;
import net.minecraft.src.eternal_winter.items.*;

public class Items {
    private static boolean initialized = false;

    public static Item quiver;

    public static void init() {
        if (initialized) return;
        initialized = true;
    }
}
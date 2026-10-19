package net.minecraft.src.eternal_winter;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;

public class Blocks {
    private static boolean initialized = false;

    public static Block glassTrapDoor;

    public static void init() {
        if (initialized) return;
        initialized = true;
    }
}
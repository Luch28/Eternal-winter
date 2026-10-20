package net.minecraft.src.eternal_winter;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import net.minecraft.src.eternal_winter.blocks.*;

public class Blocks {
    private static boolean initialized = false;

    public static Block torch;

    public static void init() {
        if (initialized) return;
        initialized = true;

        torch = new Torch(200, 100).setBlockName("glassTrapDoor");
        Block.blocksList[200] = torch;
        Item.itemsList[200] = new ItemBlock(200 - 256);
    }
}
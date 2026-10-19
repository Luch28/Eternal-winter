package net.minecraft.src.eternal_winter;

import net.minecraft.src.CraftingManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Block;
import net.minecraft.src.Item;

public class Recipes {
    public static void init() {
        CraftingManager.getInstance().addRecipe(
            new ItemStack(Blocks.glassTrapDoor, 2),
            new Object[] {
                "GGG",
                "GGG",
                Character.valueOf('G'), Block.glass
            }
        );
    }
}
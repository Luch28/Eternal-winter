package net.minecraft.src.eternal_winter;

import net.minecraft.src.Item;
import net.minecraft.src.eternal_winter.items.*;

public class Items {
    private static boolean initialized = false;

    public static Item quiver;
    
    public static Item warmHelmet;
    public static Item warmChestplate;
    public static Item warmLeggings;
    public static Item warmBoots;

    public static void init() {
        if (initialized) return;
        initialized = true;
        
        warmHelmet     = new WarmArmor(300, 0, 5, 0).setItemName("warmHelmet");
        warmChestplate = new WarmArmor(301, 0, 5, 1).setItemName("warmChestplate");
        warmLeggings   = new WarmArmor(302, 0, 5, 2).setItemName("warmLeggings");
        warmBoots      = new WarmArmor(303, 0, 5, 3).setItemName("warmBoots");
    }
}
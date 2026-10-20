package net.minecraft.src.eternal_winter.utils;

import net.minecraft.src.eternal_winter.events.*;

public class To_display {
	
	public static String getTime(net.minecraft.src.World world) {
        return Time.getTimeString(world);
    }
	
    public static String getDaysText(net.minecraft.src.World world) {
        return "Days: " + Days.getDaysPassed(world);
    }
    
    public static String getEnvTempText() {
        return String.format("Around: %.1f°C", Temperature.getEnvTemp());
    }
    
    public static String getBodyTempText() {
        return String.format("%.1f°C", Temperature.getBodyTemp());
    }
}
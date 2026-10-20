package net.minecraft.src.eternal_winter.events;

import net.minecraft.src.World;

public class Time {

    public static String getTimeString(World world) {
        long timeOfDay = world.getWorldTime() % 24000L;
        if (timeOfDay < 0) timeOfDay += 24000L;

        long totalMinutes = timeOfDay * 1440L / 24000L;

        totalMinutes = (totalMinutes + 360L) % 1440L;

        int hours   = (int)(totalMinutes / 60L);
        int minutes = (int)(totalMinutes % 60L);

        return String.format("%02d:%02d", hours, minutes);
    }
}
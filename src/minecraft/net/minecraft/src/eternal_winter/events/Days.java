package net.minecraft.src.eternal_winter.events;

import net.minecraft.src.World;
import java.io.*;

public class Days {
    private static final int TICKS_PER_DAY = 24000;
    private static final String SAVE_FILE = "eternal_winter_days.txt";

    public static int getDaysPassed(World world) {
        if (world == null) return 0;
        return (int)(world.getWorldTime() / TICKS_PER_DAY);
    }
    
    public static void saveDays(World world) {
        if (world == null) return;
        try {
            File file = new File(SAVE_FILE);
            FileWriter fw = new FileWriter(file);
            fw.write(String.valueOf(world.getWorldTime()));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long loadDays() {
        try {
            File file = new File(SAVE_FILE);
            if (!file.exists()) return 0;
            BufferedReader br = new BufferedReader(new FileReader(file));
            long time = Long.parseLong(br.readLine().trim());
            br.close();
            return time;
        } catch (Exception e) {
            return 0;
        }
    }
}
package tn.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tn.afkpool.AFKPool;

import java.io.File;
import java.io.IOException;

public class LocationAPI {

    private final AFKPool main ;
    private  final  File file ;
    private  final  FileConfiguration cfg;

    public LocationAPI(AFKPool main) {
        this.main = main;
        file = new File(main.getDataFolder(),"locations.yml");
        cfg = YamlConfiguration.loadConfiguration(file);

    }



    public boolean LocationExists(String location){
        return cfg.getConfigurationSection(location) != null;
    }

    public void setLocation(Location loc, String name) {
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        double yaw = loc.getYaw();
        double pitch = loc.getPitch();
        cfg.set(name + ".world", world);
        cfg.set(name + ".x", x);
        cfg.set(name + ".y", y);
        cfg.set(name + ".z", z);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String name) {
        try {
            String world = cfg.getString(name + ".world");
            double x = cfg.getDouble(name + ".x");
            double y = cfg.getDouble(name + ".y");
            double z = cfg.getDouble(name + ".z");
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            return loc;
        } catch (Exception e) {
            return null;	// TODO: handle exception
        }
    }



}

package tn.afkpool;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import tn.Event.InventoryClickEvent;
import tn.Manager.GameManager;
import tn.commands.afkpool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;

public final class AFKPool extends JavaPlugin {


    private static AFKPool instance;

    private GameManager manager;

    private final String version = "0.2.2";

    public static String Prefix = "&7[&aAFK Pool&7] ";
    private static Economy econ = null;



    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
       if (!setupEconomy()) {
            getLogger().info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Prefix = color(getConfig().getString("Prefix"));
        manager = new GameManager(this);
        getLogger().info("AFKPool v"+version+" has been enabled");
        getLogger().info("coded by SrKammounYT");
        getCommand("afkpool").setExecutor(new afkpool(manager));
        Bukkit.getPluginManager().registerEvents(new InventoryClickEvent(),this);

    }

    public String getVersion() {
        return version;
    }

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    @Override
    public void onDisable() {


    }

    public void reloadPlugin(){
        manager.stopGameRun();
        Prefix = color(getConfig().getString("Prefix"));
        manager = new GameManager(this);

    }
    public static AFKPool getInstance() {
        return instance;
    }
    public  Economy getEconomy() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}

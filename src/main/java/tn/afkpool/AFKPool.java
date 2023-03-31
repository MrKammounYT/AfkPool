package tn.afkpool;

import org.bukkit.Bukkit;
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

    private final String version = "0.2";
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
        manager = new GameManager(this);
        getLogger().info("AFKPool v"+version+" has been enabled");
        getLogger().info("coded by SrKammounYT");
        getCommand("afkpool").setExecutor(new afkpool(manager));
        Bukkit.getPluginManager().registerEvents(new InventoryClickEvent(),this);

    }

    public String getVersion() {
        return version;
    }

    public void registerAfkPoolMembers(){
        File tempFile = new File("afkpool-node",getConfig().getCurrentPath());
        if (!tempFile.exists()) {
            tempFile.getParentFile().mkdir();
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(tempFile);
        ArrayList<UUID> PoolMembers = new ArrayList<UUID>();
        for(Player pls: Bukkit.getOnlinePlayers()){
            if(manager.isInRegion(pls)){
                PoolMembers.add(pls.getUniqueId());
            }
        }
        config.set("PoolMember",PoolMembers);
    }

    @Override
    public void onDisable() {
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

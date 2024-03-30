package tn.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tn.afkpool.AFKPool;
import tn.utils.ItemClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class FileManager {


    private final FileConfiguration config;
    private final ItemsManager itemsManager;

    public FileManager(ItemsManager itemsManager,FileConfiguration configuration){
        this.config = configuration;
        this.itemsManager = itemsManager;
        load();

    }


    private void load(){
        if(config.getConfigurationSection("Items") ==null){
            Bukkit.getConsoleSender().sendMessage("[AFK POOL] Please setup items in your config.yml");
            return;
        }
        for(String i : config.getConfigurationSection("Items").getKeys(false)){
            String path = "Items."+i+".";
            Material material = Material.getMaterial(config.getString(path+"material").toUpperCase());
            if(material == null){
                Bukkit.getConsoleSender().sendMessage("[AFK POOL] invaldata material "+ config.getString(path+"material"));
                return;
            }
            int data = config.getInt(path +".data");
            int amount = config.getInt(path +".amount");
            int durability = config.getInt(path +".durability");
            ItemStack item = new ItemStack(material,amount,(short)data);
            ItemMeta meta = item.getItemMeta();
            if(durability != -1){
                item.setDurability((short)durability);
            }
            if(config.getString(path+"name") !=null){
                meta.setDisplayName(config.getString(path + "name").replace("&","§"));
            }
            ArrayList<String> newlore = new ArrayList<String>();
            for(String s : config.getStringList(path + "lore")){
                newlore.add(s.replace("&","§"));
            }
            meta.setLore(newlore);
            item.setItemMeta(meta);
            double chance = config.getDouble(path+"chance");
            itemsManager.itemStack.add(new ItemClass(item,chance));

        }
        Bukkit.getConsoleSender().sendMessage("[AFK POOL] Loaded "+itemsManager.itemStack.size() + " Items!");
    }

    public void addItem(ItemStack item,double chance){
        int lastint = 0;
        if(config.getConfigurationSection("Items") ==null){
            Bukkit.getConsoleSender().sendMessage("[AFK POOL] Please setup items in your config.yml");
            return;
        }
        for(String i : config.getConfigurationSection("Items").getKeys(false)){
            lastint = Integer.parseInt(i);
        }
        lastint +=1;
        config.set("Items."+lastint+".material",item.getType().toString());
        if(item.hasItemMeta()){
            config.set("Items."+lastint+".name",item.getItemMeta().getDisplayName());
            config.set("Items."+lastint+".lore",item.getItemMeta().getLore().toArray());
        }
        config.set("Items."+lastint+".amount",item.getAmount());
        config.set("Items."+lastint+".data",item.getData().getData());
        config.set("Items."+lastint+".durability",item.getDurability());
        config.set("Items."+lastint+".chance",chance);
        AFKPool.getInstance().saveConfig();
        itemsManager.itemStack.add(new ItemClass(item,chance));
    }

}

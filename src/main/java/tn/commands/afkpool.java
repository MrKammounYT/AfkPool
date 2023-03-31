package tn.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tn.Manager.GameManager;
import tn.afkpool.AFKPool;

import java.util.ArrayList;
import java.util.List;

public class afkpool implements CommandExecutor {


    private GameManager manager;

    public afkpool(GameManager manager){
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player))return true;

        Player player = (Player) sender;

        if(!player.hasPermission("afkpool.admin")){
            return true;
        }


        if(args.length == 2){
            if(args[0].equalsIgnoreCase("set")){
               if(args[1].equalsIgnoreCase("pos1")){
                   player.sendMessage("§aPos1 Has been set! ");
                   manager.getLocationAPI().setLocation(player.getLocation(),"pos1");

               }else if(args[1].equalsIgnoreCase("pos2")){
                   player.sendMessage("§aPos2 Has been set! ");
                   manager.getLocationAPI().setLocation(player.getLocation(),"pos2");

               }
               else {
                   player.sendMessage("§cPlease use /afkpool set [pos1/pos2]");
               }
            }
            else if(args[0].equalsIgnoreCase("additem")){
                String chance = args[1];
                manager.getFileManager().addItem(player.getItemInHand(),Double.valueOf(chance)/100);
                player.sendMessage(player.getItemInHand().getType() + " §ahas been added to the afkpool items with a chance of §e"+chance+"%");
            }
        }else if(args.length == 1){
           if(args[0].equalsIgnoreCase("items")){
                int bs = manager.getItemsManager().getItemStack().size()/9;
                Inventory inv = Bukkit.createInventory(null,(bs+1)*9,"§aAFK POOL Items");
                for(int i=0;i<manager.getItemsManager().getItemStack().size();i++){
                    ItemStack f =manager.getItemsManager().getItemStack().get(i).getItem();
                    ItemMeta meta = f.getItemMeta();
                    List<String> lore;
                    if(meta.hasLore()){
                        lore = meta.getLore();
                    }else{
                        lore = new ArrayList<>();
                    }
                    lore.add("§c   ");
                    lore.add("§6§lChance: §e"+Double.valueOf(manager.getItemsManager().getItemStack().get(i).getChance()*100).intValue()+"%");
                    meta.setLore(lore);


                    ItemStack newItemStack = new ItemStack(f);
                    newItemStack.setItemMeta(meta);
                    inv.setItem(i, newItemStack);
                }
                player.openInventory(inv);
            }
        }
        else{
            player.sendMessage("§9§m--------------------------------------------------");
            player.sendMessage("                    §eAfk Pool§7(§e"+ AFKPool.getInstance().getVersion()+"§7)");
            player.sendMessage("§e/afkpool set [pos1/pos2] §7(this will set the position for the afkpool)");
            player.sendMessage("§e/afkpool additem [chance] §7(this will add the item you curently holding)");
            player.sendMessage("§e/afkpool items §7(open the afkpool items inventory)");
            player.sendMessage("§9§m---------------------------------------------------");
        }

        return false;
    }
}

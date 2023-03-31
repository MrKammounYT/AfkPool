package tn.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if(e.getInventory() instanceof PlayerInventory)return;
        if(e.getView().getTitle() == null)return;
        if(e.getView().getTitle().equals("§aAFK POOL Items")){
            e.setCancelled(true);
        }

    }

}

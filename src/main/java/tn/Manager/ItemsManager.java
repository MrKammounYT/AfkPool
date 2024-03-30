package tn.Manager;

import org.bukkit.inventory.ItemStack;
import tn.utils.ItemClass;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ItemsManager {

    ArrayList<ItemClass> itemStack = new ArrayList<>();


    public ArrayList<ItemClass> getItemStack() {
        return itemStack;
    }

    public ItemStack getRandomItem(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(ItemClass item : itemStack){
            if(item.shouldBeGiven(random)){
                return item.getItem();
            }
        }
        return null;
    }

}

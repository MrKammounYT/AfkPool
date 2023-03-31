package tn.utils;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ItemClass {

    private final ItemStack item;

    private final double chance;


    public  ItemClass(ItemStack item,double chance){
        this.item = item;
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getItem() {
        return item;
    }
    public boolean shouldBeGiven(Random random){
        return random.nextDouble() < chance;
    }


}

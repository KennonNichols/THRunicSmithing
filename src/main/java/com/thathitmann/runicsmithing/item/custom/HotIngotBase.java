package com.thathitmann.runicsmithing.item.custom;

import net.minecraft.client.main.GameConfig;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class HotIngotBase extends SmithingChainItem {
    private RunicSmithingMaterial material;

    public HotIngotBase(Properties properties, RunicSmithingMaterial material) {
        super(properties);
        tooltip += "\nToo hot to handle! Use tongs or forge gloves to hold safely.";
        this.material = material;
    }

    public Item getCoolingResult() {return material.getAssociatedIngot();}






    //public RunicSmithingMaterial getMaterial() {return  material;}

    /*
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {




        if (!entity.isOnFire()){
            entity.lavaHurt();
        }





        super.inventoryTick(itemStack, level, entity, i, b);
    }*/




    /*
    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        ItemStack droppedIngot = new ItemStack(material.getAssociatedIngot());
        player.sendSystemMessage(Component.literal(String.format("The hot %s cools as you drop it.",material.getMaterialName())));
        item = droppedIngot;

        // = material.getAssociatedIngot();
        return super.onDroppedByPlayer(item, player);
    }*/



}

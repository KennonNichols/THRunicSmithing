package com.thathitmann.runicsmithing.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GlovesItem extends THRSItemBase {
    public GlovesItem(Properties properties) {
        super(properties);
        this.tooltip = "Allows you to hold hot metal safely when held anywhere in inventory.";
    }
}

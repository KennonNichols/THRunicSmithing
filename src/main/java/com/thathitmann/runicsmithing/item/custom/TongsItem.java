package com.thathitmann.runicsmithing.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;


import java.util.List;

public class TongsItem extends THRSItemBase {

    public TongsItem(Properties properties) {
        super(properties);
        this.tooltip = "Allows you to hold hot metal safely when held anywhere in inventory.";
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide()) {
            player.sendSystemMessage(Component.literal("*Click, clack*"));
        }
        return super.use(level, player, interactionHand);
    }


}

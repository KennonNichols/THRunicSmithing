package com.thathitmann.runicsmithing.item.custom;

import com.thathitmann.runicsmithing.item.custom.supers.THRSItemBase;
import com.thathitmann.runicsmithing.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TongsItem extends THRSItemBase {


    public TongsItem(Properties properties) {
        super(properties);
        this.tooltip = "Allows you to hold hot metal safely when held anywhere in inventory.";

    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide()) {
            player.getCooldowns().addCooldown(this, 10);
            level.playSeededSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.TONGS_CLICKING.get(), SoundSource.PLAYERS, 1f,1f,0);

            //player.sendSystemMessage(Component.literal("*Click, clack*"));
        }
        return super.use(level, player, interactionHand);
    }


}

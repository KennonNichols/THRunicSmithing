package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.HotIngotBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StoneHammerItem extends ForgeHammer {

    public StoneHammerItem(Properties properties) {
        super(properties);
        this.tooltip = "Hold in offhand with ingot in mainhand, and sneak-click on anvil to forge the ingot into a tool. Only works with stone anvil. ";
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        if (!level.isClientSide() && Objects.requireNonNull(player).getOffhandItem().getItem() instanceof ForgeHammer && player.getMainHandItem().getItem() instanceof HotIngotBase && (level.getBlockState(blockpos).is(ModBlocks.STONE_ANVIL_BLOCK.get()))) {
            advancedMode = false;
            NetworkHooks.openScreen(((ServerPlayer) player), this, player.blockPosition());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}

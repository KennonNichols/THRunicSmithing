package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class StoneHammerItem extends ForgeHammer {

    public StoneHammerItem(Properties properties) {
        super(properties);
        this.tooltip = "Hold in offhand with ingot in mainhand, and click on anvil to forge the ingot into a tool. Only works with stone anvil.";
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        if (!level.isClientSide() && player.getOffhandItem().getItem() instanceof ForgeHammer && (level.getBlockState(blockpos).is(ModBlocks.STONE_ANVIL_BLOCK.get()))) {
            NetworkHooks.openScreen(((ServerPlayer) player), this, player.blockPosition());
        }
        return InteractionResult.PASS;
    }

}

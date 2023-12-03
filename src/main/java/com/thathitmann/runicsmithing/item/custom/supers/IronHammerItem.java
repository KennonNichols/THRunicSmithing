package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.HotIngotBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class IronHammerItem extends ForgeHammer {
    public IronHammerItem(Properties properties) {
        super(properties);
        this.tooltip = "Hold in off hand with ingot in main hand, and sneak-click on anvil to forge the ingot into a tool. Gives an extra +3 quality when forged on a metal anvil.";
    }

    private final Block[] ironAnvilStates = new Block[] {
      Blocks.ANVIL,
      Blocks.CHIPPED_ANVIL,
      Blocks.DAMAGED_ANVIL
    };

    private boolean isBlockStateAnyIronAnvil(BlockState blockState) {
        for (Block block : ironAnvilStates) {
            if (blockState.is(block)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        if (player != null) {
            if (!level.isClientSide() && player.getOffhandItem().getItem() instanceof ForgeHammer && player.getMainHandItem().getItem() instanceof HotIngotBase) {
                if (level.getBlockState(blockpos).is(ModBlocks.STONE_ANVIL_BLOCK.get())) {
                    advancedMode = false;
                    NetworkHooks.openScreen(((ServerPlayer) player), this, player.blockPosition());
                }
                else if (isBlockStateAnyIronAnvil(level.getBlockState(blockpos))) {
                    advancedMode = true;
                    NetworkHooks.openScreen(((ServerPlayer) player), this, player.blockPosition());
                }
            }
        }
        return InteractionResult.PASS;
    }
}

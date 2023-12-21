package com.thathitmann.runicsmithing.block.custom;

import com.thathitmann.runicsmithing.block.entity.ModBlockEntities;
import com.thathitmann.runicsmithing.block.entity.ToolStationBlockEntity;
import com.thathitmann.runicsmithing.screen.ToolStationBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolStationBlock extends BaseEntityBlock {

    public ToolStationBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ToolStationBlockEntity(pPos, pState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider(
                (p_57074_, p_57075_, p_57076_) -> new ToolStationBlockMenu(p_57074_, p_57075_, pPos),
                Component.literal("Tool Station"));
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newState, boolean playerIsMoving) {

        if (blockState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ToolStationBlockEntity) {
                ((ToolStationBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(blockState, level, blockPos, newState, playerIsMoving);
    }



    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult p_60508_) {
        if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {

            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockEntity instanceof ToolStationBlockEntity toolStationEntity) {
                NetworkHooks.openScreen(((ServerPlayer) player),toolStationEntity, blockPos);
            } else {
                throw new IllegalStateException("Missing container provider!");
            }

            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;



        /*if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, blockPos));
            player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
            return InteractionResult.CONSUME;
        }*/



        //return super.use(state, level, blockPos, player, interactionHand, p_60508_);
    }



    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> tBlockEntityType) {
        return createTickerHelper(tBlockEntityType, ModBlockEntities.TOOL_STATION_BLOCK.get(),
                ToolStationBlockEntity::tick);
    }


}

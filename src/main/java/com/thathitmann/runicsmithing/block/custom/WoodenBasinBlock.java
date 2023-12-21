package com.thathitmann.runicsmithing.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WoodenBasinBlock extends Block {

    public WoodenBasinBlock(Properties properties) {
        super(properties);
    }


    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 8, 15);



    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return SHAPE;
    }



    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return (this.defaultBlockState().setValue(FILLED, false));
    }


    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (!level.isClientSide() && !blockState.getValue(FILLED)) {
            ItemStack heldItemStack = player.getMainHandItem();
            //Drain potion
            if (heldItemStack.getItem() == Items.POTION) {player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.GLASS_BOTTLE)); level.setBlock(blockPos, blockState.setValue(FILLED, true),3);}
            //Drain bucket
            else if (heldItemStack.getItem() == Items.WATER_BUCKET) {player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.BUCKET)); level.setBlock(blockPos, blockState.setValue(FILLED, true),3);}
            return InteractionResult.CONSUME;
        }





        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}

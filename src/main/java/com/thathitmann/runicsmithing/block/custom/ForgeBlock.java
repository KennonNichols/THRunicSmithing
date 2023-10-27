package com.thathitmann.runicsmithing.block.custom;

import com.thathitmann.runicsmithing.block.entity.ForgeBlockEntity;
import com.thathitmann.runicsmithing.block.entity.ModBlockEntities;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ForgeBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");



    public ForgeBlock(Properties properties) {
        super(properties);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return (this.defaultBlockState().setValue(FACING,blockPlaceContext.getHorizontalDirection().getOpposite())).setValue(LIT, false);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }


    //Block entity



    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }


    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean playerIsMoving) {

        if (blockState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ForgeBlockEntity) {
                ((ForgeBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(blockState, level, blockPos, newState, playerIsMoving);
    }



    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult p_60508_) {
        if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {

            BlockEntity blockEntity = level.getBlockEntity(blockPos);



            ItemStack heldItemStack = player.getMainHandItem();

            if (heldItemStack.getItem() == ModItems.CHARCOAL_BRIQUETTE.get()) {
                if (blockEntity instanceof ForgeBlockEntity) {
                    level.setBlock(blockPos, state.setValue(LIT, true),3);
                    level.playSeededSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f,1f,0);
                    ((ForgeBlockEntity) blockEntity).addFuel(400);
                    heldItemStack.setCount(heldItemStack.getCount()-1);
                }
            }
            else {
                if (blockEntity instanceof ForgeBlockEntity) {
                    NetworkHooks.openScreen(((ServerPlayer) player),(ForgeBlockEntity)blockEntity, blockPos);
                } else {
                    throw new IllegalStateException("Missing container provider!");
                }
            }











        }



        return super.use(state, level, blockPos, player, interactionHand, p_60508_);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ForgeBlockEntity(blockPos, blockState);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> tBlockEntityType) {
        return createTickerHelper(tBlockEntityType, ModBlockEntities.FORGE_BLOCK.get(),
                ForgeBlockEntity::tick);
    }


}

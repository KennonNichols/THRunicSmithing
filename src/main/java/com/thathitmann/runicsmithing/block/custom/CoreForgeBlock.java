package com.thathitmann.runicsmithing.block.custom;

import com.thathitmann.runicsmithing.block.entity.CoreForgeBlockEntity;
import com.thathitmann.runicsmithing.block.entity.ModBlockEntities;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoreForgeBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final IntegerProperty DEPTH = IntegerProperty.create("depth",0,3000);



    public CoreForgeBlock(Properties properties) {
        super(properties);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(FACING);
        builder.add(DEPTH);
        super.createBlockStateDefinition(builder);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {



        int depth = -blockPlaceContext.getClickedPos().getY();
        if (depth < 0 ) {
            depth = 0;
        } else if (depth > 3000) {
            depth = 3000;
        }



        return (this.defaultBlockState().setValue(FACING,blockPlaceContext.getHorizontalDirection().getOpposite())).setValue(LIT, false).setValue(DEPTH, depth);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }


    //Block entity



    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.MODEL;
    }


    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newState, boolean playerIsMoving) {

        if (blockState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CoreForgeBlockEntity) {
                ((CoreForgeBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(blockState, level, blockPos, newState, playerIsMoving);
    }



    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult p_60508_) {
        if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {







            BlockEntity blockEntity = level.getBlockEntity(blockPos);




            ItemStack heldItemStack = player.getMainHandItem();

            if (heldItemStack.getItem() == ModItems.CHARCOAL_BRIQUETTE.get()) {
                if (blockEntity instanceof CoreForgeBlockEntity) {
                    level.setBlock(blockPos, state.setValue(LIT, true),3);
                    level.playSeededSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f,1f,0);
                    ((CoreForgeBlockEntity) blockEntity).addFuel(400);
                    heldItemStack.setCount(heldItemStack.getCount()-1);
                }
            }
            else {
                if (blockEntity instanceof CoreForgeBlockEntity) {
                    NetworkHooks.openScreen(((ServerPlayer) player),(CoreForgeBlockEntity)blockEntity, blockPos);
                } else {
                    throw new IllegalStateException("Missing container provider!");
                }
            }











        }



        return super.use(state, level, blockPos, player, interactionHand, p_60508_);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CoreForgeBlockEntity(blockPos, blockState);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> tBlockEntityType) {
        return createTickerHelper(tBlockEntityType, ModBlockEntities.CORE_FORGE_BLOCK.get(),
                CoreForgeBlockEntity::tick);
    }


}

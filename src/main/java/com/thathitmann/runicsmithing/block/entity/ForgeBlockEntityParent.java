package com.thathitmann.runicsmithing.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.thathitmann.runicsmithing.block.custom.ForgeBlock.LIT;


public abstract class ForgeBlockEntityParent extends BlockEntity implements MenuProvider {



    protected final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Forge");
    }


    protected static void setToUnlit(Level level, BlockPos blockPos, BlockState blockState, boolean playSound) {
        if (playSound) {
            level.playSeededSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f, 0);
        }

        level.setBlock(blockPos, blockState.setValue(LIT, false), 3);
    }


    protected final ContainerData data;

    protected int progress = 0;
    protected int maxProgress = 80;
    protected int maxBurnTime = 2000;
    protected int burnTime = 0;
    protected static final Random rand = new Random();


    protected int heatPercentage = 0;

    public ForgeBlockEntityParent(BlockPos pos, BlockState state, BlockEntityType<?> blockEntityType) {
        super(blockEntityType, pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ForgeBlockEntityParent.this.progress;
                    case 1 -> ForgeBlockEntityParent.this.maxProgress;
                    case 2 -> ForgeBlockEntityParent.this.burnTime;
                    case 3 -> ForgeBlockEntityParent.this.maxBurnTime;
                    case 4 -> ForgeBlockEntityParent.this.heatPercentage;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ForgeBlockEntityParent.this.progress = value;
                    case 1 -> ForgeBlockEntityParent.this.maxProgress = value;
                    case 2 -> ForgeBlockEntityParent.this.burnTime = value;
                    case 3 -> ForgeBlockEntityParent.this.maxBurnTime = value;
                    case 4 -> ForgeBlockEntityParent.this.heatPercentage = value;
                }

            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return  lazyItemHandler.cast();
        }


        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setToUnlit(level, this.worldPosition, this.getBlockState(), false);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return super.getCapability(cap);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);

    }

    protected void resetProgress() {
        this.progress = 0;
    }


    public void addFuel(int i) {
        burnTime += i;
        if (burnTime > maxBurnTime) {
            burnTime = maxBurnTime;
        }
    }
}

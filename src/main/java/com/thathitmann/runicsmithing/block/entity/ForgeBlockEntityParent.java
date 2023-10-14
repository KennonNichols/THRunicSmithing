package com.thathitmann.runicsmithing.block.entity;

import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.item.custom.ForgeIngotLookup;
import com.thathitmann.runicsmithing.item.custom.HotIngotBase;
import com.thathitmann.runicsmithing.item.custom.RunicSmithingMaterial;
import com.thathitmann.runicsmithing.screen.CoreForgeBlockMenu;
import com.thathitmann.runicsmithing.screen.ForgeBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.thathitmann.runicsmithing.RunicSmithing.forgeableTag;
import static com.thathitmann.runicsmithing.block.custom.ForgeBlock.LIT;

public abstract class ForgeBlockEntityParent extends BlockEntity implements MenuProvider {



    protected final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    @Override
    public Component getDisplayName() {
        return Component.literal("Forge");
    }



    protected final ContainerData data;

    protected int progress = 0;
    protected int maxProgress = 80;
    protected int maxBurnTime = 2000;
    protected int burnTime = 0;
    protected static Random rand = new Random();


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
                };

            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return  lazyItemHandler.cast();
        }


        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
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

package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.block.entity.ForgeBlockEntity;
import com.thathitmann.runicsmithing.block.entity.ForgeBlockEntityParent;
import com.thathitmann.runicsmithing.screen.ForgeBlockMenu;
import com.thathitmann.runicsmithing.screen.HammeringMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ForgeHammer extends THRSItemBase implements MenuProvider {

    public boolean isAdvancedHammer = false;

    public ForgeHammer(Properties properties) {
        super(properties);
    }


    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return 0;
        }

        @Override
        public void set(int pIndex, int pValue) {

        }

        @Override
        public int getCount() {
            return 0;
        }
    };


    @Override
    public Component getDisplayName() {
        return Component.literal("Forging");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new HammeringMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}

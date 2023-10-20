package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.block.custom.WoodenBasinBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmithingChainItem extends Item {




    protected RunicSmithingMaterial material;


    protected String tooltip = "";



    public Aspect getNBTAspect(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null) {
            return null;
        }
        String name = tag.getString("runicsmithing.aspect_name");
        int quality = tag.getInt("runicsmithing.quality");
        return new Aspect(name, quality);
    }


    public CompoundTag buildNBTAspectTag(@NotNull ItemStack itemStack, int quality, @NotNull String description) {




        CompoundTag nbtData = new CompoundTag();

        Aspect currentAspect = getNBTAspect(itemStack);



        //Increment quality if it has a tag
        if (currentAspect != null) {
            int currentQuality = currentAspect.getQualityLevel();
            nbtData.putInt("runicsmithing.quality", currentQuality + quality);
        //Otherwise, just put quality in
        } else {
            nbtData.putInt("runicsmithing.quality", quality);
        }


        //Increment description if it has a tag
        if (currentAspect != null) {
            String currentDescription = currentAspect.getName();
            nbtData.putString("runicsmithing.aspect_name", currentDescription + description + "\n\n");
            //Otherwise, just put description in
        } else {
            nbtData.putString("runicsmithing.aspect_name", description + "\n\n");
        }




        //itemStack.setTag(nbtData);


        return nbtData;


    }




    public Item getCoolingResult() {return null;}

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        //player.sendSystemMessage(Component.literal(outputState()));
        return super.use(level, player, interactionHand);
    }

    public SmithingChainItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {



            components.add(Component.literal(tooltip).withStyle(ChatFormatting.DARK_PURPLE));
            Aspect aspect = ((SmithingChainItem)itemStack.getItem()).getNBTAspect(itemStack);
            if (aspect != null) {
                String outputString = "Forged item has the following modifiers:\n\n" + aspect.getName() + "Current quality bonus: " + aspect.getQualityLevel();
                components.add(Component.literal(outputString).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(itemStack, level, components, flag);

    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        if (!level.isClientSide() && player.getMainHandItem().getItem() == this) {

            BlockState interactedState = level.getBlockState(blockpos);

            //If clicking on a filled wooden basin
            if (((interactedState.is(ModBlocks.WOODEN_BASIN_BLOCK.get())))) {
                if (interactedState.getValue(WoodenBasinBlock.FILLED)) {
                    coolInMainHand(player);
                }
            }
            else if (interactedState.is(Blocks.WATER_CAULDRON)) {
                coolInMainHand(player);
            }
        }
        return InteractionResult.PASS;
    }



    private void coolInMainHand(Player player) {
        player.getInventory().setItem(player.getInventory().selected, new ItemStack(this.getCoolingResult(), player.getMainHandItem().getCount()));
    }



}

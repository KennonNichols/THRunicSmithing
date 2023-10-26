package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.block.custom.WoodenBasinBlock;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SmithingChainItem extends Item {




    protected RunicSmithingMaterial material;


    protected String tooltip = "";

    private final static Aspect quenchAspect = new Aspect("Quenched for +1 quality.", 1);
    private final static Aspect temperAspect = new Aspect("Tempered for +2 quality.", 2);


    public static int getTemperCount(@NotNull ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getTag()).getInt("runicsmithing.temper_count");
    }

    public static CompoundTag incrementTemperCount(CompoundTag tag) {
        tag.putInt("runicsmithing.temper_count", tag.getInt("runicsmithing.temper_count") + 1);
        return tag;
    }




    public static Aspect getNBTAspect(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null) {
            return null;
        }
        String name = tag.getString("runicsmithing.aspect_name");
        int quality = tag.getInt("runicsmithing.quality");
        return new Aspect(name, quality);
    }

    public static CompoundTag addNBTAspectTag(@NotNull CompoundTag tag, @Nullable Aspect aspect) {
        if (aspect != null) {
            return addNBTAspectTag(tag, aspect.getQualityLevel(), aspect.getName());
        }
        return tag;
    }

    public static CompoundTag addNBTAspectTag(@NotNull CompoundTag tag, @Nullable Integer quality, String description) {
        if (quality != null) {
            tag.putInt("runicsmithing.quality", tag.getInt("runicsmithing.quality") + quality);
        }
        if (description != null) {
            tag.putString("runicsmithing.aspect_name", tag.getString("runicsmithing.aspect_name") + description + "\n\n");
        }
        return tag;
    }




    public Item getCoolingResult() {
        if (RSDynamicRecipeRegistry.isItemAValidInput(this, RSRecipeCategory.QUENCHING)) {
            return RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.QUENCHING, this);
        }
        return null;
    }

    public RunicSmithingMaterial getMaterial() {return material;}

    public SmithingChainItem(Properties properties, RunicSmithingMaterial material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {



            components.add(Component.literal(tooltip).withStyle(ChatFormatting.DARK_PURPLE));
            Aspect aspect = SmithingChainItem.getNBTAspect(itemStack);
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
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        if (!level.isClientSide() && player != null) {
            if (player.getMainHandItem().getItem() == this) {
                BlockState interactedState = level.getBlockState(blockpos);

                //If clicking on a filled wooden basin
                if (((interactedState.is(ModBlocks.WOODEN_BASIN_BLOCK.get())))) {
                    if (interactedState.getValue(WoodenBasinBlock.FILLED)) {
                        coolInMainHand(player, quenchAspect);
                    }
                } else if (interactedState.is(Blocks.WATER_CAULDRON)) {
                    coolInMainHand(player, temperAspect);
                }
            }
        }
        return InteractionResult.PASS;
    }



    private void coolInMainHand(Player player, Aspect quenchAspect) {
        Item coolingResult = this.getCoolingResult();

        if (coolingResult != null) {

            ItemStack inputItemStack = player.getMainHandItem();
            ItemStack outputItemStack = new ItemStack(coolingResult, inputItemStack.getCount());


            //If it's a smithing chain item, transfer tags
            if (inputItemStack.getItem() instanceof SmithingChainItem) {
                outputItemStack.setTag(inputItemStack.getTag());
            }

            //If it's a hot tool, and has less than 3 tempers, temper
            if (inputItemStack.getItem() instanceof HotForgedToolBase && SmithingChainItem.getTemperCount(inputItemStack) < 3 && quenchAspect != null && outputItemStack.getTag() != null) {
                CompoundTag tag = SmithingChainItem.incrementTemperCount(outputItemStack.getTag());
                tag = SmithingChainItem.addNBTAspectTag(tag, quenchAspect);
                outputItemStack.setTag(tag);
            }






            player.getInventory().setItem(player.getInventory().selected, outputItemStack);
        }
    }



}

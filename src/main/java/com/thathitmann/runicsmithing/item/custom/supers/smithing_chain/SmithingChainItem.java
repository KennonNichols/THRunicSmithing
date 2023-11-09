package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain;

import com.thathitmann.runicsmithing.block.ModBlocks;
import com.thathitmann.runicsmithing.block.custom.WoodenBasinBlock;
import com.thathitmann.runicsmithing.generators.GeneratedItemRegistry;
import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import com.thathitmann.runicsmithing.generators.RSRecipeCategory;
import com.thathitmann.runicsmithing.item.custom.supers.Aspect;
import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import com.thathitmann.runicsmithing.item.custom.supers.THRSItemBase;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.forged_tool.ForgedTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SmithingChainItem extends THRSItemBase {


    protected String tooltip = "";

    private static Aspect quenchAspect(int temperCount) {
        return new Aspect(
                String.format("Quenched %s %s for +1 quality.",
                        temperCount,
                        //If the count is greater than one, do "times"
                        (temperCount > 1) ? "times" : "time"),
                1
        );
    }
    private static Aspect temperAspect(int temperCount) {
        return new Aspect(
                String.format("Tempered %s %s for +2 quality.",
                        temperCount,
                        //If the count is greater than one, do "times"
                        (temperCount > 1) ? "times" : "time"),
                2
        );
    }

    public static int getTemperCount(@NotNull ItemStack itemStack) {
        return Objects.requireNonNull(itemStack.getTag()).getInt("runicsmithing.temper_count");
    }

    public static CompoundTag incrementTemperCount(@Nullable CompoundTag tag) {
        if (tag == null) {
            tag = new CompoundTag();
        }
        tag.putInt("runicsmithing.temper_count", tag.getInt("runicsmithing.temper_count") + 1);
        return tag;
    }
    public static void addToNBTAspectTag(@Nullable CompoundTag tag, @NotNull Aspect aspect) {
        addToNBTAspectTag(tag, aspect.qualityLevel(), aspect.name());
    }

    public static void addToNBTAspectTag(@Nullable CompoundTag tag, @Nullable Integer quality, @Nullable String description) {
        if (tag == null) {
            tag = new CompoundTag();
        }
        CompoundTag aspectTag = tag.getCompound("runicsmithing.aspect");
        int currentQuality = aspectTag.getInt("quality");
        String currentName = aspectTag.getString("name");


        if (quality != null) {
            aspectTag.putInt("quality", currentQuality + quality);
        }
        if (description != null) {
            aspectTag.putString("name", currentName + description + "\n\n");
        }


        tag.put("runicsmithing.aspect", aspectTag);
    }




    public Item getCoolingResult() {
        if (RSDynamicRecipeRegistry.isItemAValidInput(this, RSRecipeCategory.QUENCHING)) {
            return RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.QUENCHING, this);
        }
        return null;
    }

    public SmithingChainItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {



            components.add(Component.literal(tooltip).withStyle(ChatFormatting.DARK_PURPLE));
            CompoundTag aspect = itemStack.getTag().getCompound("runicsmithing.aspect");
            String outputString = "Forged item has the following modifiers:\n\n" + aspect.getString("name");
            components.add(Component.literal(outputString).withStyle(ChatFormatting.LIGHT_PURPLE));
            components.add(Component.literal("Current quality bonus: " + aspect.getInt("quality")).withStyle(ChatFormatting.LIGHT_PURPLE));
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



                //region quenching
                //If clicking on a filled wooden basin
                if (((interactedState.is(ModBlocks.WOODEN_BASIN_BLOCK.get())))) {
                    if (interactedState.getValue(WoodenBasinBlock.FILLED)) {
                        coolInMainHand(player, false);
                    }
                }
                else if (interactedState.is(Blocks.WATER_CAULDRON)) {
                    coolInMainHand(player, true);
                }

                //endregion


                //region grinding

                //If clicking on a grindstone
                if (((interactedState.is(Blocks.GRINDSTONE)))) {
                   sharpenInMainHand(player);
                }

                //endregion


            }
        }
        return InteractionResult.PASS;
    }


    private void sharpenInMainHand(Player player) {
        if (this instanceof ToolBase) {
            ItemStack inputItemStack = player.getMainHandItem();

            //If we are only doing one, just change the held item
            if (inputItemStack.getCount() == 1) {
                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS, 1f, 1f, 0);



                Item outputItem = RSDynamicRecipeRegistry.getRecipeResult(RSRecipeCategory.SHARPENING, inputItemStack.getItem());
                ItemStack outputItemStack = new ItemStack(outputItem, 1);



                //Transfer the tag, but get rid of the aspect name
                CompoundTag tag = inputItemStack.getTag();
                tag.getCompound("runicsmithing.aspect").putString("name", "Forged tool.");
                outputItemStack.setTag(tag);
                //Set name
                outputItemStack.setHoverName(Component.literal(String.format(GeneratedItemRegistry.getGeneratableItem(outputItemStack.getItem()).formatableName(), StringUtils.capitalize(RunicSmithingMaterial.values()[tag.getInt("runicsmithing.material")].getMaterialName()))));
                //Load attributes
                ((ForgedTool)outputItem).reloadItemDamageStats(outputItemStack);

                player.getInventory().setItem(player.getInventory().selected, outputItemStack);
            }

        }
    }

    private void coolInMainHand(Player player, Boolean useTemperAspect) {
        Item coolingResult = this.getCoolingResult();
        if (coolingResult != null) {
            player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1f,1f,0);

            ItemStack inputItemStack = player.getMainHandItem();
            ItemStack outputItemStack = new ItemStack(coolingResult, inputItemStack.getCount());


            //If it's a smithing chain item, transfer tags, and rename
            if (inputItemStack.getItem() instanceof SmithingChainItem) {
                outputItemStack.setTag(inputItemStack.getTag());
                outputItemStack.setHoverName(Component.literal(String.format(GeneratedItemRegistry.getGeneratableItem(coolingResult).formatableName(), StringUtils.capitalize(RunicSmithingMaterial.values()[inputItemStack.getTag().getInt("runicsmithing.material")].getMaterialName()))));
            }

            //If it's a hot tool, and has less than 3 tempers, temper

            int temperCount = SmithingChainItem.getTemperCount(inputItemStack);

            if (inputItemStack.getItem() instanceof HotToolBase && temperCount < 3) {

                Aspect quenchAspect;

                if (useTemperAspect) {
                    quenchAspect = temperAspect(temperCount + 1);
                }
                else {
                    quenchAspect = quenchAspect(temperCount + 1);
                }



                CompoundTag tag = SmithingChainItem.incrementTemperCount(outputItemStack.getTag());
                SmithingChainItem.addToNBTAspectTag(tag, quenchAspect);
                outputItemStack.setTag(tag);
            }






            player.getInventory().setItem(player.getInventory().selected, outputItemStack);
        }
    }



}

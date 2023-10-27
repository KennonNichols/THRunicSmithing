package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgedTool extends THRSItemBase {


    public ForgedTool(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            if (tooltip != null) {
                components.add(Component.literal(tooltip).withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (itemStack.getTag().getInt("runicsmithing.quality") != 0) {
                String outputString = "Quality: " + itemStack.getTag().getInt("runicsmithing.quality");
                components.add(Component.literal(outputString).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(itemStack, level, components, flag);
    }



    public static void applyMaterial(RunicSmithingMaterial material, ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        tag.putString("runicsmithing:material", material.getMaterialName());
        itemStack.setTag(tag);
    }

    private static RunicSmithingMaterial getMaterial(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        String materialKey = tag.getString("runicsmithing:material");
        return MaterialRegistry.getMaterialFromRegistry(materialKey);
    }






    /*
    @Nullable
    private static Component getName(String baseKey, RunicSmithingMaterial material) {
        // if there is a specific name, use that
        String fullKey = String.format("%s.%s.%s", baseKey, location.getNamespace(), location.getPath());
        if (Util.canTranslate(fullKey)) {
            return new TranslatableComponent(fullKey);
        }
        // try material name prefix next
        String materialKey = MaterialTooltipCache.getKey(material);
        String materialPrefix = materialKey + ".format";
        if (Util.canTranslate(materialPrefix)) {
            return new TranslatableComponent(materialPrefix, new TranslatableComponent(baseKey));
        }
        // format as "<material> <item name>"
        if (Util.canTranslate(materialKey)) {
            return new TranslatableComponent(TooltipUtil.KEY_FORMAT, new TranslatableComponent(materialKey), new TranslatableComponent(baseKey));
        }
        return null;
    }*/


    @Override
    public Component getName(ItemStack stack) {
        RunicSmithingMaterial material = getMaterial(stack);


        // if no material, fallback to "forged tool"
        if (material == null) {
            return Component.literal("Forged Tool");
        }
        else {
            return Component.literal(material.getMaterialName());
        }
    }


}

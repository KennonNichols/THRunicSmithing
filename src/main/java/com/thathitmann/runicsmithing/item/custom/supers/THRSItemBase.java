package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class THRSItemBase extends Item {

    public THRSItemBase(Properties properties) {
        super(properties);
    }

    //region ToolTip
    protected String tooltip;
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (tooltip != null) {
            if (Screen.hasShiftDown()) {
                components.add(Component.literal(tooltip).withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.AQUA));
            }
            super.appendHoverText(itemStack, level, components, flag);
        }
    }
    //endregion



}

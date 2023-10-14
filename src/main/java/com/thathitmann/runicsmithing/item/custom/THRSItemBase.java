package com.thathitmann.runicsmithing.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class THRSItemBase extends Item {

    public THRSItemBase(Properties properties) {
        super(properties);
    }

    //region ToolTip
    protected String tooltip = "";
    protected String baseTooltip = "";
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (tooltip != "" || baseTooltip != "") {
            if (Screen.hasShiftDown()) {
                components.add(Component.literal(baseTooltip + tooltip).withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.AQUA));
            }
            super.appendHoverText(itemStack, level, components, flag);
        }
    }
    //endregion



}

package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain;

import net.minecraft.world.item.Item;

public class ToolBase extends SmithingChainItem {
    public ToolBase(Item.Properties properties) {
        super(properties);
        this.tooltip = "Reheat and cool up to 3 times for a quality bonus, or right-click on a grindstone to complete (Make sure you are holding just one).";
    }


}

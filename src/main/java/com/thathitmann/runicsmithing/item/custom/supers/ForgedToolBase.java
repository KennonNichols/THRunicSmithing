package com.thathitmann.runicsmithing.item.custom.supers;

public class ForgedToolBase extends SmithingChainItem {
    public ForgedToolBase(Properties properties, RunicSmithingMaterial material) {
        super(properties, material);
        this.tooltip = "Reheat to temper for a quality bonus, or right-click on a grindstone to complete (Make sure you are holding just one).";
    }
}

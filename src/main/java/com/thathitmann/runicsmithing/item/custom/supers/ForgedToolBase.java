package com.thathitmann.runicsmithing.item.custom.supers;

public class ForgedToolBase extends SmithingChainItem {
    private final ToolType type;
    public ForgedToolBase(Properties properties, RunicSmithingMaterial material, ToolType type) {
        super(properties, material);
        this.type = type;
        this.tooltip = "Reheat to temper for a quality bonus, or right-click on a grindstone to complete (Make sure you are holding just one).";
    }

    public ToolType getToolType() {return type;}

}

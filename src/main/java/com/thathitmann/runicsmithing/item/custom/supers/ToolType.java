package com.thathitmann.runicsmithing.item.custom.supers;

import com.thathitmann.runicsmithing.generators.RSDynamicRecipeRegistry;
import org.apache.commons.lang3.StringUtils;

public enum ToolType {
    PICKAXE("pickaxe", RSDynamicRecipeRegistry.PICKAXE_SHAPE),
    AXE("axe", RSDynamicRecipeRegistry.AXE_SHAPE),
    SHOVEL("shovel", RSDynamicRecipeRegistry.SHOVEL_SHAPE),
    HOE("hoe", RSDynamicRecipeRegistry.HOE_SHAPE),
    SWORD("sword", RSDynamicRecipeRegistry.SWORD_SHAPE);



    private ToolType(String name, ForgeLevel[] shape) {
        this.name = name;
        this.shape = shape;
    }
    private final String name;
    private final ForgeLevel[] shape;
    public String getName() {
        return name;
    }
    public String getCapitalizedName() {
        return StringUtils.capitalize(name);
    }
    public ForgeLevel[] getShape() {
        return shape;
    }

}

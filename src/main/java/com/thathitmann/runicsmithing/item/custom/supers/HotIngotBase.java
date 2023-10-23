package com.thathitmann.runicsmithing.item.custom.supers;


import net.minecraft.world.item.Item;

public class HotIngotBase extends SmithingChainItem {
    public HotIngotBase(Properties properties, RunicSmithingMaterial material) {
        super(properties, material);
        this.tooltip = "Too hot to handle! Use tongs or forge gloves to hold safely. Throw or dunk in a filled basin or cauldron to cool.";
    }

    @Override
    public Item getCoolingResult() {return material.getAssociatedIngot();}
}

package com.thathitmann.runicsmithing.item.custom.supers;

public class HotForgedToolBase extends SmithingChainItem{

    public HotForgedToolBase(Properties properties, RunicSmithingMaterial material) {
        super(properties, material);
        this.tooltip = "Right-click on a filled basin or cauldron to cool. Cool and reheat up to 3 times for a quality bonus each time.";
    }

}

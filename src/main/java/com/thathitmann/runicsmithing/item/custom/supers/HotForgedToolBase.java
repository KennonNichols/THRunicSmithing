package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class HotForgedToolBase extends SmithingChainItem{

    TagKey<Item> toolTag;


    public HotForgedToolBase(Properties properties, RunicSmithingMaterial material, TagKey<Item> toolTag) {
        super(properties, material);
        this.tooltip = "Too hot to handle! Use tongs or forge gloves to hold safely. Throw or dunk in a filled basin or cauldron to cool. Cool and reheat up to 3 times for a quality bonus each time.";
        this.toolTag = toolTag;
        //Tags.Items.TOOLS
    }

}

package com.thathitmann.runicsmithing.item.custom;

public class SmithingChainItem extends THRSItemBase {
    private int quality = 0;


    public int getQuality() {return quality;}



    public SmithingChainItem(Properties properties) {
        super(properties);
        tooltip = String.format("Current quality bonus: %d", quality);
    }
}

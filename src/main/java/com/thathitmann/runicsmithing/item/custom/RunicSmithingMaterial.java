package com.thathitmann.runicsmithing.item.custom;

import net.minecraft.world.item.Item;

public class RunicSmithingMaterial {
    private Item associatedIngot;
    private String materialName;


    public Item getAssociatedIngot() {return associatedIngot;}
    public String getMaterialName() {return materialName;}

    public RunicSmithingMaterial(Item associatedIngot, String materialName){
        this.associatedIngot = associatedIngot;
        this.materialName = materialName;
    }


}

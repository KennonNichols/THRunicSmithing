package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.world.item.Item;

public class RunicSmithingMaterial {
    private final Item associatedIngot;
    private final String materialName;


    public Item getAssociatedIngot() {return associatedIngot;}
    public String getMaterialName() {return materialName;}

    public RunicSmithingMaterial(Item associatedIngot, String materialName){
        this.associatedIngot = associatedIngot;
        this.materialName = materialName;
    }


}

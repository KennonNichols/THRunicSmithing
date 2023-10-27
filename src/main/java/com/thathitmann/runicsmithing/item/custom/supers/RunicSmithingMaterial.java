package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.world.item.Item;

public class RunicSmithingMaterial {
    private final Item associatedIngot;
    private final String materialName;
    private final Boolean isPrimitive;

    public String getMaterialName() {return materialName;}
    public Item getAssociatedIngot() {return associatedIngot;}
    public Boolean getPrimitive() {return isPrimitive;}

    public RunicSmithingMaterial(Item associatedIngot, String materialName, Boolean isPrimitive){
        this.associatedIngot = associatedIngot;
        this.materialName = materialName;
        this.isPrimitive = isPrimitive;
    }


}

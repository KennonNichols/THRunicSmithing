package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers;

import net.minecraft.nbt.CompoundTag;

public abstract class ToolModifier {

    public final String name;
    public final String description;



    public static final String ASPECT_NAME = "aspect";


    protected ToolModifier(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract String getTagTypeTitle();

    public abstract CompoundTag getAsTag();


}

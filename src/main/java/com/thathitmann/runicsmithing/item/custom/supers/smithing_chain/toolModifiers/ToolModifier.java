package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers;

import net.minecraft.nbt.CompoundTag;

public abstract class ToolModifier {

    public static final String ASPECT_NAME = "aspect";

    public abstract String getTagTypeTitle();

    public abstract CompoundTag getAsTag();


}

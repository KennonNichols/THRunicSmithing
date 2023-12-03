package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers;

import net.minecraft.nbt.CompoundTag;



/**
 * Contains two keys:
 * quality
 * name
 *
 * Identified as:
 * aspect
 * */
public class AspectModifier extends ToolModifier {
    public final String name;
    public final int quality;
    public AspectModifier(String name, int quality) {
        this.name = name;
        this.quality = quality;
    }

    public static AspectModifier fromTag(CompoundTag tag) {
        return new AspectModifier(
                tag.getString("name"),
                tag.getInt("quality")
        );
    }


    public String getTagTypeTitle() {return ASPECT_NAME;};

    @Override
    public CompoundTag getAsTag() {
        CompoundTag aspectTag = new CompoundTag();
        aspectTag.putInt("quality", quality);
        aspectTag.putString("name", name);
        return aspectTag;
    }
}

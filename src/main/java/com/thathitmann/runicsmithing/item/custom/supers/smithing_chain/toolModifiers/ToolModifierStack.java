package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToolModifierStack {
    private final List<ToolModifier> modifierStack;

    public final static String TOOL_MODIFIER_STACK_TAG_ID = "runicsmithing.tool_modifier";
    private final static String TAG_NAME = "name";
    private final static String TAG_ID = "modifier_tag";

    public final static String QUICKGRAB_TAG_ID = "runicsmithing.quickgrab";
    public final static String QUICKGRAB_QUALITY_TAG_ID = "quality";
    public final static String QUICKGRAB_MATERIAL_TAG_ID = "material";

    private ToolModifierStack(List<ToolModifier> modifierStack) {
        this.modifierStack = modifierStack;
    }

    public static void loadQuickGrab(ItemStack itemStack) {
        CompoundTag mainTag = itemStack.getTag();
        if (mainTag == null) {return;}
        CompoundTag quickgrabTag = mainTag.getCompound(QUICKGRAB_TAG_ID);
        if (quickgrabTag == null) {
            quickgrabTag = new CompoundTag();
        }
        quickgrabTag.putInt(QUICKGRAB_QUALITY_TAG_ID, buildFromTag(mainTag.getCompound(TOOL_MODIFIER_STACK_TAG_ID)).getQualityTotal());
    }

    private int getQualityTotal() {
        int q = 0;
        for (ToolModifier modifier : modifierStack) {
            if (modifier instanceof AspectModifier aspectModifier) {
                q += aspectModifier.quality;
            }
        }
        return q;
    }

    /**
     * Takes any tag, adds the modifier, and places it in the outTag
     * */
    public static void addToolModifierAndTransferNBT(@Nullable CompoundTag inTag, @NotNull CompoundTag outTag, @NotNull ToolModifier modifier) {
        if (inTag == null) {
            inTag = new CompoundTag();
        }
        ToolModifierStack stack = ToolModifierStack.buildFromTag(inTag.getCompound(ToolModifierStack.TOOL_MODIFIER_STACK_TAG_ID));
        stack.addToolModifier(modifier);
        outTag.put(ToolModifierStack.TOOL_MODIFIER_STACK_TAG_ID, stack.getAsTag());
    }

    public void addToolModifier(ToolModifier modifier) {
        modifierStack.add(modifier);
    }

    public String getWritableList() {
        StringBuilder aspectBuilder = new StringBuilder();
        boolean first = true;
        for (ToolModifier modifier : modifierStack) {
            if (first) {
                first = false;
            }
            else {
                aspectBuilder.append("\n");
            }

            if (modifier instanceof AspectModifier aspectModifier) {
                aspectBuilder.append(aspectModifier.name);
            }
        }

        return aspectBuilder + "\n" + "Total quality: " + getQualityTotal();
    }


    @Contract("_ -> new")
    public static @NotNull ToolModifierStack buildFromTag(@Nullable CompoundTag tag) {
        if (tag == null) {
            return new ToolModifierStack(new ArrayList<>());
        }

        List<ToolModifier> modifiers = new ArrayList<>();
        int i = 0;
        CompoundTag internalModifierTag;
        String internalModifierTagName;
        while (true) {
            if (tag.contains(Integer.toString(i))) {
                internalModifierTag = tag.getCompound(Integer.toString(i));
                internalModifierTagName = internalModifierTag.getString(TAG_NAME);
                CompoundTag internalTag = internalModifierTag.getCompound(TAG_ID);
                //Create and add the modifier using the tag. Switches along internalModifierTagName to remember the tag type
                switch (internalModifierTagName) {
                    case ToolModifier.ASPECT_NAME -> modifiers.add(AspectModifier.fromTag(internalTag));

                }
                i++;
            }
            else {
                break;
            }
        }
        return new ToolModifierStack(modifiers);
    }



    public CompoundTag getAsTag() {
        CompoundTag outTag = new CompoundTag();
        CompoundTag singleModifierTag;
        int i = 0;
        for (ToolModifier modifier : modifierStack) {
            //Creates a tag that exists to label which type of modifier it is
            singleModifierTag = new CompoundTag();
            singleModifierTag.putString(TAG_NAME, modifier.getTagTypeTitle());
            singleModifierTag.put(TAG_ID, modifier.getAsTag());
            //Puts that tag in, and just indexes it
            outTag.put(
                    Integer.toString(i),
                    singleModifierTag
            );
            i++;
        }
        return outTag;
    }


}

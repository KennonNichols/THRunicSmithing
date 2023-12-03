package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.forged_tool;

import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import net.minecraft.world.item.ItemStack;

public interface ForgedTool {
    default float getStatMultiplierFromQuality(int quality) {
        return quality / 16f + 0.5f;
    }

    default int getEnchantabilityFromItemStack(ItemStack itemStack) {
        return getMaterialFromItemStack(itemStack).getEnchantmentValue();
    }

    default int getQualityFromItemstack(ItemStack itemStack) {
        return itemStack.getTag().getCompound(ToolModifierStack.QUICKGRAB_TAG_ID).getInt(ToolModifierStack.QUICKGRAB_QUALITY_TAG_ID);
    }

    default float getSpeedFromItemStack(ItemStack itemStack) {
       return getMaterialFromItemStack(itemStack).getSpeed() * getStatMultiplierFromQuality(getQualityFromItemstack(itemStack));
    }

    default RunicSmithingMaterial getMaterialFromItemStack(ItemStack itemStack) {
        int materialKey = itemStack.getTag().getCompound(ToolModifierStack.QUICKGRAB_TAG_ID).getInt(ToolModifierStack.QUICKGRAB_MATERIAL_TAG_ID);
        return RunicSmithingMaterial.values()[materialKey];
    }

    default float getBaselineAttackFromItemStack(ItemStack itemStack, float toolAttackMultiplier) {
        return getMaterialFromItemStack(itemStack).getAttackDamageBonus() * toolAttackMultiplier * getStatMultiplierFromQuality(getQualityFromItemstack(itemStack));
    }

    void reloadItemDamageStats(ItemStack itemStack);

}

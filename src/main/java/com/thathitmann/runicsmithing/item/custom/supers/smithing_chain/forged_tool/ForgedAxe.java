package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.forged_tool;

import com.thathitmann.runicsmithing.item.custom.supers.RunicSmithingMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ForgedAxe extends AxeItem implements ForgedTool {


    public ForgedAxe(Properties properties) {
        super(RunicSmithingMaterial.NONE, 0, 0, properties.defaultDurability(RunicSmithingMaterial.getMaxUses()));
    }


    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            String outputString = "Quality: " + getQualityFromItemstack(itemStack);
            components.add(Component.literal(outputString).withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(itemStack, level, components, flag);
    }
    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return getEnchantabilityFromItemStack(stack);
    }
    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, @NotNull ItemStack pRepair) {
        pToRepair.setRepairCost(0);
        return  getMaterialFromItemStack(pToRepair).getRepairIngredient().test(pRepair);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        float ConsumeChance = (float) RunicSmithingMaterial.getMaxUses() / (float) getMaterialFromItemStack(stack).getUses();
        while (ConsumeChance > 1) {
            ConsumeChance -= 1;
            amount += 1;
        }
        if (ConsumeChance > entity.getRandom().nextFloat() && ConsumeChance < 1) {
            amount += 1;
        }
        if (amount >= (stack.getMaxDamage() - stack.getDamageValue()) && entity instanceof Player player) {
            player.playNotifySound(SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 0.8F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
            amount = (stack.getMaxDamage() - stack.getDamageValue()) - 1;
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }
    @Override
    public void reloadItemDamageStats(ItemStack itemStack) {
        float attackDamageBaseline = getBaselineAttackFromItemStack(itemStack, 2.5f);
        itemStack.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamageBaseline, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        itemStack.addAttributeModifier(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 1.0f, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        if ((pStack.getMaxDamage() - pStack.getDamageValue()) <= 1) {return false;}
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    //Stats
    public float getDestroySpeed(ItemStack pStack, @NotNull BlockState pState) {
        if ((pStack.getMaxDamage() - pStack.getDamageValue()) <= 1) {return 0;}
        return pState.is(BlockTags.MINEABLE_WITH_AXE) ? (getSpeedFromItemStack(pStack)) : 1.0F;
    }

    // FORGE START
    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_AXE) && net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getMaterialFromItemStack(stack), state);
    }
}

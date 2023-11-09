package com.thathitmann.runicsmithing.item.custom.supers;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public enum RunicSmithingMaterial implements Tier {


    NONE(Items.BRICK, "none_material", false,
            new Color(1, 1, 1),
            new Color(1, 1, 1),
            1, 0.0f, 0.0f, 0, 10, () -> Ingredient.of(Items.BRICK)
            ),
    COPPER(Items.COPPER_INGOT,"copper", true,
            new Color(232, 130, 100),
            new Color(221, 192, 100),
            200, 5.0f, 2.5f, 1, 10, () -> Ingredient.of(Items.COPPER_INGOT)),
    IRON(Items.IRON_INGOT,"iron", false,
            new Color(235, 235, 235),
            new Color(221, 192, 100),
            250, 6.0f, 3.0f, 2, 14, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD(Items.GOLD_INGOT,"gold", false,
            new Color(252, 249, 161),
            new Color(221, 192, 100),
            32, 12.0f, 2.0f, 0, 22, () -> Ingredient.of(Items.GOLD_INGOT)),
    DIAMOND(Items.DIAMOND,"diamond", false,
            new Color(139, 247, 228),
            new Color(56, 238, 214),
            1561, 8.0f, 3.5f, 3, 10, () -> Ingredient.of(Items.DIAMOND)),
    // ( ͡° ͜ʖ ͡°)
    //MERIUM(Items.BLAZE_ROD,"merium",
    //        new Color(176, 46, 46),
    //        new Color(221, 192, 100)),
    NETHERITE(Items.NETHERITE_INGOT,"netherite", false,
            new Color(94, 89, 91),
            new Color(156, 98, 188),
            2031, 9.0f, 5.0f, 4, 15, () -> Ingredient.of(Items.NETHERITE_INGOT));








    RunicSmithingMaterial(Item associatedIngot, String materialName, Boolean isPrimitive, Color baseColor, Color hotColor, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.associatedIngot = associatedIngot;
        this.materialName = materialName;
        this.isPrimitive = isPrimitive;

        this.baseColor = baseColor;
        this.hotOverlayColor = hotColor;

        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.level = level;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient.get();
    }



    public static List<RunicSmithingMaterial> getNonNoneMaterials() {
        List<RunicSmithingMaterial> materials = new ArrayList<>();
        for (RunicSmithingMaterial material : values()) {
            if (material != NONE) {
                materials.add(material);
            }
        }
        return materials;
    }


    private final Item associatedIngot;
    private final String materialName;
    private final Boolean isPrimitive;
    private final Color baseColor;
    @SuppressWarnings("FieldCanBeLocal")
    private final Color hotOverlayColor;



    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int level;
    private final int enchantmentValue;
    private final Ingredient repairIngredient;



    public String getMaterialName() {return materialName;}
    public Item getAssociatedIngot() {return associatedIngot;}
    public Boolean getPrimitive() {return isPrimitive;}
    public Color getBaseColor() {return baseColor;}


    public static RunicSmithingMaterial getAssociatedMaterial(Item ingot) {
        for (RunicSmithingMaterial material : RunicSmithingMaterial.getNonNoneMaterials()) {
            if (material.getAssociatedIngot() == ingot) {
                return material;
            }
        }
        return null;
    }

    public static int getMaxUses() {
        return Arrays.stream(values()).max(Comparator.comparing(RunicSmithingMaterial::getUses)).get().getUses();
    }









    //Stats
    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return repairIngredient;
    }
}

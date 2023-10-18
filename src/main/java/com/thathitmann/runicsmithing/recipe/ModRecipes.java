package com.thathitmann.runicsmithing.recipe;

import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RunicSmithing.MOD_ID);



    public static final RegistryObject<RecipeSerializer<ForgeRecipe>> FORGE_RECIPE_SERIALIZER =
            SERIALIZERS.register("forge_heating", () -> ForgeRecipe.Serializer.INSTANCE);




    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}

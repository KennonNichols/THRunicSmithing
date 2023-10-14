package com.thathitmann.runicsmithing.screen;

import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, RunicSmithing.MOD_ID);



    public static final RegistryObject<MenuType<ForgeBlockMenu>> FORGE_BLOCK_MENU =
            registerMenuType(ForgeBlockMenu::new, "forge_block_menu");
    public static final RegistryObject<MenuType<CoreForgeBlockMenu>> CORE_FORGE_BLOCK_MENU =
            registerMenuType(CoreForgeBlockMenu::new, "core_forge_block_menu");




    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}

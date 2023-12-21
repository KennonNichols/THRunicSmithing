package com.thathitmann.runicsmithing.block.entity;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RunicSmithing.MOD_ID);








    public static final RegistryObject<BlockEntityType<CoreForgeBlockEntity>> CORE_FORGE_BLOCK =
            BLOCK_ENTITIES.register("core_forge_block", () ->
                    BlockEntityType.Builder.of(CoreForgeBlockEntity::new,
                            ModBlocks.CORE_FORGE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ForgeBlockEntity>> FORGE_BLOCK =
            BLOCK_ENTITIES.register("forge_block", () ->
                    BlockEntityType.Builder.of(ForgeBlockEntity::new,
                            ModBlocks.FORGE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ToolStationBlockEntity>> TOOL_STATION_BLOCK =
            BLOCK_ENTITIES.register("tool_station_block", () ->
                    BlockEntityType.Builder.of(ToolStationBlockEntity::new,
                            ModBlocks.TOOL_STATION_BLOCK.get()).build(null));








    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }



}

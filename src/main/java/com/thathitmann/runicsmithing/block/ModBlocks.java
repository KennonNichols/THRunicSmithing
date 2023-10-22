package com.thathitmann.runicsmithing.block;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.block.custom.CoreForgeBlock;
import com.thathitmann.runicsmithing.block.custom.ForgeBlock;
import com.thathitmann.runicsmithing.block.custom.StoneAnvilBlock;
import com.thathitmann.runicsmithing.block.custom.WoodenBasinBlock;
import com.thathitmann.runicsmithing.item.ModCreativeTab;
import com.thathitmann.runicsmithing.item.ModItems;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    //region Bookkeeping and registry
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RunicSmithing.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends  Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
    //endregion

    public static final RegistryObject<Block> FORGE_BLOCK = registerBlock("forge_block",
            () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(ForgeBlock.LIT) ? 15 : 0)));

    public static final RegistryObject<Block> CORE_FORGE_BLOCK = registerBlock("core_forge_block",
            () -> new CoreForgeBlock(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(CoreForgeBlock.LIT) ? 15 : 0)));

    public static final RegistryObject<Block> STONE_ANVIL_BLOCK = registerBlock("stone_anvil_block",
            () -> new StoneAnvilBlock(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> WOODEN_BASIN_BLOCK = registerBlock("wooden_basin_block",
            () -> new WoodenBasinBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));


    /*
    public static final RegistryObject<Block> CARVING_TABLE_BLOCK = registerBlock("carving_table_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTab.TAB);
    public static final RegistryObject<Block> WRAPPING_TABLE_BLOCK = registerBlock("wrapping_table_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops()),
            ModCreativeTab.TAB);
     */



}

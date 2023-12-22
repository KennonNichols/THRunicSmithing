package com.thathitmann.runicsmithing.item.custom;

import com.thathitmann.runicsmithing.event.ModEvents;
import com.thathitmann.runicsmithing.item.ModItems;
import com.thathitmann.runicsmithing.item.custom.supers.THRSItemBase;
import com.thathitmann.runicsmithing.networking.ModMessages;
import com.thathitmann.runicsmithing.networking.packet.RuneKnowledgeDataSyncS2CPacket;
import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledgeProvider;
import com.thathitmann.runicsmithing.runes.Quest;
import com.thathitmann.runicsmithing.screen.ResearchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class ResearchTabletItem extends THRSItemBase implements MenuProvider {


    public ResearchTabletItem(Properties properties) {
        super(properties);
        this.tooltip = "Used to learn new words for engraving.";

    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && interactionHand == InteractionHand.MAIN_HAND) {
            NetworkHooks.openScreen((serverPlayer), this, player.blockPosition());
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        AtomicBoolean isComplete = new AtomicBoolean(false);

        Player player = pContext.getPlayer();



        pContext.getPlayer().getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(playerRuneKnowledge -> {
            BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
            if (state.is(Blocks.ENCHANTING_TABLE) && playerRuneKnowledge.getCurrentQuest() instanceof Quest.EatRuneQuest eatRuneQuest) {
                float power = getEnchantmentPowerBonus(pContext.getLevel(), pContext.getClickedPos());


                if (power < eatRuneQuest.requiredPower) {
                    pContext.getPlayer().sendSystemMessage(Component.literal("This enchanting table is too weak to learn anything."));
                }
                else {
                    if (!ModEvents.isPlayerEatingRunes(pContext.getPlayer())) {
                        ModEvents.startEatingRunes(eatRuneQuest.requiredPower, pContext.getClickedPos(), pContext.getPlayer());
                    }
                }





                isComplete.set(true);
            }
        });
        if (isComplete.get()) {
            return InteractionResult.CONSUME;
        }
        return super.useOn(pContext);
    }

    private float getEnchantmentPowerBonus(Level level, BlockPos pos) {
        float j = 0;
        for(BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
            if (EnchantmentTableBlock.isValidBookShelf(level, pos, blockpos)) {
                j += level.getBlockState(pos.offset(blockpos)).getEnchantPowerBonus(level, pos.offset(blockpos));
            }
        }
        return j;
    }

    @Override
    public @NotNull Component getDisplayName() {return Component.literal("Research");}
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {

        //Sync data from server before opening
        if (pPlayer instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(runeKnowledge -> {
                ModMessages.sendToPlayer(new RuneKnowledgeDataSyncS2CPacket(runeKnowledge.getKnownCharacters(), serverPlayer), serverPlayer);
            });
        }

        return new ResearchMenu(pContainerId, pPlayerInventory);
    }
}

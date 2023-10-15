package com.thathitmann.runicsmithing.event;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.item.custom.supers.HotIngotBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import static com.thathitmann.runicsmithing.RunicSmithing.burningHotTag;
import static com.thathitmann.runicsmithing.RunicSmithing.heatInsulatingTag;


@Mod.EventBusSubscriber(modid = RunicSmithing.MOD_ID)
public class ModEvents {



    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if (player.getInventory().contains(burningHotTag)) {
                if(!player.getInventory().contains(heatInsulatingTag)) {
                    if (!player.isOnFire()) {
                        player.setSecondsOnFire(1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityToss(ItemTossEvent event) {
        //if (event.side == LogicalSide.SERVER) {}
        ItemStack droppedItemStack = event.getEntity().getItem();
        Item droppedItem = droppedItemStack.getItem();
        if (droppedItemStack.is(burningHotTag)) {

            //ItemStack changedItem = droppedItem.getItem()
            if (droppedItem instanceof HotIngotBase) {

                Item changedItem = ((HotIngotBase) droppedItem).getCoolingResult();
                ItemStack changedItemStack = new ItemStack(changedItem);

                changedItemStack.setCount(droppedItemStack.getCount());

                event.getEntity().setItem(changedItemStack);
            }
        }
    }





}

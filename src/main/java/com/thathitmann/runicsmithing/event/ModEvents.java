package com.thathitmann.runicsmithing.event;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.item.custom.supers.HotIngotBase;
import com.thathitmann.runicsmithing.item.custom.supers.SmithingChainItem;
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


    //Cool any burning hot item. This is an anti-troll measure.
    @SubscribeEvent
    public static void onEntityToss(ItemTossEvent event) {
        //if (event.side == LogicalSide.SERVER) {}
        ItemStack droppedItemStack = event.getEntity().getItem();
        if (droppedItemStack.is(burningHotTag)) {

            Item droppedItem = droppedItemStack.getItem();
            //ItemStack changedItem = droppedItem.getItem()
            if (droppedItem instanceof SmithingChainItem) {

                Item changedItem = ((SmithingChainItem) droppedItem).getCoolingResult();
                if (changedItem != null) {
                    ItemStack changedItemStack = new ItemStack(changedItem);

                    changedItemStack.setCount(droppedItemStack.getCount());

                    event.getEntity().setItem(changedItemStack);
                }
            }
        }
    }





}

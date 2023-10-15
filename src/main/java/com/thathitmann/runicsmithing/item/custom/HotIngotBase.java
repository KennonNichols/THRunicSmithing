package com.thathitmann.runicsmithing.item.custom;


import net.minecraft.world.item.Item;

public class HotIngotBase extends SmithingChainItem {
    private final RunicSmithingMaterial material;

    public HotIngotBase(Properties properties, RunicSmithingMaterial material) {
        super(properties);
        this.tooltip = "Too hot to handle! Use tongs or forge gloves to hold safely.";
        this.material = material;
    }

    public Item getCoolingResult() {return material.getAssociatedIngot();}






    //public RunicSmithingMaterial getMaterial() {return  material;}

    /*
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {




        if (!entity.isOnFire()){
            entity.lavaHurt();
        }





        super.inventoryTick(itemStack, level, entity, i, b);
    }*/




    /*
    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        ItemStack droppedIngot = new ItemStack(material.getAssociatedIngot());
        player.sendSystemMessage(Component.literal(String.format("The hot %s cools as you drop it.",material.getMaterialName())));
        item = droppedIngot;

        // = material.getAssociatedIngot();
        return super.onDroppedByPlayer(item, player);
    }*/



}

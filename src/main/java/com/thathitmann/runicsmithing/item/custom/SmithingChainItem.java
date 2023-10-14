package com.thathitmann.runicsmithing.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SmithingChainItem extends THRSItemBase {
    private int quality = 0;

    private List<Aspect> aspects = new ArrayList<Aspect>();



    public int getQuality() {return quality;}
    public List<Aspect> getAspects() {return aspects;}


    public void addAspect(Aspect aspect) {
        aspects.add(aspect);
        quality += aspect.getQualityLevel();


        buildTooltip();
    }

    public void addAspects(List<Aspect> aspects) {
        for(Aspect aspect: aspects) {
            addAspect(aspect);
        }
    }


    public SmithingChainItem(Properties properties) {
        super(properties);
        buildTooltip();
    }

    private void buildTooltip() {

        tooltip = "";

        for (Aspect aspect : aspects) {
            tooltip += aspect.getName() + "\n\n";
        }



        tooltip += "Current quality bonus: ";
        tooltip += quality;



    }



}

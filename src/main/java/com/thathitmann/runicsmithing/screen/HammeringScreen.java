package com.thathitmann.runicsmithing.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class HammeringScreen extends AbstractContainerScreen<HammeringMenu> {





    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/hammering_gui.png");





    int xStart;
    int yStart;








    public HammeringScreen(HammeringMenu hammeringMenu, Inventory inventory, Component component) {
        super(hammeringMenu, inventory, component);
    }



    @Override
    protected void init() {
        super.init();
        xStart = (width - imageWidth) / 2;
        yStart = (height - imageHeight) / 2;
        for (int i = 0; i < 9; i++) {
            addButtonRelatedToIndex(i);
        }
    }


    @Override
    public void onClose() {
        menu.attemptToCraft();
        super.onClose();
    }

    private void addButtonRelatedToIndex(int index) {
        int xShift = (52 * (index % 3));
        int yShift = (52 * (index / 3));

        net.minecraft.client.gui.components.AbstractButton newButton = new AbstractButton(xStart + 11 + xShift, yStart + 7 + yShift, 50, 50, Component.literal("")) {
            @Override
            public void onPress() {
                smithCellAtIndex(index);
            }

            @Override
            protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

            }
        };

        addWidget(newButton);
    }



    private void smithCellAtIndex(int index) {
        menu.tryReduceButtonState(index);
    }


    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {}


    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        pGuiGraphics.blit(TEXTURE, xStart, yStart, 0, 0, imageWidth, imageHeight);
        renderCells(pGuiGraphics);
    }



    private void renderCells(GuiGraphics pGuiGraphics) {
        for (int i = 0; i < 9; i++) {
            int xShift = (52 * (i % 3));
            int yShift = (52 * (i / 3));
            pGuiGraphics.blit(TEXTURE, xStart + 11 + xShift, yStart + 7 + yShift, 176, 50 * menu.getButtonState(i), 50, 50);
        }
    }


}

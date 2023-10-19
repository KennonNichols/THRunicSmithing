package com.thathitmann.runicsmithing.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class HammeringScreen extends AbstractContainerScreen<HammeringMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/hammering_gui.png");



    public HammeringScreen(HammeringMenu hammeringMenu, Inventory inventory, Component component) {
        super(hammeringMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        //renderTooltip(poseStack, mouseX, mouseY);
    }
}

package com.thathitmann.runicsmithing.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoreForgeBlockScreen extends AbstractContainerScreen<CoreForgeBlockMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/core_forge_block_gui.png");



    public CoreForgeBlockScreen(CoreForgeBlockMenu coreForgeBlockMenu, Inventory inventory, Component component) {
        super(coreForgeBlockMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }



    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(poseStack, x, y);
        renderHeatGuage(poseStack, x, y);
        renderBurningField(poseStack, x, y);
        
    }

    private void renderHeatGuage(PoseStack poseStack, int x, int y) {
        blit(poseStack, x + 84, y + 78 - menu.getScaledHeat(), 176, 28 + 64 - menu.getScaledHeat(), 18, menu.getScaledHeat());
    }

    private void renderBurningField(PoseStack poseStack, int x, int y) {
        blit(poseStack, x + 42, y + 68 - menu.getScaledBurnTime(), 176, 125 - menu.getScaledBurnTime(), 16, menu.getScaledBurnTime());
    }

    private void renderProgressArrow(PoseStack poseStack, int x, int y) {
        if (menu.isCrafting()) {
            blit(poseStack, x + 135, y + 33, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}

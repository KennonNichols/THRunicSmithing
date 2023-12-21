package com.thathitmann.runicsmithing.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifier;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers.ToolModifierStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ToolStationBlockScreen extends AbstractContainerScreen<ToolStationBlockMenu> {

    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    //private static final int RECIPES_COLUMNS = 1;
    //private static final int RECIPES_ROWS = 6;
    //private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    //private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 72;
    private static final int RECIPES_X = 52;
    private static final int RECIPES_Y = 14;

    private float scrollOffs;
    /** Is {@code true} if the player clicked on the scroll wheel in the GUI. */
    private boolean scrolling;

    private int startIndex;


    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/tool_station_block_gui.png");

    public ToolStationBlockScreen(ToolStationBlockMenu toolStationBlockMenu, Inventory inventory, Component component) {
        super(toolStationBlockMenu , inventory, component);
        this.menu.blockEntity.setScreen(this);
        //toolStationBlockMenu.registerUpdateListener(this);
    }

    protected int imageHeight = 198;


    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, -10, 4210752, false);
        ItemStack itemStack = this.menu.getItemStackInMainSlot();
        if (menu.getDisplayingInfo()) {
            pGuiGraphics.drawString(this.font, itemStack.getDisplayName().getString(45).replaceAll("[\\[,\\]]",""), 28, 1, 4210752, false);
            pGuiGraphics.drawString(this.font, Integer.toString(itemStack.getOrCreateTag().getCompound(ToolModifierStack.QUICKGRAB_TAG_ID).getInt(ToolModifierStack.QUICKGRAB_QUALITY_TAG_ID)), 28, 10, 11217579, false);

        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.renderBackground(pGuiGraphics);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //Draw the GUI
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        //Draw the scrollbar
        int i = this.leftPos;
        int j = this.topPos;
        int k = (int)(58.0F * this.scrollOffs);
        pGuiGraphics.blit(TEXTURE, i + 101, j + 21 + k, 224 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
        //int l = this.leftPos + 52;
        //int i1 = this.topPos + 14;
        //int j1 = this.startIndex + 12;

        if (menu.getDisplayingInfo()) {
            renderToolModifiers(pGuiGraphics);
            renderEngravingButtons(pGuiGraphics);
        }

        //pGuiGraphics.blit(x, y, 0, 0, imageWidth, imageHeight);
        //renderProgressArrow(pGuiGraphics, x, y);
        //renderHeatGuage(pGuiGraphics, x, y);
        //renderBurningField(pGuiGraphics, x, y);
        
    }

    private void renderEngravingButtons(GuiGraphics pGuiGraphics) {
        //for (int i = this.startIndex; i < ; ++l)
    }


    /**
     * Called when a mouse button is clicked within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pMouseX the X coordinate of the mouse.
     * @param pMouseY the Y coordinate of the mouse.
     * @param pButton the button that was clicked.
     */

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        if (this.menu.getDisplayingInfo()) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;

            for(int l = this.startIndex; l < k; ++l) {
                int i1 = l - this.startIndex;
                double d0 = pMouseX - (double)(i + i1 % 4 * 16);
                double d1 = pMouseY - (double)(j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 9;
            if (pMouseX >= (double)i && pMouseX < (double)(i + 12) && pMouseY >= (double)j && pMouseY < (double)(j + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    /**
     * Called when the mouse is dragged within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pMouseX the X coordinate of the mouse.
     * @param pMouseY the Y coordinate of the mouse.
     * @param pButton the button that is being dragged.
     * @param pDragX the X distance of the drag.
     * @param pDragY the Y distance of the drag.
     */
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 52;
            this.scrollOffs = ((float)pMouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    /**
     * Called when the mouse wheel is scrolled within the GUI element.
     * <p>
     * @return {@code true} if the event is consumed, {@code false} otherwise.
     * @param pMouseX the X coordinate of the mouse.
     * @param pMouseY the Y coordinate of the mouse.
     * @param pDelta the scrolling delta.
     */
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)pDelta / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5D);
        }

        return true;
    }

    //16:216
    private void renderToolModifiers(GuiGraphics pGuiGraphics) {
        ToolModifierStack stack = this.menu.getEntityToolModifierStack();
        List<ToolModifier> list = stack.getModifierList();


        int x = this.leftPos + 8;
        int y = this.topPos + 21;

        for(int i = this.startIndex; i < this.startIndex + 6 && i < stack.getModifierCount(); ++i) {
            int j = i - this.startIndex;
            int i1 = y + j * 12;
            //Drawing toolmodifier backdrop and label
            pGuiGraphics.blit(TEXTURE, x, i1, 16, 216, 91, 12);
            pGuiGraphics.drawString(this.font, list.get(i).name, x + 2, i1 + 2, 6831720, false);
        }

    }

    protected int getOffscreenRows() {
        return this.menu.getEntityToolModifierStack().getModifierCount() - 6;
        //return (this.menu.getEntityToolModifierStack().getModifierCount() + 3) / 4 - 3;
    }

    private boolean isScrollBarActive() {
        return menu.getDisplayingInfo() && this.menu.getEntityToolModifierStack().getModifierCount() > 6;
    }




    /**
     * Called every time this screen's container is changed (is marked as dirty).
     */
    public void containerChanged() {
        this.scrollOffs = 0.0F;
        this.startIndex = 0;
    }



}

package com.thathitmann.runicsmithing.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledgeProvider;
import com.thathitmann.runicsmithing.runes.RuneTranslationList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchScreen extends AbstractContainerScreen<ResearchMenu> {


    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/research_tablet_gui.png");





    int xStart;
    int yStart;

    protected int imageWidth = 102;
    protected int imageHeight = 250;


    private @NotNull SelectionState selectionState = ResearchScreen.SelectionState.HOVERING;
    private DisplayMessage displayMessage = new DisplayMessage("", null);

    public ResearchScreen(ResearchMenu researchMenu, Inventory inventory, Component component) {
        super(researchMenu, inventory, component);
    }


    private enum RuneButton {
        A('a', 15, 29),
        B('b', 15, 53),
        C('c', 15, 77),
        D('d', 15, 101),
        E('e', 15, 125),
        F('f', 15, 149),
        G('g', 15, 173),
        H('h', 15, 197),
        I('i', 39, 5),
        J('j', 39, 29),
        K('k', 39, 53),
        L('l', 39, 77),
        M('m', 39, 101),
        N('n', 39, 125),
        O('o', 39, 149),
        P('p', 39, 173),
        Q('q', 39, 197),
        R('r', 39, 221),
        S('s', 63, 29),
        T('t', 63, 53),
        U('u', 63, 77),
        V('v', 63, 101),
        W('w', 63, 125),
        X('x', 63, 149),
        Y('y', 63, 173),
        Z('z', 63, 197),


        ;
        public final char character;
        public final int x;
        public final int y;

        RuneButton(char character, int x, int y) {

            this.character = character;
            this.x = x;
            this.y = y;
        }
    }

    private final List<AbstractButton> buttons = new ArrayList<>();


    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        xStart = (width - imageWidth) / 2;
        yStart = (height - imageHeight) / 2;
        for (RuneButton runeButton : RuneButton.values()) {
            addButtonRelatedToRune(runeButton);
        }
    }


    @Override
    public void onClose() {
        super.onClose();
    }

    private void addButtonRelatedToRune(RuneButton runeButton) {

        char runeCharacter = runeButton.character;
        //RuneTranslationList.RuneWord word = RuneTranslationList.RuneWord.getWordFromCharacter(runeCharacter);
        //RuneTranslationList.Rune rune = RuneTranslationList.getRuneFromCharacter(runeCharacter);

        AbstractButton newButton = new AbstractButton(xStart + runeButton.x, yStart + runeButton.y, 24, 24, Component.literal("")) {

            @Override
            public void onPress() {
                //menu.player.sendSystemMessage(Component.literal("(the player should not know that they chose " + word.name() + " (" + rune.getAssociatedWord() + ")) length is:" + word.getLength()));

                //Shows only what portion of the word the player knows
                menu.player.getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(playerRuneKnowledge -> {
                    //Don't set the word if we already know it.
                    if (!playerRuneKnowledge.isWordKnown(runeCharacter)) {
                        playerRuneKnowledge.setCurrentLearningCharacter(runeCharacter);
                        selectionState = SelectionState.SELECTED;
                        displayMessage = new DisplayMessage(
                                playerRuneKnowledge.getKnownWord(runeCharacter),
                                playerRuneKnowledge.getCurrentQuest().message
                        );
                        playerRuneKnowledge.syncData(menu.player);
                    }
                });
            }

            @Override
            protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

            }


            @Override
            public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
                pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), runeButton.x + (this.isHovered() ? 162: 88), runeButton.y, 24, 24);
                //If we haven't already selected a word, update based on hovered word
                if (isHovered && selectionState != SelectionState.SELECTED) {
                    //Shows only what portion of the word the player knows
                    menu.player.getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(playerRuneKnowledge -> {

                        //If we know it...
                        if (playerRuneKnowledge.isWordKnown(runeCharacter)) {
                            //Display the runic word and translation
                            displayMessage = new DisplayMessage(
                                    playerRuneKnowledge.getKnownWord(runeCharacter),
                                    RuneTranslationList.RuneWord.getWordFromCharacter(runeCharacter).name()
                            );
                            selectionState = SelectionState.HOVERING;
                        }
                        //Otherwise...
                        else {
                            displayMessage = new DisplayMessage(
                                    playerRuneKnowledge.getKnownWord(runeCharacter),
                                    null
                            );
                            selectionState = SelectionState.UNKNOWN_HOVERING;
                        }
                    });
                }
            }
        };
        buttons.add(newButton);
        addWidget(newButton);
    }




    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {}


    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        //pGuiGraphics.blit(TEXTURE, xStart, yStart, 0, 0, imageWidth, imageHeight);
        //pGuiGraphics.blit(TEXTURE, 0, 0, 0, 0, 102, 270);


        this.renderBackground(pGuiGraphics);

        //Draw the GUI
        pGuiGraphics.blit(TEXTURE, xStart, yStart, 0, 0, imageWidth, imageHeight);

        for (AbstractButton button : buttons) {
            button.render(pGuiGraphics, mouseX, mouseY, partialTick);
        }





        //Color based on selection state
        int color;
        int intColor;

        switch (selectionState) {
            case HOVERING -> {
                //Green, if known
                color = 3;
                intColor = 3731158;
            }
            case UNKNOWN_HOVERING -> {
                //Purple, if unknown
                color = 1;
                intColor = 6831720;
            }
            case SELECTED -> {
                //White, if selected
                color = 2;
                intColor = 16777215;
            }
            default -> {
                color = 0;
                intColor = 1234567;
            }
        }



        RuneTranslationList.drawRunicWordCenteredAt(pGuiGraphics, xStart + imageWidth / 2, yStart + imageHeight + 10, displayMessage.message, color);
        if (displayMessage.subtitle != null) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, displayMessage.subtitle, xStart + imageWidth / 2 - Minecraft.getInstance().font.width(displayMessage.subtitle) / 2, yStart + imageHeight + 20, intColor, false);
        }

    }






    private record DisplayMessage(String message, @Nullable String subtitle) {};


    private enum SelectionState {
        HOVERING,
        UNKNOWN_HOVERING,
        SELECTED
    }




}

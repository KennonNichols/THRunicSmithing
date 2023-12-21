package com.thathitmann.runicsmithing.runes;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PlayerRuneKnowledge {

    public PlayerRuneKnowledge() {
        for (char character : RuneTranslationList.alphabet) {
            knownCharacters.put(character, new PlayerRuneLearningProgress(character, 1));
        }
    }

    private Quest currentQuest = null;
    private Character currentLearningCharacter;

    private Map<Character, PlayerRuneLearningProgress> knownCharacters = new HashMap<>() {};


    public void setCurrentLearningCharacter(Character character) {
        currentLearningCharacter = character;
        currentQuest = RuneTranslationList.RuneWord.getWordFromCharacter(character).getQuest(knownCharacters.get(character).knownLetters);
    }
    public void checkQuestCompletion(Quest.QuestCompletionInfoPacket questCompletionPacket, Player player) {
        //If we are learning
        if (currentLearningCharacter != null) {
            //If we just finished the quest
            if (currentQuest.checkIfQuestCompleted(questCompletionPacket)) {
                //Learn the next letter
                knownCharacters.get(currentLearningCharacter).learnNextLetter();
                //And if we still haven't finished
                if (!knownCharacters.get(currentLearningCharacter).known) {
                    //Move on to the next quest
                    player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 0.8f, 0.8f);
                    player.sendSystemMessage(Component.literal("Quest complete! Learned more about the rune."));
                    currentQuest = RuneTranslationList.RuneWord.getWordFromCharacter(currentLearningCharacter).getQuest(knownCharacters.get(currentLearningCharacter).knownLetters);
                }
                else {
                    player.playNotifySound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.NEUTRAL, 0.8f, 0.8f);
                    player.sendSystemMessage(Component.literal("Quest complete! Learned rune '" + RuneTranslationList.RuneWord.getWordFromCharacter(currentLearningCharacter).name() + "'."));
                    currentQuest = null;
                }
            }
        }
    }
    public Quest getCurrentQuest() {return currentQuest;}
    //public Map<Character, PlayerRuneLearningProgress> getCharacterKnowledge() {return knownCharacters;}

    public String getKnownWord(Character character) {
        StringBuilder knownWordBuilder = new StringBuilder();
        String trueWord = RuneTranslationList.getRuneFromCharacter(character).getAssociatedWord();
        PlayerRuneLearningProgress progress = knownCharacters.get(character);
        int fauxIndex = 1;
        for (char thisChar : trueWord.toCharArray()) {
            if (fauxIndex <= progress.knownLetters) {
                knownWordBuilder.append(thisChar);
            }
            else {
                knownWordBuilder.append('?');
            }
            fauxIndex++;
        }

        return knownWordBuilder.toString();
    }

    public Character[] getKnownWords() {
        return knownCharacters.values().stream().filter(PlayerRuneLearningProgress::known).map(PlayerRuneLearningProgress::character).toArray(Character[]::new);
    }

    public boolean isWordKnown(char character) {
        return knownCharacters.get(character).known;
    }

    public void copyFrom(PlayerRuneKnowledge source) {this.knownCharacters = source.knownCharacters;}

    public void saveNBTData(CompoundTag tag) {
        CompoundTag libraryTag = new CompoundTag();

        for (char character : RuneTranslationList.alphabet) {
            libraryTag.putInt(Character.toString(character), knownCharacters.get(character).knownLetters);
        }


        tag.put("runicsmithing:rune_knowledge", libraryTag);
    }

    public void loadNBTData(CompoundTag tag) {
        CompoundTag libraryTag = tag.getCompound("runicsmithing:rune_knowledge");


        for (char character : RuneTranslationList.alphabet) {
            int progress = libraryTag.getInt(Character.toString(character));
            knownCharacters.put(character, new PlayerRuneLearningProgress(character, progress));
        }
    }


    private static final class PlayerRuneLearningProgress {
        private final char character;
        private int knownLetters;
        private boolean known;

        private PlayerRuneLearningProgress(char character, int knownLetters) {
            this.character = character;
            this.knownLetters = knownLetters;
            this.known = knownLetters >= RuneTranslationList.getRuneFromCharacter(character).getAssociatedWord().length();
        }

        public void learnNextLetter() {
            if (!known) {
                knownLetters += 1;
                if (knownLetters >= RuneTranslationList.getRuneFromCharacter(character).getAssociatedWord().length()) {
                    this.known = true;
                }
            }
        }

        public char character() {
            return character;
        }

        public int knownLetters() {
            return knownLetters;
        }

        public boolean known() {
            return known;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (PlayerRuneLearningProgress) obj;
            return this.character == that.character &&
                    this.knownLetters == that.knownLetters &&
                    this.known == that.known;
        }

        @Override
        public int hashCode() {
            return Objects.hash(character, knownLetters, known);
        }

        @Override
        public String toString() {
            return "PlayerRuneLearningProgress[" +
                    "Character=" + character + ", " +
                    "knownLetters=" + knownLetters + ", " +
                    "known=" + known + ']';
        }

        }

}

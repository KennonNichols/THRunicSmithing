package com.thathitmann.runicsmithing.networking.packet;

import com.thathitmann.runicsmithing.networking.ModMessages;
import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledge;
import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledgeProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.thathitmann.runicsmithing.runes.RuneTranslationList.alphabet;

public class RuneKnowledgeDataSyncC2SPacket {

    private static final Character[] modifiedAlphabet = new Character[] {
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','?'
    };
    private static final List<Character> alphabetList = List.of(alphabet);
    private Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters;
    private char learningChar;

    /*
    private Map<Character, Integer> getRuneLearningProgressAsIntMap() {
        return Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> knownCharacters.get(k).knownLetters()));
    }

    private void setRuneLearningProgressFromIntMap(Map<Character, Integer> intMap) {
        knownCharacters = Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> new PlayerRuneKnowledge.PlayerRuneLearningProgress(k, intMap.get(k))));
    }*/


    private int[] getRuneLearningProgressAsIntArray() {
        return Arrays.stream(modifiedAlphabet).mapToInt(k -> {
            if (k == '?') {
                return alphabetList.indexOf(learningChar);
            }
            return knownCharacters.get(k).knownLetters();
        }).toArray();
    }

    private void setRuneLearningProgressFromIntArray(int[] intArray) {
        knownCharacters = Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> new PlayerRuneKnowledge.PlayerRuneLearningProgress(k, intArray[alphabetList.indexOf(k)])));
        learningChar = alphabet[intArray[26]];
    }




    public RuneKnowledgeDataSyncC2SPacket(Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters, Character learningCharacter) {
        this.knownCharacters = knownCharacters;
        this.learningChar = learningCharacter;
    }
    public RuneKnowledgeDataSyncC2SPacket(FriendlyByteBuf buf) {
        //Gets int map, and sets based on it
        setRuneLearningProgressFromIntArray(buf.readVarIntArray());
    }

    public void toBytes(FriendlyByteBuf buf) {
        //Creates int map and puts it into the byteBuffer
        buf.writeVarIntArray(getRuneLearningProgressAsIntArray());
    }


    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //Server stuff

            ServerPlayer player = context.getSender();

            //player.sendSystemMessage(Component.literal(player.toString()));

            player.getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(runeKnowledge -> {
                runeKnowledge.copyKnowledgeFrom(knownCharacters);
                runeKnowledge.setCurrentLearningCharacter(learningChar);
            });

            //ClientRuneKnowledgeData.set(knownCharacters);
        });
        return true;
    }

}

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

    private Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters;

    /*
    private Map<Character, Integer> getRuneLearningProgressAsIntMap() {
        return Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> knownCharacters.get(k).knownLetters()));
    }

    private void setRuneLearningProgressFromIntMap(Map<Character, Integer> intMap) {
        knownCharacters = Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> new PlayerRuneKnowledge.PlayerRuneLearningProgress(k, intMap.get(k))));
    }*/


    private int[] getRuneLearningProgressAsIntArray() {
        return Arrays.stream(alphabet).mapToInt(k -> knownCharacters.get(k).knownLetters()).toArray();
    }

    private void setRuneLearningProgressFromIntArray(int[] intArray) {
        List<Character> alphabetList = List.of(alphabet);
        knownCharacters = Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> new PlayerRuneKnowledge.PlayerRuneLearningProgress(k, intArray[alphabetList.indexOf(k)])));
    }




    public RuneKnowledgeDataSyncC2SPacket(Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters) {
        this.knownCharacters = knownCharacters;
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
            });

            //ClientRuneKnowledgeData.set(knownCharacters);
        });
        return true;
    }

}

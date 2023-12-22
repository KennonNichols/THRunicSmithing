package com.thathitmann.runicsmithing.networking.packet;

import com.thathitmann.runicsmithing.event.ModEvents;
import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledge;
import com.thathitmann.runicsmithing.runes.RuneTranslationList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RuneKnowledgeDataSyncS2CPacket {

    private static final Character[] alphabet = new Character[] {
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','?'
    };
    private Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters;
    private int playerId;
    /*
    private Map<Character, Integer> getRuneLearningProgressAsIntMap() {
        return Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> knownCharacters.get(k).knownLetters()));
    }

    private void setRuneLearningProgressFromIntMap(Map<Character, Integer> intMap) {
        knownCharacters = Arrays.stream(alphabet).collect(Collectors.toMap(k -> k, k -> new PlayerRuneKnowledge.PlayerRuneLearningProgress(k, intMap.get(k))));
    }*/


    private int[] getPacketAsIntArray() {
        return Arrays.stream(alphabet).mapToInt(k -> {
            if (k == '?') {
                return playerId;
            }
            return knownCharacters.get(k).knownLetters();
        }).toArray();
    }

    private void setPacketFromIntArray(int[] intArray) {
        List<Character> alphabetList = List.of(RuneTranslationList.alphabet);
        knownCharacters = Arrays.stream(RuneTranslationList.alphabet).collect(Collectors.toMap(k -> k, k -> new PlayerRuneKnowledge.PlayerRuneLearningProgress(k, intArray[alphabetList.indexOf(k)])));
        playerId = intArray[26];
    }




    public RuneKnowledgeDataSyncS2CPacket(@NotNull Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters, @NotNull ServerPlayer player) {
        this.knownCharacters = knownCharacters;
        this.playerId = player.getId();
    }
    public RuneKnowledgeDataSyncS2CPacket(FriendlyByteBuf buf) {
        //Gets int map, and sets based on it
        setPacketFromIntArray(buf.readVarIntArray());
    }

    public void toBytes(FriendlyByteBuf buf) {
        //Creates int map and puts it into the byteBuffer
        buf.writeVarIntArray(getPacketAsIntArray());
    }



    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //TODO enqueue work
            //ClientRuneKnowledgeData.set(knownCharacters);
            ModEvents.queueProgressSync(playerId, knownCharacters);

            //player.getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(playerRuneKnowledge -> {
            //    playerRuneKnowledge.copyKnowledgeFrom(knownCharacters);
            //});
        });
        return true;
    }

}

package com.thathitmann.runicsmithing.client;

import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledge;

import java.util.Map;

public class ClientRuneKnowledgeData {
    private static Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters;

    public static void set(Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> knownCharacters) {
        ClientRuneKnowledgeData.knownCharacters = knownCharacters;
    }

    public static Map<Character, PlayerRuneKnowledge.PlayerRuneLearningProgress> getPlayerKnownCharacters() {
        return knownCharacters;
    }
}

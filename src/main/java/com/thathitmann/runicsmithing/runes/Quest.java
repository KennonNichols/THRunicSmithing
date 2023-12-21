package com.thathitmann.runicsmithing.runes;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.Objects;

public abstract class Quest {

    public final String message;


    public Quest(String message) {
        this.message = message;
    }

    public static class UnParameterizedQuest extends Quest{
        private final UnParameterizedQuestGoal unParameterizedQuestGoal;

        public UnParameterizedQuest(UnParameterizedQuestGoal unParameterizedQuestGoal, String message) {
            super(message);
            this.unParameterizedQuestGoal = unParameterizedQuestGoal;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.unPGoal == unParameterizedQuestGoal;
        }
    }
    public static class PickupItemQuest extends Quest{
        private final Item itemGoal;

        public PickupItemQuest(Item itemGoal, String message) {
            super(message);
            this.itemGoal = itemGoal;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.obtainedItem == itemGoal;
        }
    }
    public static class KillMobQuest extends Quest{
        private final LivingEntity entityGoal;

        public KillMobQuest(LivingEntity entityGoal, String message) {
            super(message);
            this.entityGoal = entityGoal;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.killedEntity == entityGoal;
        }
    }
    public static class TakeDamageQuest extends Quest{
        private final DamageType typeGoal;
        private final float amountGoal;

        public TakeDamageQuest(DamageType type, float amount, String message) {
            super(message);
            this.typeGoal = type;
            this.amountGoal = amount;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.damageType == typeGoal && packet.damageAmount >= amountGoal;
        }
    }
    public static class DrinkPotionQuest extends Quest{
        private final MobEffect effectGoal;

        public DrinkPotionQuest(MobEffect effect, String message) {
            super(message);
            this.effectGoal = effect;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.mobEffect == effectGoal;
        }
    }
    public static class EatFoodQuest extends Quest{
        private final int foodGoal;

        public EatFoodQuest(int amount, String message) {
            super(message);
            this.foodGoal = amount;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.hungerGained >= foodGoal;
        }
    }
    public static class EatRuneQuest extends Quest {

        public final float requiredPower;

        public EatRuneQuest(String message, float requiredPower) {
            super(message);
            this.requiredPower = requiredPower;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.unPGoal == UnParameterizedQuestGoal.EAT_RUNE;
        }
    }

    public abstract boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet);


    public static final class QuestCompletionInfoPacket {
        private final Float damageAmount;
        private final DamageType damageType;
        private final LivingEntity killedEntity;
        private final Item obtainedItem;
        private final Integer hungerGained;
        private final MobEffect mobEffect;
        private final UnParameterizedQuestGoal unPGoal;

        private QuestCompletionInfoPacket(Float damageAmount, DamageType damageType, LivingEntity killedEntity, Item obtainedItem, Integer hungerGained, MobEffect mobEffect, UnParameterizedQuestGoal unPGoal) {
            this.damageAmount = damageAmount;
            this.damageType = damageType;
            this.killedEntity = killedEntity;
            this.obtainedItem = obtainedItem;
            this.hungerGained = hungerGained;
            this.mobEffect = mobEffect;
            this.unPGoal = unPGoal;
        }

        @Override
        public String toString() {
            return "QuestCompletionInfoPacket{" +
                    "damageAmount=" + damageAmount +
                    ", damageType=" + damageType +
                    ", killedEntity=" + killedEntity +
                    ", obtainedItem=" + obtainedItem +
                    ", hungerGained=" + hungerGained +
                    ", mobEffect=" + mobEffect +
                    ", unPGoal=" + unPGoal +
                    '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(damageAmount, damageType, killedEntity, obtainedItem);
        }

    }
    public static class CompletionPacketBuilder {
        private Float damageAmount = null;
        private DamageType damageType = null;
        private LivingEntity killedEntity = null;
        private Item obtainedItem = null;
        private Integer hungerGained = null;
        private MobEffect mobEffect = null;
        private UnParameterizedQuestGoal unPGoal = null;
        public CompletionPacketBuilder setPlayerDamageTaken(DamageType type, Float damageAmount) {
            this.damageType = type;
            this.damageAmount = damageAmount;
            return this;
        }
        public CompletionPacketBuilder setPlayerKilledEntity(LivingEntity entity) {
            this.killedEntity = entity;
            return this;
        }

        public CompletionPacketBuilder setPlayerObtainedItem(Item item) {
            this.obtainedItem = item;
            return this;
        }

        public CompletionPacketBuilder setHungerGained(Integer hungerGained) {
            this.hungerGained = hungerGained;
            return this;
        }

        public CompletionPacketBuilder setMobEffect(MobEffect mobEffect) {
            this.mobEffect = mobEffect;
            return this;
        }

        public CompletionPacketBuilder setUnparameterizedGoal(UnParameterizedQuestGoal unPGoal) {
            this.unPGoal = unPGoal;
            return this;
        }


        public QuestCompletionInfoPacket build() {
            return new QuestCompletionInfoPacket(damageAmount, damageType, killedEntity, obtainedItem, hungerGained, mobEffect, unPGoal);
        }
    }


    public enum UnParameterizedQuestGoal {
        SLEEP,
        FISH,
        LEVEL_UP,
        EAT_RUNE
    }
}

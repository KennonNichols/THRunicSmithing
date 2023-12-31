package com.thathitmann.runicsmithing.runes;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Quest {

    public final String message;


    public Quest(String message) {
        this.message = message;
    }

    public static class UnParameterizedQuest extends Quest{
        private final UnParameterizedQuestGoal unParameterizedQuestGoal;

        public UnParameterizedQuest(String message, UnParameterizedQuestGoal unParameterizedQuestGoal) {
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

        public PickupItemQuest(String message, Item itemGoal) {
            super(message);
            this.itemGoal = itemGoal;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            return packet.obtainedItem == itemGoal;
        }
    }
    public static class KillMobQuest<T extends Entity> extends Quest{
        private final EntityType<T> entityGoal;

        public KillMobQuest(String message, EntityType<T> entityGoal) {
            super(message);
            this.entityGoal = entityGoal;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            if (packet.killedEntity == null) {
                return false;
            }
            return packet.killedEntity.getType() == entityGoal;
        }
    }
    public static class TakeDamageQuest extends Quest{
        private final String typeGoal;
        private final float amountGoal;

        public TakeDamageQuest(String message, String type, float amount) {
            super(message);
            this.typeGoal = type;
            this.amountGoal = amount;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            if (packet.damageType == null) {
                return false;
            }

            return packet.damageType.msgId().equals(typeGoal) && packet.damageAmount >= amountGoal;
        }
    }
    public static class DrinkPotionQuest extends Quest{
        private final MobEffect effectGoal;

        public DrinkPotionQuest(String message, MobEffect effect) {
            super(message);
            this.effectGoal = effect;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            if (packet.mobEffect == null) {
                return false;
            }
            return packet.mobEffect == effectGoal;
        }
    }
    public static class EatFoodQuest extends Quest{
        private final int foodGoal;

        public EatFoodQuest(String message, int amount) {
            super(message);
            this.foodGoal = amount;
        }

        @Override
        public boolean checkIfQuestCompleted(QuestCompletionInfoPacket packet) {
            if (packet.hungerGained == null) {
                return false;
            }
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
            if (packet.unPGoal == null) {
                return false;
            }
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
        public CompletionPacketBuilder setPlayerDamageTaken(@NotNull DamageType type, @NotNull Float damageAmount) {
            this.damageType = type;
            this.damageAmount = damageAmount;
            return this;
        }
        public CompletionPacketBuilder setPlayerKilledEntity(@NotNull LivingEntity entity) {
            this.killedEntity = entity;
            return this;
        }

        public CompletionPacketBuilder setPlayerObtainedItem(@NotNull Item item) {
            this.obtainedItem = item;
            return this;
        }

        public CompletionPacketBuilder setHungerGained(@NotNull Integer hungerGained) {
            this.hungerGained = hungerGained;
            return this;
        }

        public CompletionPacketBuilder setMobEffect(@NotNull MobEffect mobEffect) {
            this.mobEffect = mobEffect;
            return this;
        }

        public CompletionPacketBuilder setUnparameterizedGoal(@NotNull UnParameterizedQuestGoal unPGoal) {
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
        EAT_RUNE,
        SHIELD_BLOCK
    }
}

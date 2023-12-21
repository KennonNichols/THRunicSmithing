package com.thathitmann.runicsmithing.runes;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerRuneKnowledgeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerRuneKnowledge> PLAYER_RUNE_KNOWLEDGE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerRuneKnowledge playerRuneKnowledge = null;
    private final LazyOptional<PlayerRuneKnowledge> optional = LazyOptional.of(this::createPlayerRuneKnowledge);

    private PlayerRuneKnowledge createPlayerRuneKnowledge () {
        if (this.playerRuneKnowledge == null) {
            this.playerRuneKnowledge = new PlayerRuneKnowledge();
        }

        return this.playerRuneKnowledge;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_RUNE_KNOWLEDGE) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerRuneKnowledge().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        createPlayerRuneKnowledge().loadNBTData(tag);
    }
}

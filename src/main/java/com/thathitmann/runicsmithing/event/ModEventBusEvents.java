package com.thathitmann.runicsmithing.event;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.particle.EnchantmentAbsorb;
import com.thathitmann.runicsmithing.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RunicSmithing.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.ENCHANTMENT_ABSORB_PARTICLES.get(), EnchantmentAbsorb.Provider::new);
    }
}

package com.thathitmann.runicsmithing.particle;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.screen.ResearchScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class EnchantmentAbsorb extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;


    protected EnchantmentAbsorb(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.xStart = pX;
        this.yStart = pY;
        this.zStart = pZ;
        this.xo = pX + pXSpeed;
        this.yo = pY + pYSpeed;
        this.zo = pZ + pZSpeed;
        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;
        this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = 0.9F * f;
        this.gCol = 0.9F * f;
        this.bCol = f;
        this.hasPhysics = false;
        this.lifetime = 60;
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet texture;

        public Provider(SpriteSet texture) {
            this.texture = texture;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            EnchantmentAbsorb enchantmentAbsorb = new EnchantmentAbsorb(level, x, y, z, dx, dy, dz);
            enchantmentAbsorb.pickSprite(texture);
            return enchantmentAbsorb;
        }

    }



}

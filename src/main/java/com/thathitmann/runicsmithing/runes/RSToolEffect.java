package com.thathitmann.runicsmithing.runes;

import net.minecraft.world.effect.MobEffect;

public abstract class RSToolEffect {

    private final float strength;

    public RSToolEffect(float strength) {
        this.strength = strength;
    }





    public static class TriggeredEffect extends RSToolEffect {

        public TriggeredEffect(float strength) {
            super(strength);
        }

        public static class Overheal extends TriggeredEffect {
            public Overheal(float strength) {
                super(strength);
            }
        }

        public static class Nourish extends TriggeredEffect {
            public Nourish(float strength) {
                super(strength);
            }
        }

        public static class Jump extends TriggeredEffect {
            public Jump(float strength) {
                super(strength);
            }
        }

        public static class Heal extends TriggeredEffect {
            public Heal(float strength) {
                super(strength);
            }
        }

        public static class Fortify extends TriggeredEffect {
            public Fortify(float strength) {
                super(strength);
            }
        }
    }


    public static class Super extends RSToolEffect {
        public Super(float strength) {
            super(strength);
        }


        public static class Aerial extends RSToolEffect {
            public Aerial(float strength) {
                super(strength);
            }
        }
        public static class Crumble extends RSToolEffect {
            public Crumble(float strength) {
                super(strength);
            }
        }
        public static class CallSmite extends RSToolEffect {
            public CallSmite(float strength) {
                super(strength);
            }
        }
        public static class UnweaveMatter extends RSToolEffect {
            public UnweaveMatter(float strength) {
                super(strength);
            }
        }
        public static class ZenithThrow extends RSToolEffect {
            public ZenithThrow(float strength) {
                super(strength);
            }
        }
        public static class YouthDrain extends RSToolEffect {
            public YouthDrain(float strength) {
                super(strength);
            }
        }
        public static class XRayVision extends RSToolEffect {
            public XRayVision(float strength) {
                super(strength);
            }
        }
        public static class Energize extends RSToolEffect {
            public Energize(float strength) {
                super(strength);
            }
        }
    }

    public static class Garble extends RSToolEffect {
        public Garble(float strength) {
            super(strength);
        }
    }
    public static class LaunchMob extends RSToolEffect {
        public LaunchMob(float strength) {
            super(strength);
        }
    }
    public static class AttributeModifier extends RSToolEffect {
        public AttributeModifier(float strength) {
            super(strength);
        }

        public static class Reach extends AttributeModifier {
            public Reach(float strength) {
                super(strength);
            }
        }

        public static class Haste extends AttributeModifier {
            public Haste(float strength) {
                super(strength);
            }
        }

        public static class Damage extends AttributeModifier {
            public Damage(float strength) {
                super(strength);
            }
        }
    }
    public static class InflictMobEffect extends RSToolEffect {
        private MobEffect effect;
        private int duration;

        public InflictMobEffect(float strength) {
            super(strength);
        }

        public void setMobEffect(MobEffect effect, int duration) {
            this.effect = effect;
            this.duration = duration;
        }


        public static class Wither extends InflictMobEffect {
            public Wither(float strength) {
                super(strength);
            }
        }

        public static class Poison extends InflictMobEffect {

            public Poison(float strength) {
                super(strength);
            }
        }

    }
    public static class LeechHealth extends RSToolEffect {
        public LeechHealth(float strength) {
            super(strength);
        }
    }
    public static class Durability extends RSToolEffect {
        public Durability(float strength) {
            super(strength);
        }
    }
    public static class Burn extends RSToolEffect {
        public Burn(float strength) {
            super(strength);
        }
    }
}

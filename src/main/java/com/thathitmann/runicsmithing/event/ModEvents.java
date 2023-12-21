package com.thathitmann.runicsmithing.event;

import com.thathitmann.runicsmithing.RunicSmithing;
import com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.SmithingChainItem;
import com.thathitmann.runicsmithing.runes.PlayerRuneKnowledgeProvider;
import com.thathitmann.runicsmithing.runes.Quest;
import com.thathitmann.runicsmithing.runes.RuneTranslationList;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.thathitmann.runicsmithing.RunicSmithing.burningHotTag;
import static com.thathitmann.runicsmithing.RunicSmithing.heatInsulatingTag;
import static net.minecraft.world.level.block.EnchantmentTableBlock.BOOKSHELF_OFFSETS;


@Mod.EventBusSubscriber(modid = RunicSmithing.MOD_ID)
public class ModEvents {


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        for (RuneEatingHandler runeEatingHandler : runeEatings) {
            runeEatingHandler.tick();
        }
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        for (RuneEatingHandler runeEatingHandler : runeEatings) {
            runeEatingHandler.animate();
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            Player player = event.player;
            if (player.getInventory().contains(burningHotTag)) {
                if(!player.getInventory().contains(heatInsulatingTag)) {
                    if (!player.isOnFire()) {
                        player.setSecondsOnFire(1);
                    }
                }
            }
        }
    }


    //region quest events
    @SubscribeEvent
    public static void onPlayerPickupItem(PlayerEvent.ItemPickupEvent event) {
        sendQuestCompletionPacket(
                new Quest.CompletionPacketBuilder().setPlayerObtainedItem(event.getStack().getItem()).build(),
                event.getEntity());
    }


    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        sendQuestCompletionPacket(
                new Quest.CompletionPacketBuilder().setUnparameterizedGoal(Quest.UnParameterizedQuestGoal.SLEEP).build(),
                event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerFish(ItemFishedEvent event) {
        sendQuestCompletionPacket(
                new Quest.CompletionPacketBuilder().setUnparameterizedGoal(Quest.UnParameterizedQuestGoal.FISH).build(),
                event.getEntity());
    }

    @SubscribeEvent
    public static void onItemUsed(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player) {
            Item item = event.getItem().getItem();

            if (item.isEdible()) {
                sendQuestCompletionPacket(
                        new Quest.CompletionPacketBuilder().setHungerGained(event.getItem().getFoodProperties(player).getNutrition()).build(),
                        player);
            }

            if (item instanceof PotionItem potionItem) {
                for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(event.getItem())) {
                    sendQuestCompletionPacket(
                            new Quest.CompletionPacketBuilder().setMobEffect(mobeffectinstance.getEffect()).build(),
                            player);
                }
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerLevelUp(PlayerXpEvent.LevelChange event) {
        if (event.getLevels() > 0) {
            sendQuestCompletionPacket(
                    new Quest.CompletionPacketBuilder().setUnparameterizedGoal(Quest.UnParameterizedQuestGoal.LEVEL_UP).build(),
                    event.getEntity());
        }
    }

    /*@SubscribeEvent
    public static void onEnchant(EnchantE) {
        if (event.getLevels() > 0) {
            sendQuestCompletionPacket(
                    new Quest.CompletionPacketBuilder().setUnparameterizedGoal(Quest.UnParameterizedQuestGoal.LEVEL_UP).build(),
                    event.getEntity());
        }
    }*/


    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            sendQuestCompletionPacket(
                    new Quest.CompletionPacketBuilder().setPlayerKilledEntity(event.getEntity()).build(),
                    player
            );
        }
    }


    /*@SubscribeEvent
    public static void onMobHurtByPlayer(AttackEntityEvent event) {

    }*/

    @SubscribeEvent
    public static void onMobHurt(LivingHurtEvent event) {
        //Sends player damage
        if (event.getEntity() instanceof Player player) {
            sendQuestCompletionPacket(
                    new Quest.CompletionPacketBuilder().setPlayerDamageTaken(event.getSource().type(), event.getAmount()).build(),
                    player
            );
        }
        /*
        //Sends player dealt damage
        else if (event.getSource().getEntity() instanceof Player player) {
            sendQuestCompletionPacket(
                    new Quest.QuestCompletionInfoPacket(null, null, null, null),
                    player
            );
        }*/
    }


    //endregion






    //Sanity system
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).isPresent()) {
                event.addCapability(new ResourceLocation(RunicSmithing.MOD_ID, "properties"), new PlayerRuneKnowledgeProvider());
            }
        }
    }


    @SubscribeEvent
    public static void onWorldStart(ServerStartedEvent event) {
        RuneTranslationList.generateSeededTranslation(event.getServer());
    }

    //Cool any burning hot item when thrown. This is an anti-troll measure.
    @SubscribeEvent
    public static void onEntityToss(ItemTossEvent event) {
        //if (event.side == LogicalSide.SERVER) {}
        ItemStack droppedItemStack = event.getEntity().getItem();
        if (droppedItemStack.is(burningHotTag)) {

            Item droppedItem = droppedItemStack.getItem();
            //ItemStack changedItem = droppedItem.getItem()
            if (droppedItem instanceof SmithingChainItem) {

                Item changedItem = ((SmithingChainItem) droppedItem).getCoolingResult();
                if (changedItem != null) {
                    ItemStack changedItemStack = new ItemStack(changedItem);

                    changedItemStack.setCount(droppedItemStack.getCount());

                    event.getEntity().setItem(changedItemStack);
                }
            }
        }
    }





    private static void sendQuestCompletionPacket(Quest.QuestCompletionInfoPacket questCompletionInfoPacket, Player player) {
        player.getCapability(PlayerRuneKnowledgeProvider.PLAYER_RUNE_KNOWLEDGE).ifPresent(playerRuneKnowledge -> {
            playerRuneKnowledge.checkQuestCompletion(questCompletionInfoPacket, player);
        });
    }





    //region rune eating
    private static final Random rand = new Random();
    private static final List<RuneEatingHandler> runeEatings = new CopyOnWriteArrayList<>();

    public static void startEatingRunes(float enchantingPower, BlockPos eatingPosition, Player player) {
        runeEatings.add(new RuneEatingHandler(
                player,
                (int) (enchantingPower * 30),
                eatingPosition
        ));
    }

    public static boolean isPlayerEatingRunes(Player player) {
        for (RuneEatingHandler runeEatingHandler : runeEatings) {
            if (runeEatingHandler.player == player) {
                return true;
            }
        }
        return false;
    }

    private static class RuneEatingHandler {
        private final Player player;
        private final int length;
        private final BlockPos pos;
        private int currentFrame = 0;

        private double angle = 0;
        private float volume = 0.2f;
        private final int soundCooldown = 60;
        private int soundTimer = 0;

        private RuneEatingHandler(Player player, int length, BlockPos pos) {
            this.player = player;
            //Highest length should be Zenith's 600-frame animation (30 seconds)
            this.length = length;
            this.pos = pos;
        }

        void tick() {
            currentFrame++;
            if (currentFrame >= length) {
                runeEatings.remove(this);
                sendQuestCompletionPacket(
                        new Quest.CompletionPacketBuilder().setUnparameterizedGoal(Quest.UnParameterizedQuestGoal.EAT_RUNE).build(),
                        player);
            }
        }


        void animate() {
            Vec3 playerEyePos = player.getEyePosition();


            double xPlayerShift = rand.nextDouble(-1, 1);
            double zPlayerShift = rand.nextDouble(-1, 1);
            double yPlayerShift = rand.nextDouble(-1, 1);

            double xShift = rand.nextDouble(-0.3, 0.3);
            double zShift = rand.nextDouble(-0.3, 0.3);
            double yShift = 0.7;

            double xTableLocation = pos.getCenter().x + xShift;
            double yTableLocation = pos.getCenter().y + yShift;
            double zTableLocation = pos.getCenter().z + zShift;

            double xPlayerLocation = playerEyePos.x() + xPlayerShift;
            double yPlayerLocation = playerEyePos.y() + yPlayerShift;
            double zPlayerLocation = playerEyePos.z() + zPlayerShift;

            double xSpeed = (xTableLocation - xPlayerLocation);
            double ySpeed = (yTableLocation - yPlayerLocation);
            double zSpeed = (zTableLocation - zPlayerLocation);

            //Chance gets higher as we get later into the animation
            if (rand.nextInt(64) <= currentFrame) {
                player.level().addParticle(ParticleTypes.ENCHANT, xPlayerLocation, yPlayerLocation, zPlayerLocation, xSpeed, ySpeed, zSpeed);
            }
            if (rand.nextInt(256) <= currentFrame) {
                player.level().addParticle(ParticleTypes.ENCHANT, xPlayerLocation, yPlayerLocation, zPlayerLocation, xSpeed, ySpeed, zSpeed);
            }
            if (rand.nextInt(600) <= currentFrame) {
                player.level().addParticle(ParticleTypes.ENCHANT, xPlayerLocation, yPlayerLocation, zPlayerLocation, xSpeed, ySpeed, zSpeed);
            }

            if (rand.nextFloat() < 0.02f && soundTimer <= 0) {
                player.playNotifySound(SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, volume, 0);
                volume += 0.1;
                soundTimer = soundCooldown;
            }

            soundTimer -= 1;


            //Circle the player
            placeCircledParticle(player.getEyePosition(), -1.0d, angle, player.level());
            //Circle the table
            placeCircledParticle(pos.getCenter(), 0, -angle, player.level());
            angle += 0.1;
        }

        private void placeCircledParticle(Vec3 pPos, double yOffset, double angle, Level level) {
            double x = pPos.x() + Math.cos(angle);
            double y = pPos.y() + yOffset;
            double z = pPos.z() + Math.sin(angle);
            player.level().addParticle(ParticleTypes.PORTAL, x, y, z, 0, 0, 0);
        }

    }
    //endregion

}

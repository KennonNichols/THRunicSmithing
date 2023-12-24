package com.thathitmann.runicsmithing.runes;

import com.thathitmann.runicsmithing.RunicSmithing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RuneTranslationList {

    //region rune alphabet, alphabet data, alphabet IO
    private static final ResourceLocation RUNE_ALPHABET_ATLAS =
            new ResourceLocation(RunicSmithing.MOD_ID, "textures/gui/runes.png");

    private static int getLetterAtlasX(char character) {
        return Arrays.stream(alphabet).toList().indexOf(character) * 8;
    }

    public static void drawRunicWordCenteredAt(@NotNull GuiGraphics pGuiGraphics, int x, int y, @NotNull String word, int color) {
        int xStart = x - 8 * word.length() / 2;
        int progress = 0;
        int atlasY = 8 * color;

        for (Character character : word.toCharArray()) {
            if (Arrays.stream(alphabet).anyMatch(character1 -> character1 == character)) {
                pGuiGraphics.blit(RUNE_ALPHABET_ATLAS, xStart + progress, y, getLetterAtlasX(character), atlasY, 7, 7);
            }
            else  {
                //Draw random letters for unknowns
                pGuiGraphics.blit(RUNE_ALPHABET_ATLAS, xStart + progress, y, rand.nextInt(26) * 8, atlasY, 7, 7);
            }
            progress += 8;
        }
    }

    //Runes are 7 x 7




    public static final HashMap<Character, Rune> runeTranslation = new HashMap<>() {};
    public static final Character[] alphabet = new Character[] {
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
    };

    private static final Random rand = new Random();

    private static final String tagKey = "RUNIC_SMITHING_WORLD_TRANSLATION";
    private static final String translationTagKey = "RUNIC_SMITHING_WORLD_TRANSLATION";
    private static final String wordTagKey = "RUNIC_SMITHING_WORLD_TRANSLATION";

    public static void generateSeededTranslation(@NotNull MinecraftServer server) {
        DimensionDataStorage overworldStorage = server.overworld().getDataStorage();

        //Create data if it doesn't exist
        RuneSavedData data = overworldStorage.computeIfAbsent(RuneSavedData::load, RuneSavedData::create, tagKey);
        data.setDirty();
    }

    /**
     * This is NOT a translation. This gets the exact rune associated with a character.
     */
    public static Rune getRuneFromCharacter(char character) {
        for (Rune rune : runeTranslation.values()) {
            if (rune.runicLetter == character) {
                return rune;
            }
        }
        return new Rune('a', "Something's wrong. Please contact the mod author :)");
    }



    private static class RuneSavedData extends SavedData {

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag pCompoundTag) {
            CompoundTag runeTranslationTag = new CompoundTag();
            CompoundTag runeWordTag = new CompoundTag();
            CompoundTag runicSmithingRuneTag = new CompoundTag();

            //The key is the ENGLISH letter, the tagged string is the RUNIC letter
            for (int i = 0; i < 26; i++){
                runeTranslationTag.putString(alphabet[i].toString(), Character.toString(runeTranslation.get(alphabet[i]).runicLetter));
            }
            for (int i = 0; i < 26; i++){
                runeWordTag.putString(alphabet[i].toString(), runeTranslation.get(alphabet[i]).associatedWord);
            }


            runicSmithingRuneTag.put(translationTagKey, runeTranslationTag);
            runicSmithingRuneTag.put(wordTagKey, runeWordTag);


            pCompoundTag.put(tagKey, runicSmithingRuneTag);

            return pCompoundTag;
        }




        private static RuneSavedData create() {
            //Builds new save data;


            List<Character> listOfRuneChars = new ArrayList<>(Arrays.stream(alphabet).toList());

            Collections.shuffle(listOfRuneChars);

            for (int i = 0; i < 26; i++){

                Character englishCharacter = alphabet[i];
                Character runicCharacter = listOfRuneChars.get(i);

                runeTranslation.put(
                        englishCharacter,
                        new Rune(runicCharacter, createRandomWord(runicCharacter, RuneWord.getWordFromCharacter(runicCharacter).length))
                );
            }


            return new RuneSavedData();
        }

        private static String createRandomWord(Character firstLetter, int length) {
            StringBuilder wordBuilder = new StringBuilder(firstLetter.toString());
            for (int i = 0; i < length - 1; i++) {
                //Get a random letter
                wordBuilder.append(alphabet[rand.nextInt(26)]);
            }
            return wordBuilder.toString();
        }

        private static RuneSavedData load(CompoundTag tag) {
            RuneSavedData data = new RuneSavedData();
            CompoundTag mainTag = tag.getCompound(tagKey);
            CompoundTag translationTag = mainTag.getCompound(translationTagKey);
            CompoundTag wordTag = mainTag.getCompound(wordTagKey);

            //Loads the translation from the tag
            for (int i = 0; i < 26; i++){
                runeTranslation.put(
                        alphabet[i],
                        new Rune(
                            translationTag.getString(Character.toString(alphabet[i])).charAt(0),
                                wordTag.getString(Character.toString(alphabet[i])))
                );
            }

            return data;
        }
    }





    public static class Rune {
        //private final char englishLetter;
        private final char runicLetter;
        private final String associatedWord;

        public Rune(char runicLetter, String associatedWord) {
            this.runicLetter = runicLetter;
            this.associatedWord = associatedWord;
        }

        public char getRunicLetter() {
            return runicLetter;
        }
        public String getAssociatedWord() {
            return associatedWord;
        }

        @Override
        public String toString() {
            return "Rune " + runicLetter + " (" + associatedWord + ")";
        }
    }


    //endregion

    //region words
    //final static Map<Character, RuneWord> runicAlphabet = new HashMap<>();

    private enum RuneWordType {
        ATTRIBUTEMODIFIER,
        TRIGGERED,
        TRIGGER,
        SUPER,
        BASIC
    }

    public enum RuneWord {
        AERIAL('a', null, new Class[]{RSToolEffect.Super.Aerial.class}, 4, RuneWordType.SUPER, new Quest[]{
                new Quest.TakeDamageQuest("Feel the impact of gravity.", "fall", 4),
                new Quest.TakeDamageQuest("Feel the sting of a rocket.", "fireworks", 1),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 16),
        }),
        BURN('b', new Class[]{RSToolTrigger.onMobInjured.class, RSToolTrigger.onBlockMined.class}, new Class[]{RSToolEffect.Burn.class}, 3, RuneWordType.BASIC, new Quest[]{
                new Quest.TakeDamageQuest("Bathe in the heat of flames.", "inFire", 1),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 5),
        }),
        CRUMBLE('c', null, new Class[]{RSToolEffect.Super.Crumble.class}, 5, RuneWordType.SUPER, new Quest[]{
                new Quest.PickupItemQuest("Mine the deepest stone.", Items.COBBLED_DEEPSLATE),
                new Quest.KillMobQuest<>("Kill a winged creature of the deep.", EntityType.BAT),
                new Quest.UnParameterizedQuest("Learn and grow in experience.", Quest.UnParameterizedQuestGoal.LEVEL_UP),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 15),
        }),
        DURABILITY('d', new Class[]{RSToolTrigger.onMobInjured.class, RSToolTrigger.onBlockMined.class}, new Class[]{RSToolEffect.Durability.class}, 3, RuneWordType.BASIC, new Quest[]{
                new Quest.PickupItemQuest("Obtain an anvil.", Items.ANVIL),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 5),
        }),
        ENERGIZE('e', null, new Class[]{RSToolEffect.Super.Energize.class}, 3, RuneWordType.SUPER, new Quest[]{
                new Quest.UnParameterizedQuest("Take a nap.", Quest.UnParameterizedQuestGoal.SLEEP),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 12),
        }),
        FORTIFY('f', null, new Class[]{RSToolEffect.TriggeredEffect.Fortify.class}, 3, RuneWordType.TRIGGERED, new Quest[]{
                new Quest.UnParameterizedQuest("Deflect an attack.", Quest.UnParameterizedQuestGoal.SHIELD_BLOCK),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 8),
        }),
        GARBLE('g', new Class[]{RSToolTrigger.onMobInjured.class, RSToolTrigger.onBlockMined.class}, new Class[]{RSToolEffect.Garble.class}, 3, RuneWordType.BASIC, new Quest[]{
                new Quest.TakeDamageQuest("Get crushed in a tight space.", "cramming", 1),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 8),
        }),
        HEAL('h', null, new Class[] {RSToolEffect.TriggeredEffect.Heal.class}, 4, RuneWordType.TRIGGERED, new Quest[]{
                new Quest.DrinkPotionQuest("Hurt yourself by unnatural means.", MobEffects.HARM),
                new Quest.DrinkPotionQuest("Heal yourself by unnatural means.", MobEffects.HEAL),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 12),
        }),
        INJURE('i', new Class[] {RSToolTrigger.onMobInjured.class}, null, 3, RuneWordType.TRIGGER, new Quest[]{
                new Quest.KillMobQuest<>("Draw the blood of a zombie.", EntityType.ZOMBIE),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 5),
        }),
        JUMP('j', null, new Class[] {RSToolEffect.TriggeredEffect.Jump.class}, 2, RuneWordType.TRIGGERED, new Quest[]{
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 4),
        }),
        KILL('k', new Class[] {RSToolTrigger.onHostileMobKilled.class}, null, 2, RuneWordType.TRIGGER, new Quest[]{
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 5),
        }),
        LAUNCH('l',  new Class[]{RSToolTrigger.onMobInjured.class}, new Class[]{RSToolEffect.LaunchMob.class}, 2, RuneWordType.BASIC, new Quest[]{
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 5),
        }),
        MINE('m', new Class[] {RSToolTrigger.onBlockMined.class}, null, 2, RuneWordType.TRIGGER, new Quest[]{
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 5),
        }),
        NOURISH('n', null, new Class[] {RSToolEffect.TriggeredEffect.Nourish.class}, 3, RuneWordType.TRIGGERED, new Quest[]{
                new Quest.EatFoodQuest("Eat some fulfilling food.", 6),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 8),
        }),
        OVERHEAL('o', null, new Class[] {RSToolEffect.TriggeredEffect.Overheal.class}, 5, RuneWordType.TRIGGERED, new Quest[]{
                new Quest.TakeDamageQuest("Feel the pangs of starvation gnawing at you.", "starve", 1),
                new Quest.EatFoodQuest("Eat some very fulfilling food.", 8),
                new Quest.DrinkPotionQuest("Heal yourself by unnatural means.", MobEffects.HEAL),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 13),
        }),
        POWER('p', null, new Class[]{RSToolEffect.AttributeModifier.Damage.class}, 3, RuneWordType.ATTRIBUTEMODIFIER, new Quest[]{
                new Quest.DrinkPotionQuest("Empower yourself with magic.", MobEffects.DAMAGE_BOOST),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 8),
        }),
        QUICKNESS('q', null, new Class[]{RSToolEffect.AttributeModifier.Haste.class}, 3, RuneWordType.ATTRIBUTEMODIFIER, new Quest[]{
                new Quest.DrinkPotionQuest("Speed yourself up.", MobEffects.MOVEMENT_SPEED),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 10),
        }),
        REACH('r', null, new Class[]{RSToolEffect.AttributeModifier.Reach.class}, 3, RuneWordType.ATTRIBUTEMODIFIER, new Quest[]{
                new Quest.UnParameterizedQuest("Take some time to fish and lament that the crab lost.", Quest.UnParameterizedQuestGoal.FISH),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 10),
        }),
        SMITE('s', null, new Class[]{RSToolEffect.Super.CallSmite.class}, 5, RuneWordType.SUPER, new Quest[]{
                new Quest.UnParameterizedQuest("Fish something up.", Quest.UnParameterizedQuestGoal.FISH),
                new Quest.TakeDamageQuest("Get impaled by a trident.", "trident", 1),
                new Quest.PickupItemQuest("Obtain a lightning rod", Items.LIGHTNING_ROD),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 15),
        }),
        TOXIC('t',  new Class[]{RSToolTrigger.onMobInjured.class}, new Class[]{RSToolEffect.InflictMobEffect.Poison.class}, 3, RuneWordType.BASIC, new Quest[]{
                new Quest.DrinkPotionQuest("Have a taste of your own poison.", MobEffects.POISON),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 8),

        }),
        UNWEAVE('u', null, new Class[]{RSToolEffect.Super.UnweaveMatter.class}, 5, RuneWordType.SUPER, new Quest[]{
                new Quest.PickupItemQuest("Craft an iconic block of creation.", Items.CRAFTING_TABLE),
                new Quest.PickupItemQuest("Craft an iconic block of destruction.", Items.TNT),
                new Quest.TakeDamageQuest("Feel the blast wave of an explosion.", "explosion.player", 8),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 18),
        }),
        VAMPIRISM('v', new Class[]{RSToolTrigger.onMobInjured.class}, new Class[] {RSToolEffect.LeechHealth.class}, 3, RuneWordType.BASIC, new Quest[]{
                new Quest.DrinkPotionQuest("Heal yourself by unnatural means.", MobEffects.HEAL),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 9),
        }),
        WITHER('w', new Class[]{RSToolTrigger.onMobInjured.class}, new Class[]{RSToolEffect.InflictMobEffect.Wither.class}, 3, RuneWordType.BASIC, new Quest[]{
                new Quest.PickupItemQuest("Obtain the skull of a withered beast.", Items.WITHER_SKELETON_SKULL),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 9),
        }),
        XRAY('x', null, new Class[] {RSToolEffect.Super.XRayVision.class}, 3, RuneWordType.SUPER, new Quest[]{
                new Quest.PickupItemQuest("Obtain a magic arrow of sight.", Items.SPECTRAL_ARROW),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 9),
        }),
        YOUTH('y', null, new Class[] {RSToolEffect.Super.YouthDrain.class}, 5, RuneWordType.SUPER, new Quest[]{
                new Quest.KillMobQuest<>("Kill a long-dead undead.", EntityType.SKELETON),
                new Quest.PickupItemQuest("Gaze into an eye from beyond.", Items.ENDER_EYE),
                new Quest.UnParameterizedQuest("Get some beauty sleep to keep looking young.", Quest.UnParameterizedQuestGoal.SLEEP),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 15),
        }),
        ZENITH('z', null, new Class[] {RSToolEffect.Super.ZenithThrow.class}, 6, RuneWordType.SUPER, new Quest[]{
                new Quest.KillMobQuest<>("Kill the most iconic monster.", EntityType.CREEPER),
                new Quest.PickupItemQuest("Get a magic apple.", Items.GOLDEN_APPLE),
                new Quest.PickupItemQuest("Obtain a timeless and precious jewel.", Items.DIAMOND),
                new Quest.UnParameterizedQuest("Level up.", Quest.UnParameterizedQuestGoal.LEVEL_UP),
                new Quest.EatRuneQuest("Absorb some runes by shift-clicking on an enchantment table.", 20),
        });


        private final char firstLetter;
        private final Class<? extends RSToolTrigger>[] triggers;
        private final Class<? extends RSToolEffect>[] effects;
        private final Quest[] quests;
        private final int length;
        private final RuneWordType type;

        RuneWord(char firstLetter, Class<? extends RSToolTrigger>[] triggers, Class<? extends RSToolEffect>[] effects, int length, RuneWordType type, Quest[] quests) {
            if (quests.length < length - 1) {
                throw new RuntimeException("Not enough quests for word " + name());
            }

            this.firstLetter = firstLetter;
            this.triggers = triggers;
            this.effects = effects;
            this.length = length;
            this.type = type;
            this.quests = quests;

            //runicAlphabet.put(firstLetter, this);
        }



        public static RuneWord getWordFromCharacter(char character) {
            for (RuneWord word : values()) {
                if (word.firstLetter == character) {
                    return word;
                }
            }
            return ZENITH;
        }

        public Quest getQuest(int letterIndex) {
            return quests[letterIndex - 1];
        }

        public int getLength() {
            return length;
        }
    }


    //endregion

}

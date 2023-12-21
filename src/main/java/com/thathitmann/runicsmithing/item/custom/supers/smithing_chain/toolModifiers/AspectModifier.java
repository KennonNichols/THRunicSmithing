package com.thathitmann.runicsmithing.item.custom.supers.smithing_chain.toolModifiers;

import com.thathitmann.runicsmithing.block.entity.CoreForgeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.thathitmann.runicsmithing.block.custom.CoreForgeBlock.DEPTH;


/**
 * Contains two keys:
 * quality
 * name
 *
 * Identified as:
 * aspect
 * */
public class AspectModifier extends ToolModifier {
    public final int quality;
    public AspectModifier(String name, String description, int quality) {
        super(name, description);
        this.quality = quality;
    }

    public static AspectModifier fromTag(CompoundTag tag) {
        String description = tag.getString("description");

        if (description.equals("")) {
            description = "No description";
        }

        return new AspectModifier(
                tag.getString("name"),
                description,
                tag.getInt("quality")
        );
    }


    public String getTagTypeTitle() {return ASPECT_NAME;};

    @Override
    public CompoundTag getAsTag() {
        CompoundTag aspectTag = new CompoundTag();
        aspectTag.putInt("quality", quality);
        aspectTag.putString("name", name);
        aspectTag.putString("description", description);
        return aspectTag;
    }


    private record DepthModifier(String name, int depth, int quality) {}

    private static final DepthModifier[] expandedDepthModifiers = new DepthModifier[] {
            new DepthModifier("Coreforged", 500, 15),
            new DepthModifier("Hellforged", 450, 10),
            new DepthModifier("Netherforged", 400, 9),
            new DepthModifier("Stygianforged", 350, 8),
            new DepthModifier("Ultradeepforged", 300, 7),
            new DepthModifier("Deepforged", 250, 6),
            new DepthModifier("Dwarvforged", 200, 5),
            new DepthModifier("Extreme Forged", 150, 4),
            new DepthModifier("Depth Forged", 100, 3),
            new DepthModifier("High-p Forged", 50, 2),
            new DepthModifier("Pressure Forged", 0, 1)
    };
    private static final DepthModifier[] vanillaDepthModifiers = new DepthModifier[] {
            new DepthModifier("Coreforged", 50, 2),
            new DepthModifier("Deepforged", 0, 1)
    };



    public static @Nullable AspectModifier getDepthAspect(int depth, boolean extendedWorld) {
        if (extendedWorld) {
            for (DepthModifier depthModifier : expandedDepthModifiers) {
                if (depth > depthModifier.depth) {
                    return new AspectModifier(depthModifier.name, "Forged at " + -depth + " for " + depthModifier.quality + " quality.", depthModifier.quality);
                }
            }
        }
        else {
            for (DepthModifier depthModifier : vanillaDepthModifiers) {
                if (depth > depthModifier.depth) {
                    return new AspectModifier(depthModifier.name, "Forged at " + -depth + " for " + depthModifier.quality + " quality.", depthModifier.quality);
                }
            }
        }
        return null;
    }


}

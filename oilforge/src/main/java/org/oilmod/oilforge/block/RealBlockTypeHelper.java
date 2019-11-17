package org.oilmod.oilforge.block;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.PistonReaction;
import org.oilmod.api.blocks.nms.NMSBlockType;

import static org.oilmod.api.blocks.BlockType.BlockTypeEnum.*;
import static org.oilmod.api.blocks.BlockType.BlockTypeEnum.STRUCTURE_VOID;

public class RealBlockTypeHelper  extends BlockType.BlockTypeHelper {
    private TMap<Material, BlockType> map = new THashMap<>();
    private boolean initialised;

    private void register(Material mat, BlockType.BlockTypeEnum blockTypeEnum) {
        map.put(mat, new RealBlockType(mat, blockTypeEnum));
    }

    public BlockType get(Material mat) {
        return map.get(mat);
    }

    @Override
    protected void apiInit() {
        Validate.isTrue(!initialised, "Already initialised");
        initialised = true;
        register(Material.AIR, AIR);//KEEPSYNC have all types - needs to be done this way to ensure that both static instances are initialized properly
        register(Material.STRUCTURE_VOID, STRUCTURE_VOID);
        register(Material.PORTAL, PORTAL);
        register(Material.CARPET, CARPET);
        register(Material.PLANTS, PLANTS);
        register(Material.OCEAN_PLANT, OCEAN_PLANT);
        register(Material.VINE, VINE);
        register(Material.SEA_GRASS, SEA_GRASS);
        register(Material.WATER, WATER);
        register(Material.BUBBLE_COLUMN, BUBBLE_COLUMN);
        register(Material.LAVA, LAVA);
        register(Material.SNOW, SNOW);
        register(Material.FIRE, FIRE);
        register(Material.CIRCUITS, CIRCUITS);
        register(Material.WEB, WEB);
        register(Material.REDSTONE_LIGHT, REDSTONE_LIGHT);
        register(Material.CLAY, CLAY);
        register(Material.GROUND, GROUND);
        register(Material.GRASS, GRASS);
        register(Material.PACKED_ICE, PACKED_ICE);
        register(Material.SAND, SAND);
        register(Material.SPONGE, SPONGE);
        register(Material.WOOD, WOOD);
        register(Material.CLOTH, CLOTH);
        register(Material.TNT, TNT);
        register(Material.LEAVES, LEAVES);
        register(Material.GLASS, GLASS);
        register(Material.ICE, ICE);
        register(Material.CACTUS, CACTUS);
        register(Material.ROCK, ROCK);
        register(Material.IRON, IRON);
        register(Material.CRAFTED_SNOW, CRAFTED_SNOW);
        register(Material.ANVIL, ANVIL);
        register(Material.BARRIER, BARRIER);
        register(Material.PISTON, PISTON);
        register(Material.CORAL, CORAL);
        register(Material.GOURD, GOURD);
        register(Material.DRAGON_EGG, DRAGON_EGG);
        register(Material.CAKE, CAKE);
    }

    @Override
    protected void apiPostInit() {

    }

    @Override
    protected BlockType getVanillaBlockType(BlockType.BlockTypeEnum blockType) {
        BlockType result = BlockType.getStandard(blockType);
        Validate.notNull(result, "Misses implementation for vanilla block type " + blockType.toString());
        return result;
    }

    @Override
    protected NMSBlockType registerCustom(BlockType blockType) {
        RealCustomMaterial result = new RealCustomMaterial(blockType);
        map.put(result.getForge(), blockType);
        return result;
    }

    public  static EnumPushReaction toNMS(PistonReaction pistonReaction) {
        switch (pistonReaction) {
            case BLOCK: return EnumPushReaction.BLOCK;
            case DESTROY: return EnumPushReaction.DESTROY;
            case IGNORE: return EnumPushReaction.IGNORE;
            case NORMAL: return EnumPushReaction.NORMAL;
            case PUSH_ONLY: return EnumPushReaction.PUSH_ONLY;
            default: throw new UnsupportedOperationException("Missing is just a placeholder");
        }
    }

    public  static PistonReaction toOil(EnumPushReaction pistonReaction) {
        switch (pistonReaction) {
            case BLOCK: return PistonReaction.BLOCK;
            case DESTROY: return PistonReaction.DESTROY;
            case IGNORE: return PistonReaction.IGNORE;
            case NORMAL: return PistonReaction.NORMAL;
            case PUSH_ONLY: return PistonReaction.PUSH_ONLY;
            default: return PistonReaction.MISSING;
        }
    }
}

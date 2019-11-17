package org.oilmod.oilforge.block;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.PistonReaction;
import org.oilmod.api.blocks.nms.NMSBlockType;

import static org.oilmod.api.blocks.BlockType.BlockTypeEnum.*;

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
        register(Material.STRUCTURE_VOID, STRUCTURE_VOID); //todo write test
        register(Material.PORTAL, PORTAL);
        register(Material.CARPET, CARPET);
        register(Material.PLANTS, PLANTS);
        register(Material.OCEAN_PLANT, OCEAN_PLANT);
        register(Material.TALL_PLANTS, VINE);
        register(Material.SEA_GRASS, SEA_GRASS);
        register(Material.WATER, WATER);
        register(Material.BUBBLE_COLUMN, BUBBLE_COLUMN);
        register(Material.LAVA, LAVA);
        register(Material.SNOW, SNOW);
        register(Material.FIRE, FIRE);
        register(Material.MISCELLANEOUS, CIRCUITS);
        register(Material.WEB, WEB);
        register(Material.REDSTONE_LIGHT, REDSTONE_LIGHT);
        register(Material.CLAY, CLAY);
        register(Material.EARTH, GROUND);
        register(Material.ORGANIC, GRASS);
        register(Material.PACKED_ICE, PACKED_ICE);
        register(Material.SAND, SAND);
        register(Material.SPONGE, SPONGE);
        register(Material.SHULKER, ENUM_MISSING);
        register(Material.WOOD, WOOD);
        register(Material.BAMBOO_SAPLING, ENUM_MISSING);
        register(Material.BAMBOO, ENUM_MISSING);
        register(Material.WOOL, CLOTH);
        register(Material.TNT, TNT);
        register(Material.LEAVES, LEAVES);
        register(Material.GLASS, GLASS);
        register(Material.ICE, ICE);
        register(Material.CACTUS, CACTUS);
        register(Material.ROCK, ROCK);
        register(Material.IRON, IRON);
        register(Material.SNOW_BLOCK, CRAFTED_SNOW);
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

    public  static PushReaction toNMS(PistonReaction pistonReaction) {
        switch (pistonReaction) {
            case BLOCK: return PushReaction.BLOCK;
            case DESTROY: return PushReaction.DESTROY;
            case IGNORE: return PushReaction.IGNORE;
            case NORMAL: return PushReaction.NORMAL;
            case PUSH_ONLY: return PushReaction.PUSH_ONLY;
            default: throw new UnsupportedOperationException("Missing is just a placeholder");
        }
    }

    public  static PistonReaction toOil(PushReaction pistonReaction) {
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

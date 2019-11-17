package org.oilmod.oilforge.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.MapColor;
import org.oilmod.api.blocks.PistonReaction;
import org.oilmod.api.rep.block.BlockStateRep;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.rep.block.BlockFR;

public class RealBlockType extends BlockType {
    public RealBlockType(Material material, BlockTypeEnum blockTypeEnum) {
        super(new MaterialWrapper(material), OilMain.ModMinecraft.createKey(blockTypeEnum.toString().toLowerCase()), blockTypeEnum);
    }

    @Override
    public MaterialWrapper getNmsBlockType() {
        return (MaterialWrapper)super.getNmsBlockType();
    }

    public Material getForge() {
        return getNmsBlockType().getForge();
    }

    @Override
    public boolean isLiquid() {
        return getForge().isLiquid();
    }

    @Override
    public boolean isBuildable() {
        return getForge().blocksMovement(); //maybe?
    }

    @Override
    public boolean isOpaque() {
        return getForge().isOpaque();
    }

    @Override
    public boolean isSolid() {
        return getForge().isSolid();
    }

    @Override
    public boolean isFlammable() {
        return getForge().isFlammable();
    }

    @Override
    public boolean isReplaceable() {
        return getForge().isReplaceable();
    }

    @Override
    public boolean isAlwaysDestroyable() {
        return getForge().isToolNotRequired();
    }

    //todo need some generic test, also these are to be deleted anyway

    @Override
    public boolean breakablePickaxe(BlockStateRep blockstate) {
        Block block = ((BlockFR)blockstate.getBlock()).getForge();
        switch (getBlockTypeEnum()) {
            case ANVIL: //Inferred by ItemPickaxe KEEPSYNC
            case IRON:
            case ROCK:
            case PACKED_ICE: //Inferred by blocks
                return true;
            case CIRCUITS: //Unit test helps here
                return block == Blocks.POWERED_RAIL || block == Blocks.DETECTOR_RAIL  || block == Blocks.RAIL || block == Blocks.STONE_BUTTON || block == Blocks.ACTIVATOR_RAIL;
            case ICE:
                return block == Blocks.ICE;
            default:
                return false;
        }
    }

    @Override
    public boolean breakableAxe(BlockStateRep blockstate) {
        Block block = ((BlockFR)blockstate.getBlock()).getForge();
        switch (getBlockTypeEnum()) {
            case WOOD: //Inferred by ItemAxe KEEPSYNC
            case PLANTS:
            case VINE:
            case GOURD: //Inferred by blocks
                return true;
            case CIRCUITS: //Unit test helps here
                return block == Blocks.LADDER || block == Blocks.BIRCH_BUTTON || block == Blocks.ACACIA_BUTTON || block == Blocks.DARK_OAK_BUTTON || block == Blocks.JUNGLE_BUTTON || block == Blocks.OAK_BUTTON || block == Blocks.SPRUCE_BUTTON;
            default:
                return false;
        }
    }

    @Override
    public boolean breakableShovel(BlockStateRep blockstate) {
        Block block = ((BlockFR)blockstate.getBlock()).getForge();
        switch (getBlockTypeEnum()) {
            case SAND: //Inferred by blocks ItemSpade KEEPSYNC
            case GROUND:
            case CRAFTED_SNOW:
            case SNOW:
                return true;
            case GRASS://Unit test helps here
                return block == Blocks.GRASS || block == Blocks.MYCELIUM;
            case CLAY:
                return block == Blocks.CLAY;
            default:
                return false;
        }
    }

    @Override
    public boolean breakableShears(BlockStateRep block) {
        switch (getBlockTypeEnum()) {
            case PLANTS: //Inferred by blocks ItemShears KEEPSYNC
            case VINE:
            case LEAVES:
            case CARPET:
            case CLOTH:
            case WEB:
            case REDSTONE_LIGHT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean breakableBlade(BlockStateRep block) {
        switch (getBlockTypeEnum()) {
            case PLANTS: //Inferred by ItemSword KEEPSYNC
            case VINE:
            case CORAL:
            case LEAVES:
            case GOURD:
            case WEB: //Inferred by blocks
            case REDSTONE_LIGHT: //TODO check this one
                return true;
            default:
                return false;
        }
    }

    @Override
    public PistonReaction getPistonReaction() {
        return RealBlockTypeHelper.toOil(getForge().getPushReaction());
    }

    @Override
    public MapColor getColor() {
        return null; //TODO: implement when MapColor actually exists
    }
}

package org.oilmod.oilforge;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.stdimpl.world.LocFactoryImpl;
import org.oilmod.api.rep.world.LocationBlockRep;
import org.oilmod.api.rep.world.LocationEntityRep;
import org.oilmod.api.rep.world.LocationRep;
import org.oilmod.api.rep.world.VectorRep;
import org.oilmod.api.util.InteractionResult;
import org.oilmod.oilforge.block.RealBlockType;
import org.oilmod.oilforge.block.RealBlockTypeHelper;
import org.oilmod.oilforge.items.RealItemStack;
import org.oilmod.oilforge.rep.block.BlockFR;
import org.oilmod.oilforge.rep.block.BlockStateFR;
import org.oilmod.oilforge.rep.entity.EntityFR;
import org.oilmod.oilforge.rep.entity.EntityHumanFR;
import org.oilmod.oilforge.rep.entity.EntityLivingBaseFR;
import org.oilmod.oilforge.rep.entity.EntityLivingFR;
import org.oilmod.oilforge.rep.inventory.InventoryFR;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;
import org.oilmod.oilforge.rep.itemstack.RealItemStackFactory;
import org.oilmod.oilforge.rep.location.LocationBlockFR;
import org.oilmod.oilforge.rep.location.WorldFR;

import java.util.List;

public final class Util {
    //todo use mixins



    //ItemStack
    public static ItemStack toForge(OilItemStack stack) {
        return ((RealItemStack)stack.getNmsItemStack()).getForgeItemStack();
    }

    public static ItemStack toForge(ItemStackRep stack) {
        return ((ItemStackFR)stack).getForge();
    }

    public static ItemStackRep[] toOil(List<ItemStack> stacks) {
        ItemStackRep[] result = new ItemStackRep[stacks.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = toOil(stacks.get(i));
        }
        return result;
    }


    public static ItemFR toOil(Item item) {
        return new ItemFR(item);
    }
    public static ItemStackFR toOil(ItemStack stack) {
        return RealItemStackFactory.INSTANCE.create(stack);
    }

    public static InteractionResult toOil(EnumActionResult nms) {
        switch (nms) {
            case SUCCESS:
                return InteractionResult.SUCCESS;
            case PASS:
                return InteractionResult.PASS;
            case FAIL:
                return InteractionResult.FAIL;
            default:
                return InteractionResult.NONE;
        }
    }



    public static EnumActionResult toForge(InteractionResult oil) {
        switch (oil) {
            case SUCCESS:
                return EnumActionResult.SUCCESS;
            case FAIL:
                return EnumActionResult.FAIL;
            case PASS:
            case NONE:
            default:
                return EnumActionResult.PASS;
        }
    }

    //Location stuff
    public static WorldFR toOil(World w) {
        return new WorldFR(w);
    }

    public static LocationRep toOil(Vec3d vec, World w) {
        return LocFactoryImpl.getInstance().createLocation(vec.x, vec.y, vec.z, toOil(w));
    }

    public static VectorRep toOil(Vec3d vec) {
        return LocFactoryImpl.getInstance().createVector(vec.x, vec.y, vec.z);
    }

    public static LocationBlockRep toOil(BlockPos pos, World w) {
        return LocFactoryImpl.getInstance().createBlockLocation(pos.getX(), pos.getY(), pos.getZ(), toOil(w));
    }

    public static LocationEntityRep toOil(Vec3d vec, double yaw, double pitch,  World w) {
        return LocFactoryImpl.getInstance().createEntityLocation(vec.x, vec.y, vec.z, yaw, pitch, toOil(w));
    }


    //Entity stuff

    public static EntityFR toOil(Entity e) {
        return new EntityFR(e);
    }
    public static EntityLivingFR toOil(EntityLiving e) {
        return new EntityLivingFR(e);
    }
    public static EntityHumanFR toOil(EntityPlayer e) {
        return new EntityHumanFR(e);
    }

    //block
    public static BlockFR toOil(Block block) {
        return new BlockFR(block);
    }
    public static BlockStateFR toOil(IBlockState state) {
        return new BlockStateFR(state);
    }

    public static BlockType toOil(Material mat) {
        return ((RealBlockTypeHelper)BlockType.BlockTypeHelper.getInstance()).get(mat);
    }

    public static BlockFaceRep toOil(EnumFacing notch) {
        switch (notch) {
            case DOWN:
                return BlockFaceRep.DOWN;
            case UP:
                return BlockFaceRep.UP;
            case NORTH:
                return BlockFaceRep.NORTH;
            case SOUTH:
                return BlockFaceRep.SOUTH;
            case WEST:
                return BlockFaceRep.WEST;
            case EAST:
                return BlockFaceRep.EAST;
        }
        throw new IllegalStateException("invalid blockface");
    }


    //inventory
    public static InventoryRep toOil(IInventory inv) {
        return new InventoryFR(inv);
    }



    public static EntityLivingBase toForge(EntityLivingRep entity) {
        return ((EntityLivingBaseFR)entity).getForge();
    }


    public static BlockPos toForge(LocationBlockRep loc) {
        return ((LocationBlockFR)loc).getPos();
    }


    public static EnumFacing toForge(BlockFaceRep face) {
        switch (face) {
            case DOWN:
                return EnumFacing.DOWN;
            case UP:
                return EnumFacing.UP;
            case NORTH:
                return EnumFacing.NORTH;
            case SOUTH:
                return EnumFacing.SOUTH;
            case WEST:
                return EnumFacing.WEST;
            case EAST:
                return EnumFacing.EAST;
            case SELF:
            default:
                throw new IllegalStateException("self cannot be represented, invalid blockface");
        }
    }
}

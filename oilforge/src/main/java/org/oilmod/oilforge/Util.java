package org.oilmod.oilforge;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.oilmod.api.blocks.BlockType;
import org.oilmod.api.blocks.MapColor;
import org.oilmod.api.items.EnchantmentType;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.block.BlockFaceRep;
import org.oilmod.api.rep.block.BlockRep;
import org.oilmod.api.rep.enchant.EnchantmentRep;
import org.oilmod.api.rep.entity.EntityLivingRep;
import org.oilmod.api.rep.entity.EntityPlayerRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.stdimpl.world.LocFactoryImpl;
import org.oilmod.api.rep.world.*;
import org.oilmod.api.stateable.complex.IComplexState;
import org.oilmod.api.stateable.enumerable.IEnumerableState;
import org.oilmod.api.util.InteractionResult;
import net.minecraft.util.Direction;
import org.oilmod.api.util.OilKey;
import org.oilmod.oilforge.block.RealBlockType;
import org.oilmod.oilforge.block.RealBlockTypeHelper;
import org.oilmod.oilforge.block.tileentity.RealTileEntity;
import org.oilmod.oilforge.enchantments.RealEnchantmentTypeHelper;
import org.oilmod.oilforge.inventory.OilIInventory;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.items.RealItemStack;
import org.oilmod.oilforge.items.capability.OilItemStackHandler;
import org.oilmod.oilforge.mixin.IEntityCache;
import org.oilmod.oilforge.rep.block.BlockFR;
import org.oilmod.oilforge.rep.block.BlockStateFR;
import org.oilmod.oilforge.rep.enchantment.EnchantmentFR;
import org.oilmod.oilforge.rep.entity.EntityFR;
import org.oilmod.oilforge.rep.entity.EntityPlayerFR;
import org.oilmod.oilforge.rep.entity.LivingEntityFR;
import org.oilmod.oilforge.rep.inventory.IItemHandlerInventoryFR;
import org.oilmod.oilforge.rep.inventory.InventoryFR;
import org.oilmod.oilforge.rep.item.BlockItemFR;
import org.oilmod.oilforge.rep.item.ItemFR;
import org.oilmod.oilforge.rep.itemstack.ItemStackFR;
import org.oilmod.oilforge.rep.itemstack.RealItemStackFactory;
import org.oilmod.oilforge.rep.location.LocationBlockFR;
import org.oilmod.oilforge.rep.location.WorldFR;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    public static Item toForge(ItemRep item) {
        return ((ItemFR)item).getForge();
    }

    public static ItemStackRep[] toOil(List<ItemStack> stacks) {
        ItemStackRep[] result = new ItemStackRep[stacks.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = toOil(stacks.get(i));
        }
        return result;
    }


    public static ItemFR toOil(Item item) {
        if (item instanceof BlockItem)return toOil((BlockItem)item);
        return new ItemFR(item);
    }

    public static BlockItemFR toOil(BlockItem item) {
        return new BlockItemFR(item);
    }
    public static ItemStackFR toOil(ItemStack stack) {
        return RealItemStackFactory.INSTANCE.create(stack);
    }

    public static InteractionResult toOil(ActionResultType nms) {
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



    public static ActionResultType toForge(InteractionResult oil) {
        switch (oil) {
            case SUCCESS:
                return ActionResultType.SUCCESS;
            case FAIL:
                return ActionResultType.FAIL;
            case PASS:
            case NONE:
            default:
                return ActionResultType.PASS;
        }
    }

    public static boolean isModStack(ItemStack stack) {
        //might not have item set yet so we need to test both
        return stack.getItem() instanceof RealItemImplHelper || stack.getCapability(OilItemStackHandler.CAPABILITY).isPresent();
    }

    public static RealItemStack toReal(ItemStack stack) {
        return stack.getCapability(OilItemStackHandler.CAPABILITY).orElseThrow(() -> new IllegalStateException("No RealItemStack instance supplied"));
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
        return ((IEntityCache)e).getOilEntityRep();
    }
    public static LivingEntityFR toOil(MobEntity e) {
        return ((IEntityCache)e).getOilEntityRep();
    }
    public static EntityPlayerFR toOil(PlayerEntity e) {
        return ((IEntityCache)e).getOilEntityRep();
    }

    //block
    public static BlockFR toOil(Block block) {
        return new BlockFR(block);
    }
    public static Block toForge(BlockRep blockRep) {
        return ((BlockFR)blockRep).getForge();
    }

    public static BlockStateFR toOil(BlockState state) {
        return new BlockStateFR(state);
    }
    public static IEnumerableState toOilState(BlockState state) {
        return null; //todo
    }
    public static IComplexState toOilState(TileEntity te) {
        return te instanceof RealTileEntity ?((RealTileEntity<?>) te).getComplexState():null;
    }

    public static BlockType toOil(Material mat) {
        return ((RealBlockTypeHelper) BlockType.Helper.getInstance()).get(mat);
    }

    public static BlockFaceRep toOil(Direction notch) {
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

    public static Material toForge(BlockType blockType) {
        return ((RealBlockType)blockType).getForge();
    }

    public static MaterialColor toForge(MapColor blockType) {
        return MaterialColor.ADOBE;//todo add logic, placeholder
    }


    //inventory
    public static InventoryRep toOil(IInventory inv) {
        return inv instanceof OilIInventory?((OilIInventory<?>) inv).getInventoryRep():InventoryFR.createInventory(inv);
    }

    public static IInventory toForge(InventoryRep inv) {
        return ((InventoryFR)inv).getForge();
    }

    public static LivingEntity toForge(EntityLivingRep entity) {
        return ((LivingEntityFR)entity).getForge();
    }



    public static PlayerEntity toForge(EntityPlayerRep entity) {
        return ((EntityPlayerFR)entity).getForge();
    }


    public static BlockPos toForge(LocationBlockRep loc) {
        return ((LocationBlockFR)loc).getPos();
    }


    public static Direction toForge(BlockFaceRep face) {
        switch (face) {
            case DOWN:
                return Direction.DOWN;
            case UP:
                return Direction.UP;
            case NORTH:
                return Direction.NORTH;
            case SOUTH:
                return Direction.SOUTH;
            case WEST:
                return Direction.WEST;
            case EAST:
                return Direction.EAST;
            case SELF:
            default:
                throw new IllegalStateException("self cannot be represented, invalid blockface");
        }
    }

    public static IItemHandlerInventoryFR toOil(IItemHandler itemHandler) {
        return new IItemHandlerInventoryFR(itemHandler);
    }

    //enchantments

    public static EnchantmentRep toOil(Enchantment ench) {
        return new EnchantmentFR(ench);
    }

    public static Enchantment toForge(EnchantmentRep item) {
        return ((EnchantmentFR)item).getForge();
    }

    public static EnchantmentType toOil(net.minecraft.enchantment.EnchantmentType forge) {
        return ((RealEnchantmentTypeHelper)EnchantmentType.EnchantmentTypeHelper.getInstance()).convertToOil(forge);
    }

    public static World toForge(WorldRep world) {
        return ((WorldFR)world).getForge();
    }


    private static Constructor<ItemUseContext> ItemUseContextCtor;
    static {
        try {
            ItemUseContextCtor = ItemUseContext.class.getDeclaredConstructor(World.class, PlayerEntity.class, Hand.class, ItemStack.class, BlockRayTraceResult.class);
            ItemUseContextCtor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    //todo make ItemUseContext not protect #AT
    public static ItemUseContext createItemUseContext(World worldIn, @Nullable PlayerEntity player, Hand handIn, ItemStack heldItem, BlockRayTraceResult rayTraceResultIn) {
        try {
            return ItemUseContextCtor.newInstance(worldIn, player, handIn, heldItem, rayTraceResultIn);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static ResourceLocation toForge(OilKey key) {
        return ((NMSKeyImpl)key.getNmsKey()).resourceLocation;
    }
}

package org.oilmod.oilforge.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.type.IDurable;
import org.oilmod.api.items.type.IPickaxe;
import org.oilmod.api.items.type.IToolBlockBreaking;
import org.oilmod.oilforge.NMSKeyImpl;
import org.oilmod.oilforge.items.RealItemImplHelper;
import org.oilmod.oilforge.rep.item.ItemFR;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.oilmod.oilforge.items.RealItem.createBuilder;

public class RealPickaxe extends ItemPickaxe implements RealItemImplHelper {

    private final OilItem apiItem;



    public RealPickaxe(OilItem oilItem) {
        super(new IItemTier() {
            @Override
            public int getMaxUses() {
                return (oilItem instanceof IDurable)?((IDurable)oilItem).getMaxDurability():0;
            }

            @Override
            public float getEfficiency() {
                return (oilItem instanceof IToolBlockBreaking)?((IToolBlockBreaking)oilItem).getDestroySpeed(null):0;
            }

            @Override
            public float getAttackDamage() {
                return 0;
            }

            @Override
            public int getHarvestLevel() {
                return (oilItem instanceof IPickaxe)?((IPickaxe)oilItem).getPickaxeStrength():0;
            }

            @Override
            public int getEnchantability() {
                return oilItem.getItemEnchantability();
            }

            @Override
            public Ingredient getRepairMaterial() {
                return null;
            }
        }, 0, 1, createBuilder(oilItem).addToolType());
        this.apiItem = oilItem;
        setRegistryName(((NMSKeyImpl) apiItem.getOilKey().getNmsKey()).resourceLocation);
    }

    public OilItem getApiItem() {
        return apiItem;
    }

    //NMS



    public boolean canHarvestBlock(ItemStack stack, IBlockState state) {
        return RealItemImplHelper.super.canHarvestBlock(stack, state);
    }

    public int getItemEnchantability() {
        return RealItemImplHelper.super.getItemEnchantability();
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return RealItemImplHelper.super.getDestroySpeed(stack, state);
    }

    @Nonnull
    public EnumActionResult onItemUse(ItemUseContext context) {
        return RealItemImplHelper.super.onItemUse(context);
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return RealItemImplHelper.super.onBlockDestroyed(stack,worldIn,state,pos,entityLiving);
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return RealItemImplHelper.super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    public NBTTagCompound getShareTag(ItemStack stack) {
        return RealItemImplHelper.super.getShareTag(stack);
    }

    public void readShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        RealItemImplHelper.super.readShareTag(stack, nbt);
    }
}

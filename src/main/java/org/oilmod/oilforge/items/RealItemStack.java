package org.oilmod.oilforge.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import org.oilmod.api.OilMod;
import org.oilmod.api.config.Compound;
import org.oilmod.api.data.IData;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.config.nbttag.NBTCompound;
import org.oilmod.oilforge.rep.itemstack.OilModItemStackFR;

import java.util.Map;

public class RealItemStack implements NMSItemStack, INBTSerializable<NBTTagCompound> {
    public final static RealItemStack EMPTY = new RealItemStack();
    private final ItemStack itemStack;
    private final OilItemStack oilItemStack;
    private final OilModItemStackFR itemStackRep;

    public RealItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.oilItemStack = getItem().getApiItem().createOilStack(this);
        this.itemStackRep = new OilModItemStackFR(this);
    }

    public RealItemStack(ItemStack itemStack, OilItem oilItem) {
        this.itemStack = itemStack;
        this.oilItemStack = oilItem.createOilStack(this);
        this.itemStackRep = new OilModItemStackFR(this);
    }

    private RealItemStack() {
        if (EMPTY != null) throw new IllegalStateException("Cannot use this constructor for anything but example empty instance");
        this.itemStack = ItemStack.EMPTY;
        this.oilItemStack = new OilItemStack(this, new OilItem(OilMain.ModOilMod.createKey("empty_oilstate") ,MinecraftItem.STICK, "empty_oilstate"){});
        this.itemStackRep = new OilModItemStackFR(this);
    }

    public RealItemImplHelper getItem() {
        return (RealItemImplHelper) itemStack.getItem();
    }

    public ItemStack getForgeItemStack() {
        return itemStack;
    }

    @Override
    public OilModItemStackFR asItemStackRep() {
        return itemStackRep;
    }

    @Override
    public OilItemStack getOilItemStack() {
        return oilItemStack;
    }

    @Override
    public int getDataNMS() {
        return itemStack.getDamage();
    }

    @Override
    public void setDataNMS(int i) {
        itemStack.setDamage(i);
    }

    @Override
    public String getRenameNMS() {
        return itemStack.getDisplayName().getFormattedText();
    }

    @Override
    public void setRenameNMS(String s) {
        itemStack.setDisplayName(new TextComponentString(s));
    }

    @Override
    public boolean isRenamedNMS() {
        return itemStack.hasDisplayName();
    }


    public Compound saveModData(Compound compound) {
        for (Map.Entry<String, IData<?>> entry:oilItemStack.getRegisteredIData().entrySet()) {
            entry.getValue().saveTo(compound, entry.getKey());
        }
        return compound;
    }

    public Compound loadModData(Compound compound) {
        for (Map.Entry<String, IData<?>> entry:oilItemStack.getRegisteredIData().entrySet()) {
            entry.getValue().loadFrom(compound, entry.getKey());
        }
        return compound;
    }

    //todo BUG: as we dont use mixin handleDamage cannot be detected from
    public boolean handleDamageVanilla(int amount, EntityLivingBase damager) {
        //taken from attemptDamageItem()'s end todo use mixins for generification instead
        ItemStack stack = getForgeItemStack();

        if (damager instanceof EntityPlayerMP && amount != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger((EntityPlayerMP) damager, stack, stack.getDamage() + amount);
        }

        int l = stack.getDamage() + amount;
        stack.setDamage(l);
        OilMain.printTrace("handleDamageVanilla");
        return l >= stack.getMaxDamage();

    }

    boolean isValid() {
        return getForgeItemStack().getItem() instanceof RealItemImplHelper;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        //actually this crashed mc
        //if (oilItemStack.getRegisteredIData().size()==0)return null; //we dont need empty caps everywhere
        NBTCompound result = new NBTCompound();
        saveModData(result);
        return result.getNBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTCompound oilNBT = new NBTCompound((NBTTagCompound) nbt);
        loadModData(oilNBT);
    }

}

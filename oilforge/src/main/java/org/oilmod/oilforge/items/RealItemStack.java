package org.oilmod.oilforge.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.api.config.Compound;
import org.oilmod.api.data.IData;
import org.oilmod.api.items.NMSItemStack;
import org.oilmod.api.items.OilItem;
import org.oilmod.api.items.OilItemStack;
import org.oilmod.api.rep.providers.minecraft.MinecraftItem;
import org.oilmod.oilforge.OilMain;
import org.oilmod.oilforge.config.nbttag.OilNBTCompound;
import org.oilmod.oilforge.rep.itemstack.OilModItemStackFR;

import java.util.Map;

public class RealItemStack implements NMSItemStack, INBTSerializable<CompoundNBT> {
    public static final Logger LOGGER = LogManager.getLogger();
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
        LOGGER.trace("created RealItemStack for {}", oilItem::getOilKey);
        this.itemStack = itemStack;
        this.oilItemStack = oilItem.createOilStack(this);
        this.itemStackRep = new OilModItemStackFR(this);
    }

    private RealItemStack() {
        if (EMPTY != null) throw new IllegalStateException("Cannot use this constructor for anything but example empty instance");
        this.itemStack = ItemStack.EMPTY;
        OilItem fake = new OilItem(MinecraftItem.STICK, "empty_oilstate"){};
        fake.setOilKey(OilMain.ModOilMod.createKey("empty_oilstate"));

        this.oilItemStack = new OilItemStack(this, fake);
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
        itemStack.setDisplayName(new StringTextComponent(s));
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
    public boolean handleDamageVanilla(int amount, LivingEntity damager) {
        //taken from attemptDamageItem()'s end todo use mixins for generification instead
        ItemStack stack = getForgeItemStack();

        if (damager instanceof ServerPlayerEntity && amount != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger((ServerPlayerEntity) damager, stack, stack.getDamage() + amount);
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
    public CompoundNBT serializeNBT() {
        //actually this crashed mc
        //if (oilItemStack.getRegisteredIData().size()==0)return null; //we dont need empty caps everywhere
        OilNBTCompound result = new OilNBTCompound();
        saveModData(result);
        //LOGGER.debug("serialize item {} to NBT: {}", this::getItem, ()->result.getCompoundNBT().toFormattedComponent().getFormattedText());
        return result.getCompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        //LOGGER.debug("deserialize item {} from NBT: {}", this::getItem, ()->nbt.toFormattedComponent().getFormattedText());
        OilNBTCompound oilNBT = new OilNBTCompound(nbt);
        loadModData(oilNBT);
    }

}

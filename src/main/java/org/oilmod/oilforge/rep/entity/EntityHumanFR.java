package org.oilmod.oilforge.rep.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.IInteractionObject;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.rep.entity.EntityHumanRep;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.InventoryUIRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.oilforge.modloader.OilModLoaderMod;
import org.oilmod.oilforge.rep.inventory.InventoryFR;

import static org.oilmod.oilforge.Util.toOil;

public class EntityHumanFR extends EntityLivingBaseFR implements EntityHumanRep {


    public EntityHumanFR(EntityPlayer forge) {
        super(forge);
    }

    @Override
    public EntityPlayer getForge() {
        return (EntityPlayer) super.getForge();
    }

    @Override
    public String getName() {
        return getForge().getName().getUnformattedComponentText();
    }

    @Override
    public InventoryUIRep openInventory(InventoryRep inventory) {
        IInventory inv = ((InventoryFR)inventory).getForge();
        EntityPlayer player = getForge();
        player.displayGUIChest(inv);
        return null;
    }

    @Override
    public ItemStackRep getItemInHand() {
        return toOil(getForge().getHeldItem(getForge().getActiveHand()));
    }

    @Override
    public ItemStackRep getItemOnCursor() {
        throw new NotImplementedException("to be implemented"); //todo implement
        //return toOil(getForge().);
    }

    @Override
    public void setItemOnCursor(ItemStackRep item) {
        throw new NotImplementedException("to be implemented"); //todo implement
    }

    @Override
    public boolean isSleeping() {
        return getForge().isPlayerSleeping();
    }

    @Override
    public int getSleepTicks() {
        return getForge().getSleepTimer();
    }

    @Override
    public boolean isBlocking() {
        return getForge().isActiveItemStackBlocking();
    }

    @Override
    public int getExpToLevel() {//todo: consider removing, add xp api
        return getForge().xpBarCap();
    }

    @Override
    public boolean isSneaking() {
        return getForge().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneak) {
        getForge().setSneaking(sneak);
    }

    @Override
    public boolean isSprinting() {
        return getForge().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getForge().setSprinting(sprinting);
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        throw new NotImplementedException("not implementable"); //todo remove
    }

    @Override
    public boolean getCanPickupItems() {
        throw new NotImplementedException("not implementable");  //todo remove
    }

    @Override
    public void setAI(boolean ai) {
        throw new NotImplementedException("not implementable");  //todo remove
    }

    @Override
    public boolean hasAI() {
        throw new NotImplementedException("not implementable");  //todo remove
    }
}

package org.oilmod.oilforge.rep.itemstack;

import net.minecraft.item.ItemStack;
import org.oilmod.api.rep.item.ItemRep;
import org.oilmod.api.rep.itemstack.ItemStackRep;
import org.oilmod.api.rep.itemstack.state.ItemStackStateRep;
import org.oilmod.oilforge.rep.item.ItemFR;

import static org.oilmod.oilforge.Util.toOil;

public class ItemStackFR implements ItemStackRep {
    private final ItemStack forge;
    private final ItemRep item; //tmp until itemrep is saved with mixin
    private final ItemStackStateFR state;

    public ItemStack getForge() {
        return forge;
    }

    public ItemStackFR(ItemStack forge) {
        this.forge = forge;
        this.item = toOil(forge.getItem());
        this.state = new ItemStackStateFR(forge);
    }

    protected ItemStackFR(ItemFR item, ItemStackStateFR state, int amount) {
        this.forge  = new ItemStack(item.getForge(), amount);
        if (state != null)state.applyTo(this);
        this.item = toOil(forge.getItem()); //might have changed so need to get it from stack
        this.state = new ItemStackStateFR(this.forge);
    }

    @Override
    public ItemRep getItem() {
        return item;
    }

    @Override
    public ItemStackStateRep getItemStackState() {
        return state;
    }

    @Override
    public int getAmount() {
        return forge.getCount();
    }

    @Override
    public void setAmount(int i) {
        forge.setCount(i);
    }

    /*@Override
    public void setDurability(short i) {
        forge.setDamage(i);
    }

    @Override
    public short getDurability() {
        return (short) forge.getDamage(); //TODO why is this short?!
    }*/

    @Override
    public int getMaxStackSize() {
        return forge.getMaxStackSize();
    }

    @Override
    public boolean isSimilar(ItemStackRep itemStackRep) {
        ItemStack forge2 = ((ItemStackFR)itemStackRep).forge;
        return ItemStack.areItemsEqual(forge, forge2);
    }

    @Override
    public ItemStackRep copy() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStackRep) {
            ItemStackRep stack2 = (ItemStackRep) obj;
            return isSimilar(stack2) && getAmount() == stack2.getAmount();
        }
        return super.equals(obj);
    }

    @Override
    public ItemStackRep clone() {
        return toOil(forge.copy());
    }

    /*@Override
    public int getEnchantmentLevel(EnchantmentRep enchantmentRep) {
        return EnchantmentHelper.getEnchantmentLevel(((EnchantmentFR)enchantmentRep).getForge(), getForge());
    }

    @Override
    public void addEnchantment(EnchantmentRep enchantmentRep, int level, boolean force) {
        //todo check if applicable
        forge.addEnchantment(((EnchantmentFR)enchantmentRep).getForge(), level);
    }

    @Override
    public int removeEnchantment(EnchantmentRep enchantmentRep) {
        int result = getEnchantmentLevel(enchantmentRep); //todo is this really needed, make nicer
        forge.addEnchantment(((EnchantmentFR)enchantmentRep).getForge(), 0);
        return result;
    }*/

    @Override
    public boolean isEmpty() {
        return forge.isEmpty();
    }
}

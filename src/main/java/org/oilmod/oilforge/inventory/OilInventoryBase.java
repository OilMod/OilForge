package org.oilmod.oilforge.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;
import org.oilmod.api.config.Compound;
import org.oilmod.api.inventory.ModInventoryObjectBase;
import org.oilmod.api.rep.inventory.InventoryHolderRep;
import org.oilmod.api.userinterface.IInteractableUIElement;
import org.oilmod.api.util.ITicker;
import org.oilmod.oilforge.items.TempRealItemHelper;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * Created by sirati97 on 13.02.2016.
 */
public abstract class OilInventoryBase<APIObject extends ModInventoryObjectBase> extends InventoryBasic implements OilIInventory<APIObject> {
    private final WeakReference<InventoryHolderRep> owner;
    private final IItemFilter itemFilter;
    public final NonNullList<ItemStack> items;
    public final List<ItemStack> itemsReadOnly;
    private WeakReference<APIObject> modInventoryObject;
    private ITicker ticker;
    private final boolean needsOwner;
    private World world;


    public OilInventoryBase(InventoryHolderRep owner, String title, int size, ITicker ticker, IItemFilter itemFilter, boolean needsOwner) {
        super(new TextComponentString(title), size);
        this.items = initItems(size);
        this.itemsReadOnly = Collections.unmodifiableList(this.items);
        this.owner = new WeakReference<>(owner);
        this.itemFilter = itemFilter==null?new NoItemFilter():itemFilter;
        this.ticker = ticker;
        this.needsOwner = needsOwner;
        if (isTickable() && ticker != null) {
            ticker.add(this);
        }
    }

    protected NonNullList<ItemStack> initItems(int size) {
        return NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public InventoryHolderRep getOwner() {
        return owner.get();
    }

    @Override
    public IItemFilter getItemFilter() {
        return itemFilter;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return super.isItemValidForSlot(index, stack) && getItemFilter().allowed(stack);
    }

    @Override
    public boolean isValid() {
        return !needsOwner || getOwner()!=null;
    }

    @Override
    public void load(Compound compound) {
        TempRealItemHelper.loadItemsFromCompound(compound, this.items, "Items");
    }

    @Override
    public void save(Compound compound) {
        TempRealItemHelper.saveItemsToCompound(compound, this.items, "Items");
    }

    @Override
    public void setOilApiMirror(APIObject modInventoryObject) {
        this.modInventoryObject = new WeakReference<>(modInventoryObject);
    }

    public APIObject getModInventoryObject() {
        return modInventoryObject.get();
    }

    @Override
    public IInteractableUIElement createUIElement() {
        throw new NotImplementedException("todo"); //todo
        //return new OilInventoryViewSlot(this);
    }

    @Override
    public void clear() {
        items.clear();
    }

    public List<ItemStack> getItems() {
        return itemsReadOnly;
    }

    @Override
    public void setTitle(String s) {
        super.setCustomName(new TextComponentString(s));
    }
}

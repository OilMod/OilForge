package org.oilmod.oilforge.ui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.UI.IItemElement;
import org.oilmod.api.UI.IItemInteractionHandler;
import org.oilmod.api.UI.IItemRef;
import org.oilmod.api.rep.inventory.InventoryRep;
import org.oilmod.api.rep.inventory.InventoryView;
import org.oilmod.api.rep.itemstack.ItemStackRep;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.oilmod.oilforge.Util.toForge;
import static org.oilmod.oilforge.Util.toOil;


public class RealItemRef implements IItemRef {
    private boolean isNative;
    private InventoryRep invCustom;
    private IInventory invNative;
    private final List<IItemElement> trace = new ObjectArrayList<>();
    private int slotId;
    private int localRow;
    private int globalRow;
    private int localColumn;
    private int globalColumn;

    //these are flexible
    private IRealItemInteractionHandler handler;

    protected int toIndex() {
        IItemElement element = trace.get(trace.size()-1);
        return element.toIndex(localRow, localColumn);
    }

    public RealItemRef() {
        reset();
    }

    @Override
    public boolean isNative() {
        return isNative;
    }

    @Override
    public InventoryRep getRelatedInventory() {
        return isNative?toOil(invNative):invCustom;
    }

    @Override
    public int getGlobalColumn() {
        return globalColumn;
    }

    @Override
    public int getGlobalRow() {
        return globalRow;
    }

    @Override
    public int getLocalColumn() {
        return localColumn;
    }

    @Override
    public int getLocalRow() {
        return localRow;
    }

    @Override
    public int getSlotId() {
        return slotId;
    }

    @Override
    public List<IItemElement> getTrace() {
        return trace;
    }


    @Override
    public void next(IItemElement element, int row, int column) {
        if (trace.size() == 0) {
            globalRow = row;
            globalColumn = column;
        }
        localRow = row;
        localColumn = column;
        trace.add(element);
        element.handle(this);
    }

    public void fixIndexForView(InventoryView view) {
        localRow += view.getTopOff();
        localColumn += view.getLeftOff();
    }

    private void assertUntouched() {
        Validate.isTrue(invCustom == null && invNative == null, "Cannot set resolving inventory more than once");
    }

    public void setNative(IInventory inv) {
        assertUntouched();
        isNative = true;
        invNative = inv;
        handler = UIHelper.nativeHandler;
    }


    public void setCustom(InventoryRep inv) {
        assertUntouched();
        isNative = false;
        invCustom = inv;
        throw new NotImplementedException("todo");
    }

    public void setCustom(InventoryRep inv, IItemInteractionHandler handler) {
        assertUntouched();
        isNative = false;
        invCustom = inv;
        if (handler.getNMS() instanceof IItemInteractionHandler) {
            this.handler = (IRealItemInteractionHandler) handler.getNMS();
        } else {
            this.handler = new CustomItemInteractionHandlerWrapper(handler);
            handler.setNMS(this.handler);
        }
    }

    public IRealItemInteractionHandler getHandler() {
        return handler;
    }

    public void reset() {
        invNative = null;
        invCustom = null;

        slotId = -1;
        globalRow = -1;
        globalColumn = -1;
        localRow = -1;
        localColumn = -1;

        trace.clear();

        handler = null;
    }


    public IInventory getInvNative() {
        return invNative;
    }

    public InventoryRep getInvCustom() {
        return invCustom;
    }
}

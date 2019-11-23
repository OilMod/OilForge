package org.oilmod.oilforge.inventory.container;

import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oilmod.oilforge.inventory.IItemFilter;

public class FlexChestPacket {
    public static final Logger LOGGER = LogManager.getLogger();
    public final SetItemFilterPacket itemFilterPacket;
    public final int rows;
    public final int columns;
    public final boolean customOrdering;

    public FlexChestPacket(IItemFilter itemFilterPacket, int rows, int columns) {
        this(itemFilterPacket, rows, columns, false);
    }
    public FlexChestPacket(IItemFilter itemFilterPacket, int size) {
        this(itemFilterPacket, size, 1, true);
    }

    private FlexChestPacket(IItemFilter itemFilterPacket, int rows, int columns, boolean customOrdering) {
        this.itemFilterPacket = new SetItemFilterPacket(itemFilterPacket);
        this.rows = rows;
        this.columns = columns;
        this.customOrdering = customOrdering;
    }
    public FlexChestPacket(PacketBuffer buffer) {
        this.itemFilterPacket = SetItemFilterPacket.decode(buffer);
        this.customOrdering = buffer.readBoolean();
        if (customOrdering) {
            rows = buffer.readInt();
            columns = 1;
        } else {
            rows = buffer.readInt();
            columns = buffer.readInt();
        }
    }

    public static FlexChestPacket decode(PacketBuffer buffer) {
        return new FlexChestPacket(buffer);
    }
    public void encode(PacketBuffer buffer) {
        itemFilterPacket.encode(buffer);
        buffer.writeBoolean(customOrdering);
        if (customOrdering) {
            buffer.writeInt(rows);
        } else {
            buffer.writeInt(rows);
            buffer.writeInt(columns);
        }
    }

    public int getSize() {
        return columns *rows;
    }
}

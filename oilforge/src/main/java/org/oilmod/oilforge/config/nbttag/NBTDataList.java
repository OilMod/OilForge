package org.oilmod.oilforge.config.nbttag;


import net.minecraft.nbt.*;
import org.oilmod.api.config.DataIndexedEntry;
import org.oilmod.api.config.DataList;
import org.oilmod.api.config.DataType;

import java.util.Iterator;

public class NBTDataList<Type> implements DataList<Type> {
	private final ListNBT parent;
    private final Class<Type> javaType;
    private final DataType type;


	public NBTDataList(Class<Type> javaType, DataType type) {
		this(new ListNBT(), javaType, type);
	}

	private NBTDataList(ListNBT parent, Class<Type> javaType, DataType type) {
		this.parent = parent;
        this.javaType = javaType;
        this.type = type;
    }


    public NBTDataList(ListNBT parent) {
        this.parent = parent;
        this.type = DataType.getByNbtId(parent.getTagType());
        //noinspection unchecked
        this.javaType = (Class<Type>) type.getJavaClass();
    }

	public ListNBT getListNBT() {
		return parent;
	}


    @Override
    public void append(Type type) {
        parent.add(convertNBT(type));
    }

    @Override
    public void set(int i, Type type) {
        parent.set(i, convertNBT(type));
    }

    @Override
    public Type get(int i) {
        return convertJava(parent.get(i));
    }

    @Override
    public Type remove(int i) {
        return convertJava(parent.remove(i));
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public Class<Type> getJavaType() {
        return javaType;
    }

    private INBT convertNBT(Type obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        switch (type) {
            case Byte:
                return new ByteNBT((Byte) obj);
            case Short:
                return new ShortNBT((Short) obj);
            case Int:
                return new IntNBT((Integer) obj);
            case Long:
                return new LongNBT((Long) obj);
            case Float:
                return new FloatNBT((Float) obj);
            case Double:
                return new DoubleNBT((Double) obj);
            case ByteArray:
                return new ByteArrayNBT((byte[]) obj);
            case String:
                return new StringNBT((String) obj);
            case List:
                return ((NBTDataList) obj).getListNBT();
            case Subsection:
                return ((OilNBTCompound) obj).getCompoundNBT();
            case IntArray:
                return new IntArrayNBT((int[]) obj);
            default:
                throw new IllegalStateException("Cannot convert object with type " + type.toString() + " to NBT");
        }
    }

    @SuppressWarnings("unchecked")
    private Type convertJava(INBT nbt) {
        return (Type) OilNBTCompound.convertJava(nbt, type);
    }

    @Override
    public Iterator<DataIndexedEntry<Type>> iterator() {
        return new Iterator<DataIndexedEntry<Type>>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index<parent.size();
            }

            @Override
            public DataIndexedEntry<Type> next() {
                INBT nbt = parent.get(index);
                DataIndexedEntry<Type> result = new DataIndexedEntry<>(index, convertJava(nbt), type);
                index++;
                return result;
            }
        };
    }
}

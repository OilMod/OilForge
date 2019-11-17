package org.oilmod.oilforge.config.nbttag;


import net.minecraft.nbt.*;
import org.oilmod.api.config.DataIndexedEntry;
import org.oilmod.api.config.DataList;
import org.oilmod.api.config.DataType;

import java.util.Iterator;

public class NBTDataList<Type> implements DataList<Type> {
	private final NBTTagList parent;
    private final Class<Type> javaType;
    private final DataType type;


	public NBTDataList(Class<Type> javaType, DataType type) {
		this(new NBTTagList(), javaType, type);
	}

	private NBTDataList(NBTTagList parent, Class<Type> javaType, DataType type) {
		this.parent = parent;
        this.javaType = javaType;
        this.type = type;
    }


    public NBTDataList(NBTTagList parent) {
        this.parent = parent;
        this.type = DataType.getByNbtId(parent.getTagType());
        //noinspection unchecked
        this.javaType = (Class<Type>) type.getJavaClass();
    }

	public NBTTagList getNBTTagList() {
		return parent;
	}


    @Override
    public void append(Type type) {
        parent.add(convertNBT(type));
    }

    @Override
    public void set(int i, Type type) {
        parent.setTag(i, convertNBT(type));
    }

    @Override
    public Type get(int i) {
        return convertJava(parent.getTag(i));
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

    private INBTBase convertNBT(Type obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        switch (type) {
            case Byte:
                return new NBTTagByte((Byte) obj);
            case Short:
                return new NBTTagShort((Short) obj);
            case Int:
                return new NBTTagInt((Integer) obj);
            case Long:
                return new NBTTagLong((Long) obj);
            case Float:
                return new NBTTagFloat((Float) obj);
            case Double:
                return new NBTTagDouble((Double) obj);
            case ByteArray:
                return new NBTTagByteArray((byte[]) obj);
            case String:
                return new NBTTagString((String) obj);
            case List:
                return ((NBTDataList) obj).getNBTTagList();
            case Subsection:
                return ((NBTCompound) obj).getNBTTagCompound();
            case IntArray:
                return new NBTTagIntArray((int[]) obj);
            default:
                throw new IllegalStateException("Cannot convert object with type " + type.toString() + " to NBT");
        }
    }

    @SuppressWarnings("unchecked")
    private Type convertJava(INBTBase nbt) {
        return (Type) NBTCompound.convertJava(nbt, type);
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
                INBTBase nbt = parent.getTag(index); //todo: replaced h with get hopefully correct
                DataIndexedEntry<Type> result = new DataIndexedEntry<>(index, convertJava(nbt), type);
                index++;
                return result;
            }
        };
    }
}

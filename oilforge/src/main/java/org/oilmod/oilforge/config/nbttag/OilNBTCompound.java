package org.oilmod.oilforge.config.nbttag;


import net.minecraft.nbt.*;
import org.apache.commons.lang3.Validate;
import org.oilmod.api.config.Compound;
import org.oilmod.api.config.DataKeyedEntry;
import org.oilmod.api.config.DataList;
import org.oilmod.api.config.DataType;

import java.util.Iterator;

public class OilNBTCompound implements Compound {
	CompoundNBT parent;
	
	
	public OilNBTCompound() {
		this(new CompoundNBT());
	}

	public OilNBTCompound(CompoundNBT parent) {
		this.parent = parent;
	}
	
	public CompoundNBT getCompoundNBT() {
		return parent;
	}
	
	
	@Override
	public Compound createCompound() {
        return new OilNBTCompound();
	}

    @Override
    public <Type> DataList<Type> createList(DataType dataType) {
        //noinspection unchecked
        Class<Type> typeClass = (Class<Type>) dataType.getJavaClass();
        return new NBTDataList<Type>(typeClass, dataType);
    }


	@Override
	public void set(String paramString, Compound paramCompound) {
		parent.put(paramString, ((OilNBTCompound) paramCompound).getCompoundNBT());
	}

	@Override
	public void setList(String paramString, DataList paramCompoundList) {
		parent.put(paramString, ((NBTDataList) paramCompoundList).getListNBT());
		
	}

	@Override
	public void setByte(String paramString, byte paramByte) {
		parent.putByte(paramString, paramByte);
	}

	@Override
	public void setShort(String paramString, short paramShort) {
		parent.putShort(paramString, paramShort);
	}

	@Override
	public void setInt(String paramString, int paramInt) {
		parent.putInt(paramString, paramInt);
	}

	@Override
	public void setLong(String paramString, long paramLong) {
		parent.putLong(paramString, paramLong);
	}

	@Override
	public void setFloat(String paramString, float paramFloat) {
		parent.putFloat(paramString, paramFloat);
	}

	@Override
	public void setDouble(String paramString, double paramDouble) {
		parent.putDouble(paramString, paramDouble);
	}

	@Override
	public void setString(String paramString1, String paramString2) {
		parent.putString(paramString1, paramString2);
	}

	@Override
	public void setByteArray(String paramString, byte[] paramArrayOfByte) {
		parent.putByteArray(paramString, paramArrayOfByte);
	}

	@Override
	public void setIntArray(String paramString, int[] paramArrayOfInt) {
		parent.putIntArray(paramString, paramArrayOfInt);
	}

	@Override
	public void setBoolean(String paramString, boolean paramBoolean) {
		parent.putBoolean(paramString, paramBoolean);
	}

	@Override
	public void setNBT(String paramString, Object paramNBTTag) {
		parent.put(paramString, (CompoundNBT) paramNBTTag);
	}


    @Override
    public void set(String key, Object value, DataType type) {
		Validate.isInstanceOf(type.getJavaClass(), value);
        switch (type) {
            case Byte:
                setByte(key, (Byte) value);
                break;
            case Short:
                setShort(key, (Short) value);
                break;
            case Int:
                setInt(key, (Integer) value);
                break;
            case Long:
                setLong(key, (Long) value);
                break;
            case Float:
                setFloat(key, (Float) value);
                break;
            case Double:
                setDouble(key, (Double) value);
                break;
            case ByteArray:
                setByteArray(key, (byte[]) value);
                break;
            case String:
                setString(key, (String) value);
                break;
            case List:
                setList(key, (DataList) value);
                break;
            case Subsection:
                set(key, (Compound) value);
                break;
            case IntArray:
                setIntArray(key, (int[]) value);
                break;
        }
    }

    @Override
    public void set(DataKeyedEntry dataKeyedEntry) {
        set(dataKeyedEntry.getKey(), dataKeyedEntry.getValue(), dataKeyedEntry.getType());
    }


    @Override
	public byte getByte(String paramString) {
		return parent.getByte(paramString);
	}

	@Override
	public short getShort(String paramString) {
		return parent.getShort(paramString);
	}

	@Override
	public int getInt(String paramString) {
		return parent.getInt(paramString);
	}

	@Override
	public long getLong(String paramString) {
		return parent.getLong(paramString);
	}

	@Override
	public float getFloat(String paramString) {
		return parent.getFloat(paramString);
	}

	@Override
	public double getDouble(String paramString) {
		return parent.getDouble(paramString);
	}

	@Override
	public String getString(String paramString) {
		return parent.getString(paramString);
	}

	@Override
	public byte[] getByteArray(String paramString) {
		return parent.getByteArray(paramString);
	}

	@Override
	public int[] getIntArray(String paramString) {
		return parent.getIntArray(paramString);
	}

	@Override
	public Compound getCompound(String paramString) {
		return new OilNBTCompound(parent.getCompound(paramString));
	}

	@Override
    public <Type> DataList<Type> getList(String paramString) {
		return new NBTDataList<>(parent.getList(paramString, 10));
	}

	@Override
	public boolean getBoolean(String paramString) {
		return parent.getBoolean(paramString);
	}

	@Override
	public Object getRaw(String s) {
		return parent.get(s);
	}

    @Override
    public DataKeyedEntry get(String s) {
        INBT nbtBase = parent.get(s);
        DataType type = DataType.getByNbtId(nbtBase.getId());
        return new DataKeyedEntry<>(s, convertJava(nbtBase, type), type);
    }

    static Object convertJava(INBT nbt, DataType type) {
        switch (type) {
            case Byte:
                return ((ByteNBT) nbt).getByte();
            case Short:
                return ((ShortNBT) nbt).getShort();
            case Int:
                return ((IntNBT) nbt).getInt();
            case Long:
                return ((LongNBT) nbt).getLong();
            case Float:
                return ((FloatNBT) nbt).getFloat();
            case Double:
                return ((DoubleNBT) nbt).getDouble();
            case ByteArray:
                return ((ByteArrayNBT) nbt).getByteArray();
            case String:
                return ((StringNBT)nbt).getString(); //needed to check instance
            case List:
                return new NBTDataList<>((ListNBT) nbt);
            case Subsection:
                return new OilNBTCompound((CompoundNBT) nbt);
			case IntArray:
				return ((IntArrayNBT) nbt).getIntArray();
			case LongArray:
				return ((LongArrayNBT) nbt).getAsLongArray();
            default:
                throw new IllegalStateException("Cannot convert object with type " + type.toString() + " to Java");
        }
    }

    @Override
	public boolean containsKey(String s) {
		return parent.contains(s);
	}

    @Override
    public boolean containsKey(String s, DataType dataType) {
        return containsKey(s) && getType(s)==dataType;
    }

    @Override
	public Object nbtClone() {
		return parent.copy();
	}

	@Override
	public DataType getType(String s) {
		return DataType.getByNbtId(parent.get(s).getId());
	}

    @Override
    public Iterator<DataKeyedEntry> iterator() {
        return new Iterator<DataKeyedEntry>() {
            private final Iterator<String> keyIterator = parent.keySet().iterator();

            @Override
            public boolean hasNext() {
                return keyIterator.hasNext();
            }

            @Override
            public DataKeyedEntry next() {
                String key = keyIterator.next();
                return get(key);
            }
        };
    }
}

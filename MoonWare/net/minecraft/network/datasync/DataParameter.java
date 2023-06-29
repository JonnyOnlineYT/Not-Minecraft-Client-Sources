package net.minecraft.network.datasync;

public class DataParameter<T>
{
    private final int id;
    private final DataSerializer<T> serializer;

    public DataParameter(int idIn, DataSerializer<T> serializerIn)
    {
        id = idIn;
        serializer = serializerIn;
    }

    public int getId()
    {
        return id;
    }

    public DataSerializer<T> getSerializer()
    {
        return serializer;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && getClass() == p_equals_1_.getClass())
        {
            DataParameter<?> dataparameter = (DataParameter)p_equals_1_;
            return id == dataparameter.id;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return id;
    }
}

package net.minecraftforge.common.property;

public interface IUnlistedProperty<X> {
    String getName();

    boolean isValid(V var1);

    Class<V> getType();

    String valueToString(V var1);
}

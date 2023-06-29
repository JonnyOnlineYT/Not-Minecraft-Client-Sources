package de.strafe.settings;

@FunctionalInterface
public interface ValueChangeListener<T> {
    void onValueChange(final T p0, final T p1);
}

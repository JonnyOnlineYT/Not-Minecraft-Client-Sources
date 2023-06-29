package net.minecraft.util.text;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KeybindComponent extends BaseComponent {
    public static Function<String, Supplier<String>> supplierFunction = (s) -> () -> s;
    @Getter private final String key;
    private Supplier<String> keyNameGetter;

    @Override
    public String getString() {
        if (keyNameGetter == null) keyNameGetter = supplierFunction.apply(key);
        return keyNameGetter.get();
    }

    @Override
    public KeybindComponent copy() {
        KeybindComponent component = new KeybindComponent(this.key);
        component.setStyle(getStyle().createShallowCopy());
        for (Component c : getSiblings()) {
            component.append(c.copy());
        }
        return component;
    }
}

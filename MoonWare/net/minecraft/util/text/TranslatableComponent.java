package net.minecraft.util.text;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.util.text.translation.I18n;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatableComponent extends BaseComponent {
    @Getter private final String key;
    @Getter private final Object[] args;
    private final Object syncLock = new Object();
    private long updated = -1L;
    List<Component> children = Lists.newArrayList();
    public static final Pattern STRING_VARIABLE_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TranslatableComponent(String key, Object... args) {
        this.key = key;
        this.args = args;
        for (Object obj : args) {
            if (obj instanceof Component) ((Component) obj).getStyle().setParentStyle(getStyle());
        }
    }

    @Override
    public String getString() {
        init();
        return "";
    }

    synchronized void init() {
        synchronized (syncLock) {
            long i = I18n.getLastTranslationUpdateTimeInMilliseconds();
            if (i == updated) return;
            updated = i;
            children.clear();
        }
        try {
            initializeFromFormat(I18n.translateToLocal(key));
        } catch (TranslationException e) {
            children.clear();
            try {
                initializeFromFormat(I18n.translateToFallback(key));
            } catch (TranslationException ex) {
                e.addSuppressed(ex);
                throw e;
            }
        }
    }

    protected void initializeFromFormat(String format) {
        Matcher matcher = STRING_VARIABLE_PATTERN.matcher(format);
        int i = 0;
        int j = 0;
        try {
            int l;
            for (; matcher.find(j); j = l) {
                int k = matcher.start();
                l = matcher.end();
                if (k > j) {
                    TextComponent c = new TextComponent(String.format(format.substring(j, k)));
                    c.getStyle().setParentStyle(getStyle());
                    children.add(c);
                }
                String s2 = matcher.group(2);
                String s = format.substring(k, l);
                if ("%".equals(s2) && "%%".equals(s)) {
                    TextComponent c = new TextComponent("%");
                    c.getStyle().setParentStyle(getStyle());
                    children.add(c);
                } else {
                    if (!"s".equals(s2)) throw new TranslationException(this, "Unsupported format: '" + s + "'");
                    String s1 = matcher.group(1);
                    int i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;
                    if (i1 < args.length) children.add(getFormatArgumentAsComponent(i1));
                }
            }
            if (j < format.length()) {
                TextComponent c = new TextComponent(String.format(format.substring(j)));
                c.getStyle().setParentStyle(getStyle());
                children.add(c);
            }
        } catch (IllegalFormatException ex) {
            throw new TranslationException(this, ex);
        }
    }

    private Component getFormatArgumentAsComponent(int index) {
        if (index >= args.length) throw new TranslationException(this, index);
        Object object = args[index];
        if (object instanceof Component) return (Component) object;
        TextComponent c = new TextComponent(String.valueOf(object));
        c.getStyle().setParentStyle(getStyle());
        return c;
    }

    @Override
    public Component setStyle(Style style) {
        super.setStyle(style);
        for (Object obj : args) {
            if (obj instanceof Component) {
                ((Component) obj).getStyle().setParentStyle(getStyle());
            }
        }
        for (Component c : children) {
            c.getStyle().setParentStyle(style);
        }
        return this;
    }

    @Override
    public Iterator<Component> iterator() {
        init();
        return Iterators.concat(BaseComponent.createDeepCopyIterator(children), BaseComponent.createDeepCopyIterator(siblings));
    }

    @Override
    public TranslatableComponent copy() {
        Object[] args = new Object[this.args.length];
        for (int i = 0; i < this.args.length; ++i) {
            if (this.args[i] instanceof Component) {
                args[i] = ((Component) this.args[i]).copy();
            } else {
                args[i] = this.args[i];
            }
        }
        TranslatableComponent component = new TranslatableComponent(key, args);
        component.setStyle(getStyle().createShallowCopy());
        for (Component c : getSiblings()) {
            component.append(c.copy());
        }
        return component;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof TranslatableComponent)) return false;
        TranslatableComponent component = (TranslatableComponent) other;
        return getKey().equals(component.getKey()) && Arrays.equals(getArgs(), component.getArgs()) && super.equals(other);
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + key.hashCode();
        i = 31 * i + Arrays.hashCode(args);
        return i;
    }

    @Override
    public String toString() {
        return "TranslatableComponent{key='" + getKey() + '\'' + ", args=" + Arrays.toString(getArgs()) + ", siblings=" + getSiblings() + ", style=" + getStyle() + '}';
    }
}

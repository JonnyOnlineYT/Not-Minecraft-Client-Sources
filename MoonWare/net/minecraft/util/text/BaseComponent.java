package net.minecraft.util.text;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

public abstract class BaseComponent implements Component {
    protected List<Component> siblings = Lists.newArrayList();
    private Style style;
    @Override
    public Component append(String text) {
        return append(new TextComponent(text));
    }

    @Override
    public Component append(Component component) {
        component.getStyle().setParentStyle(getStyle());
        getSiblings().add(component);
        return this;
    }

    @Override
    public List<Component> getSiblings() {
        return siblings;
    }

    public Component setStyle(Style style) {
        this.style = style;
        for (Component c : getSiblings()) c.getStyle().setParentStyle(getStyle());
        return this;
    }

    public Style getStyle() {
        if (style == null) {
            style = new Style();
            for (Component c : getSiblings()) c.getStyle().setParentStyle(style);
        }
        return style;
    }

    @Override
    public Iterator<Component> iterator() {
        return Iterators.concat(Iterators.forArray(this), createDeepCopyIterator(siblings));
    }

    @Override
    public final String asString() {
        StringBuilder sb = new StringBuilder();
        for (Component c : this) sb.append(c.getString());
        return sb.toString();
    }

    @Override
    public final String asFormattedString() {
        StringBuilder sb = new StringBuilder();
        for (Component c : this) {
            String s = c.getString();
            if (!s.isEmpty()) {
                sb.append(c.getStyle().getFormattingCode());
                sb.append(s);
                sb.append(Formatting.RESET);
            }
        }
        return sb.toString();
    }

    public static Iterator<Component> createDeepCopyIterator(Iterable<Component> components) {
        Iterator<Component> iterator = Iterators.concat(Iterators.transform(components.iterator(),
                Iterable::iterator));
        iterator = Iterators.transform(iterator, p_apply_1_ -> {
            Component itextcomponent = p_apply_1_.copy();
            itextcomponent.setStyle(itextcomponent.getStyle().createDeepCopy());
            return itextcomponent;
        });
        return iterator;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof BaseComponent)) {
            return false;
        } else {
            BaseComponent textcomponentbase = (BaseComponent) p_equals_1_;
            return siblings.equals(textcomponentbase.siblings) && getStyle().equals(textcomponentbase.getStyle());
        }
    }

    public int hashCode() {
        return 31 * style.hashCode() + siblings.hashCode();
    }

    public String toString() {
        return "BaseComponent{style=" + style + ", siblings=" + siblings + '}';
    }
}

/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui;

import org.moonware.client.utils.FontStorage;
import org.moonware.client.ui.celestialgui.component.Component;

import java.util.Comparator;

public class SorterHelper
implements Comparator<Component> {
    @Override
    public int compare(Component component, Component component2) {
        String s1 = component.getName();
        String s2 = component2.getName();
        int cmp = FontStorage.robotoRegularFontRender.getWidth(s2) - FontStorage.robotoRegularFontRender.getWidth(s1);
        return cmp != 0 ? cmp : s2.compareTo(s1);
    }
}


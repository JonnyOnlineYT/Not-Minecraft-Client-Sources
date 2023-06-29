package org.moonware.client.ui.sqgui;


import org.moonware.client.ui.sqgui.component.Component;
import org.moonware.client.utils.MWFont;

import java.util.Comparator;

public class SorterHelper implements Comparator<Component> {

    @Override
    public int compare(Component component, Component component2) {
        String s1 = component.getName();
        String s2 = component2.getName();
        int cmp = MWFont.GREYCLIFFCF_MEDIUM.get(17).getWidth(s2) - MWFont.GREYCLIFFCF_MEDIUM.get(17).getWidth(s1);
        return cmp != 0 ? cmp : s2.compareTo(s1);
    }

}
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.Formatting;

public class GuiUtilRenderComponents
{
    public static String removeTextColorsIfConfigured(String text, boolean forceColor)
    {
        return !forceColor && !Minecraft.gameSettings.chatColours ? Formatting.strip(text) : text;
    }

    public static List<Component> splitText(Component textComponent, int maxTextLenght, Font fontIn, boolean p_178908_3_, boolean forceTextColor)
    {
        int i = 0;
        Component itextcomponent = new TextComponent("");
        List<Component> list = Lists.newArrayList();
        List<Component> list1 = Lists.newArrayList(textComponent);

        for (int j = 0; j < list1.size(); ++j)
        {
            Component itextcomponent1 = list1.get(j);
            String s = itextcomponent1.getString();
            boolean flag = false;

            if (s.contains("\n"))
            {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                Component itextcomponent2 = new TextComponent(s1);
                itextcomponent2.setStyle(itextcomponent1.getStyle().createShallowCopy());
                list1.add(j + 1, itextcomponent2);
                flag = true;
            }

            String s4 = removeTextColorsIfConfigured(itextcomponent1.getStyle().getFormattingCode() + s, forceTextColor);
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = fontIn.getStringWidth(s5);
            TextComponent textcomponentstring = new TextComponent(s5);
            textcomponentstring.setStyle(itextcomponent1.getStyle().createShallowCopy());

            if (i + i1 > maxTextLenght)
            {
                String s2 = fontIn.trimStringToWidth(s4, maxTextLenght - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && !s3.isEmpty())
                {
                    int l = s2.lastIndexOf(32);

                    if (l >= 0 && fontIn.getStringWidth(s4.substring(0, l)) > 0)
                    {
                        s2 = s4.substring(0, l);

                        if (p_178908_3_)
                        {
                            ++l;
                        }

                        s3 = s4.substring(l);
                    }
                    else if (i > 0 && !s4.contains(" "))
                    {
                        s2 = "";
                        s3 = s4;
                    }

                    TextComponent textcomponentstring1 = new TextComponent(s3);
                    textcomponentstring1.setStyle(itextcomponent1.getStyle().createShallowCopy());
                    list1.add(j + 1, textcomponentstring1);
                }

                i1 = fontIn.getStringWidth(s2);
                textcomponentstring = new TextComponent(s2);
                textcomponentstring.setStyle(itextcomponent1.getStyle().createShallowCopy());
                flag = true;
            }

            if (i + i1 <= maxTextLenght)
            {
                i += i1;
                itextcomponent.append(textcomponentstring);
            }
            else
            {
                flag = true;
            }

            if (flag)
            {
                list.add(itextcomponent);
                i = 0;
                itextcomponent = new TextComponent("");
            }
        }

        list.add(itextcomponent);
        return list;
    }
}

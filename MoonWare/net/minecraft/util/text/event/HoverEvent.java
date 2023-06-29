package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import net.minecraft.util.text.Component;

import java.util.Map;

public class HoverEvent
{
    private final HoverEvent.Action action;
    private final Component value;

    public HoverEvent(HoverEvent.Action actionIn, Component valueIn)
    {
        action = actionIn;
        value = valueIn;
    }

    /**
     * Gets the action to perform when this event is raised.
     */
    public HoverEvent.Action getAction()
    {
        return action;
    }

    /**
     * Gets the value to perform the action on when this event is raised.  For example, if the action is "show item",
     * this would be the item to show.
     */
    public Component getValue()
    {
        return value;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && getClass() == p_equals_1_.getClass())
        {
            HoverEvent hoverevent = (HoverEvent)p_equals_1_;

            if (action != hoverevent.action)
            {
                return false;
            }
            else
            {
                if (value != null)
                {
                    return value.equals(hoverevent.value);
                }
                else return hoverevent.value == null;
            }
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return "HoverEvent{action=" + action + ", value='" + value + '\'' + '}';
    }

    public int hashCode()
    {
        int i = action.hashCode();
        i = 31 * i + (value != null ? value.hashCode() : 0);
        return i;
    }

    public enum Action
    {
        SHOW_TEXT("show_text", true),
        SHOW_ITEM("show_item", true),
        SHOW_ENTITY("show_entity", true);

        private static final Map<String, HoverEvent.Action> NAME_MAPPING = Maps.newHashMap();
        private final boolean allowedInChat;
        private final String canonicalName;

        Action(String canonicalNameIn, boolean allowedInChatIn)
        {
            canonicalName = canonicalNameIn;
            allowedInChat = allowedInChatIn;
        }

        public boolean shouldAllowInChat()
        {
            return allowedInChat;
        }

        public String getCanonicalName()
        {
            return canonicalName;
        }

        public static HoverEvent.Action getValueByCanonicalName(String canonicalNameIn)
        {
            return NAME_MAPPING.get(canonicalNameIn);
        }

        static {
            for (HoverEvent.Action hoverevent$action : values())
            {
                NAME_MAPPING.put(hoverevent$action.getCanonicalName(), hoverevent$action);
            }
        }
    }
}

package net.minecraft.util.text.event;

import com.google.common.collect.Maps;

import java.util.Map;

public class ClickEvent
{
    private final ClickEvent.Action action;
    private final String value;

    public ClickEvent(ClickEvent.Action theAction, String theValue)
    {
        action = theAction;
        value = theValue;
    }

    /**
     * Gets the action to perform when this event is raised.
     */
    public ClickEvent.Action getAction()
    {
        return action;
    }

    /**
     * Gets the value to perform the action on when this event is raised.  For example, if the action is "open URL",
     * this would be the URL to open.
     */
    public String getValue()
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
            ClickEvent clickevent = (ClickEvent)p_equals_1_;

            if (action != clickevent.action)
            {
                return false;
            }
            else
            {
                if (value != null)
                {
                    return value.equals(clickevent.value);
                }
                else return clickevent.value == null;
            }
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return "ClickEvent{action=" + action + ", value='" + value + '\'' + '}';
    }

    public int hashCode()
    {
        int i = action.hashCode();
        i = 31 * i + (value != null ? value.hashCode() : 0);
        return i;
    }

    public enum Action
    {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private static final Map<String, ClickEvent.Action> NAME_MAPPING = Maps.newHashMap();
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

        public static ClickEvent.Action getValueByCanonicalName(String canonicalNameIn)
        {
            return NAME_MAPPING.get(canonicalNameIn);
        }

        static {
            for (ClickEvent.Action clickevent$action : values())
            {
                NAME_MAPPING.put(clickevent$action.getCanonicalName(), clickevent$action);
            }
        }
    }
}

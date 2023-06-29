package net.minecraft.util.text;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class Components {
    public static Component processComponent(ICommandSender commandSender, Component component, Entity entityIn) throws CommandException {
        Component itextcomponent;

        if (component instanceof ScoreComponent) {
            ScoreComponent textcomponentscore = (ScoreComponent) component;
            String s = textcomponentscore.getName();

            if (EntitySelector.hasArguments(s)) {
                List<Entity> list = EntitySelector.matchEntities(commandSender, s, Entity.class);

                if (list.size() != 1) {
                    throw new EntityNotFoundException("commands.generic.selector.notFound", s);
                }

                Entity entity = list.get(0);

                if (entity instanceof EntityPlayer) {
                    s = entity.getName();
                } else {
                    s = entity.getCachedUniqueIdString();
                }
            }

            String s2 = entityIn != null && s.equals("*") ? entityIn.getName() : s;
            itextcomponent = new ScoreComponent(s2, textcomponentscore.getObjective());
            ((ScoreComponent) itextcomponent).setValue(textcomponentscore.getString());
            ((ScoreComponent) itextcomponent).resolve(commandSender);
        } else if (component instanceof SelectorComponent) {
            String s1 = ((SelectorComponent) component).getSelector();
            itextcomponent = EntitySelector.matchEntitiesToTextComponent(commandSender, s1);

            if (itextcomponent == null) {
                itextcomponent = new TextComponent("");
            }
        } else if (component instanceof TextComponent) {
            itextcomponent = new TextComponent(((TextComponent) component).getText());
        } else if (component instanceof KeybindComponent) {
            itextcomponent = new KeybindComponent(((KeybindComponent) component).getKey());
        } else {
            if (!(component instanceof TranslatableComponent)) {
                return component;
            }

            Object[] aobject = ((TranslatableComponent) component).getArgs();

            for (int i = 0; i < aobject.length; ++i) {
                Object object = aobject[i];

                if (object instanceof Component) {
                    aobject[i] = processComponent(commandSender, (Component) object, entityIn);
                }
            }

            itextcomponent = new TranslatableComponent(((TranslatableComponent) component).getKey(), aobject);
        }

        Style style = component.getStyle();

        if (style != null) {
            itextcomponent.setStyle(style.createShallowCopy());
        }

        for (Component itextcomponent1 : component.getSiblings()) {
            itextcomponent.append(processComponent(commandSender, itextcomponent1, entityIn));
        }

        return itextcomponent;
    }
}

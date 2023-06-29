package net.minecraft.util.text;

import lombok.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScoreComponent extends BaseComponent {
    @Getter private final String name;
    @Getter private final String objective;
    @Getter @Setter private String value = "";

    @Override
    public String getString() {
        return value;
    }

    @Override
    public ScoreComponent copy() {
        ScoreComponent component = new ScoreComponent(name, objective);
        component.setValue(value);
        component.setStyle(getStyle().createShallowCopy());
        for (Component c : getSiblings()) {
            component.append(c.copy());
        }
        return component;
    }

    public void resolve(ICommandSender sender) {
        MinecraftServer serv = sender.getServer();
        if (serv == null || !serv.isAnvilFileSet() || !StringUtils.isNullOrEmpty(value)) return;
        Scoreboard sb = serv.worldServerForDimension(0).getScoreboard();
        ScoreObjective obj = sb.getObjective(objective);
        if (sb.entityHasObjective(name, obj)) {
            setValue(String.format("%d", sb.getOrCreateScore(name, obj).getScorePoints()));
        } else {
            value = "";
        }
    }
}

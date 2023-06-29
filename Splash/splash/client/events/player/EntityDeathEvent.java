package splash.client.events.player;
import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class EntityDeathEvent extends Cancelable {

    private EntityPlayer player;
    private DamageSource source;

    public EntityDeathEvent(EntityPlayer player, DamageSource source) {
        this.player = player;
        this.source = source;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public DamageSource getSource() {
        return source;
    }

}
package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.Vec3d;

public class LevitationTrigger implements ICriterionTrigger<LevitationTrigger.Instance>
{
    private static final Namespaced field_193164_a = new Namespaced("levitation");
    private final Map<PlayerAdvancements, LevitationTrigger.Listeners> field_193165_b = Maps.newHashMap();

    public Namespaced func_192163_a()
    {
        return field_193164_a;
    }

    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<LevitationTrigger.Instance> p_192165_2_)
    {
        LevitationTrigger.Listeners levitationtrigger$listeners = field_193165_b.get(p_192165_1_);

        if (levitationtrigger$listeners == null)
        {
            levitationtrigger$listeners = new LevitationTrigger.Listeners(p_192165_1_);
            field_193165_b.put(p_192165_1_, levitationtrigger$listeners);
        }

        levitationtrigger$listeners.func_193449_a(p_192165_2_);
    }

    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<LevitationTrigger.Instance> p_192164_2_)
    {
        LevitationTrigger.Listeners levitationtrigger$listeners = field_193165_b.get(p_192164_1_);

        if (levitationtrigger$listeners != null)
        {
            levitationtrigger$listeners.func_193446_b(p_192164_2_);

            if (levitationtrigger$listeners.func_193447_a())
            {
                field_193165_b.remove(p_192164_1_);
            }
        }
    }

    public void func_192167_a(PlayerAdvancements p_192167_1_)
    {
        field_193165_b.remove(p_192167_1_);
    }

    public LevitationTrigger.Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_)
    {
        DistancePredicate distancepredicate = DistancePredicate.func_193421_a(p_192166_1_.get("distance"));
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("duration"));
        return new LevitationTrigger.Instance(distancepredicate, minmaxbounds);
    }

    public void func_193162_a(EntityPlayerMP p_193162_1_, Vec3d p_193162_2_, int p_193162_3_)
    {
        LevitationTrigger.Listeners levitationtrigger$listeners = field_193165_b.get(p_193162_1_.func_192039_O());

        if (levitationtrigger$listeners != null)
        {
            levitationtrigger$listeners.func_193448_a(p_193162_1_, p_193162_2_, p_193162_3_);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final DistancePredicate field_193202_a;
        private final MinMaxBounds field_193203_b;

        public Instance(DistancePredicate p_i47571_1_, MinMaxBounds p_i47571_2_)
        {
            super(field_193164_a);
            field_193202_a = p_i47571_1_;
            field_193203_b = p_i47571_2_;
        }

        public boolean func_193201_a(EntityPlayerMP p_193201_1_, Vec3d p_193201_2_, int p_193201_3_)
        {
            if (!field_193202_a.func_193422_a(p_193201_2_.xCoord, p_193201_2_.yCoord, p_193201_2_.zCoord, p_193201_1_.posX, p_193201_1_.posY, p_193201_1_.posZ))
            {
                return false;
            }
            else
            {
                return field_193203_b.func_192514_a((float)p_193201_3_);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193450_a;
        private final Set<ICriterionTrigger.Listener<LevitationTrigger.Instance>> field_193451_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47572_1_)
        {
            field_193450_a = p_i47572_1_;
        }

        public boolean func_193447_a()
        {
            return field_193451_b.isEmpty();
        }

        public void func_193449_a(ICriterionTrigger.Listener<LevitationTrigger.Instance> p_193449_1_)
        {
            field_193451_b.add(p_193449_1_);
        }

        public void func_193446_b(ICriterionTrigger.Listener<LevitationTrigger.Instance> p_193446_1_)
        {
            field_193451_b.remove(p_193446_1_);
        }

        public void func_193448_a(EntityPlayerMP p_193448_1_, Vec3d p_193448_2_, int p_193448_3_)
        {
            List<ICriterionTrigger.Listener<LevitationTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<LevitationTrigger.Instance> listener : field_193451_b)
            {
                if (listener.func_192158_a().func_193201_a(p_193448_1_, p_193448_2_, p_193448_3_))
                {
                    if (list == null)
                    {
                        list = Lists.newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<LevitationTrigger.Instance> listener1 : list)
                {
                    listener1.func_192159_a(field_193450_a);
                }
            }
        }
    }
}

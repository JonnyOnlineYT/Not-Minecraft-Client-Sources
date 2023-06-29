package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements
{
    private static final Logger field_192753_a = LogManager.getLogger();
    private static final Gson field_192754_b = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer()).registerTypeAdapter(Namespaced.class, new Namespaced.Serializer()).setPrettyPrinting().create();
    private static final TypeToken<Map<Namespaced, AdvancementProgress>> field_192755_c = new TypeToken<Map<Namespaced, AdvancementProgress>>()
    {
    };
    private final MinecraftServer field_192756_d;
    private final File field_192757_e;
    private final Map<Advancement, AdvancementProgress> field_192758_f = Maps.newLinkedHashMap();
    private final Set<Advancement> field_192759_g = Sets.newLinkedHashSet();
    private final Set<Advancement> field_192760_h = Sets.newLinkedHashSet();
    private final Set<Advancement> field_192761_i = Sets.newLinkedHashSet();
    private EntityPlayerMP field_192762_j;
    @Nullable
    private Advancement field_194221_k;
    private boolean field_192763_k = true;

    public PlayerAdvancements(MinecraftServer p_i47422_1_, File p_i47422_2_, EntityPlayerMP p_i47422_3_)
    {
        field_192756_d = p_i47422_1_;
        field_192757_e = p_i47422_2_;
        field_192762_j = p_i47422_3_;
        func_192740_f();
    }

    public void func_192739_a(EntityPlayerMP p_192739_1_)
    {
        field_192762_j = p_192739_1_;
    }

    public void func_192745_a()
    {
        for (ICriterionTrigger<?> icriteriontrigger : CriteriaTriggers.func_192120_a())
        {
            icriteriontrigger.func_192167_a(this);
        }
    }

    public void func_193766_b()
    {
        func_192745_a();
        field_192758_f.clear();
        field_192759_g.clear();
        field_192760_h.clear();
        field_192761_i.clear();
        field_192763_k = true;
        field_194221_k = null;
        func_192740_f();
    }

    private void func_192751_c()
    {
        for (Advancement advancement : field_192756_d.func_191949_aK().func_192780_b())
        {
            func_193764_b(advancement);
        }
    }

    private void func_192752_d()
    {
        List<Advancement> list = Lists.newArrayList();

        for (Map.Entry<Advancement, AdvancementProgress> entry : field_192758_f.entrySet())
        {
            if (entry.getValue().func_192105_a())
            {
                list.add(entry.getKey());
                field_192761_i.add(entry.getKey());
            }
        }

        for (Advancement advancement : list)
        {
            func_192742_b(advancement);
        }
    }

    private void func_192748_e()
    {
        for (Advancement advancement : field_192756_d.func_191949_aK().func_192780_b())
        {
            if (advancement.getCriteria().isEmpty())
            {
                func_192750_a(advancement, "");
                advancement.getRewards().func_192113_a(field_192762_j);
            }
        }
    }

    private void func_192740_f()
    {
        if (field_192757_e.isFile())
        {
            try
            {
                String s = Files.toString(field_192757_e, StandardCharsets.UTF_8);
                Map<Namespaced, AdvancementProgress> map = JsonUtils.func_193840_a(field_192754_b, s, field_192755_c.getType());

                if (map == null)
                {
                    throw new JsonParseException("Found null for advancements");
                }

                Stream<Map.Entry<Namespaced, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue));

                for (Map.Entry<Namespaced, AdvancementProgress> entry : stream.collect(Collectors.toList()))
                {
                    Advancement advancement = field_192756_d.func_191949_aK().func_192778_a(entry.getKey());

                    if (advancement == null)
                    {
                        field_192753_a.warn("Ignored advancement '" + entry.getKey() + "' in progress file " + field_192757_e + " - it doesn't exist anymore?");
                    }
                    else
                    {
                        func_192743_a(advancement, entry.getValue());
                    }
                }
            }
            catch (JsonParseException jsonparseexception)
            {
                field_192753_a.error("Couldn't parse player advancements in " + field_192757_e, jsonparseexception);
            }
            catch (IOException ioexception)
            {
                field_192753_a.error("Couldn't access player advancements in " + field_192757_e, ioexception);
            }
        }

        func_192748_e();
        func_192752_d();
        func_192751_c();
    }

    public void func_192749_b()
    {
        Map<Namespaced, AdvancementProgress> map = Maps.newHashMap();

        for (Map.Entry<Advancement, AdvancementProgress> entry : field_192758_f.entrySet())
        {
            AdvancementProgress advancementprogress = entry.getValue();

            if (advancementprogress.func_192108_b())
            {
                map.put(entry.getKey().getId(), advancementprogress);
            }
        }

        if (field_192757_e.getParentFile() != null)
        {
            field_192757_e.getParentFile().mkdirs();
        }

        try
        {
            Files.write(field_192754_b.toJson(map), field_192757_e, StandardCharsets.UTF_8);
        }
        catch (IOException ioexception)
        {
            field_192753_a.error("Couldn't save player advancements to " + field_192757_e, ioexception);
        }
    }

    public boolean func_192750_a(Advancement p_192750_1_, String p_192750_2_)
    {
        boolean flag = false;
        AdvancementProgress advancementprogress = func_192747_a(p_192750_1_);
        boolean flag1 = advancementprogress.func_192105_a();

        if (advancementprogress.func_192109_a(p_192750_2_))
        {
            func_193765_c(p_192750_1_);
            field_192761_i.add(p_192750_1_);
            flag = true;

            if (!flag1 && advancementprogress.func_192105_a())
            {
                p_192750_1_.getRewards().func_192113_a(field_192762_j);

                if (p_192750_1_.getDisplay() != null && p_192750_1_.getDisplay().func_193220_i() && field_192762_j.world.getGameRules().getBoolean("announceAdvancements"))
                {
                    field_192756_d.getPlayerList().sendChatMsg(new TranslatableComponent("chat.type.advancement." + p_192750_1_.getDisplay().func_192291_d().func_192307_a(), field_192762_j.getDisplayName(), p_192750_1_.getComponent()));
                }
            }
        }

        if (advancementprogress.func_192105_a())
        {
            func_192742_b(p_192750_1_);
        }

        return flag;
    }

    public boolean func_192744_b(Advancement p_192744_1_, String p_192744_2_)
    {
        boolean flag = false;
        AdvancementProgress advancementprogress = func_192747_a(p_192744_1_);

        if (advancementprogress.func_192101_b(p_192744_2_))
        {
            func_193764_b(p_192744_1_);
            field_192761_i.add(p_192744_1_);
            flag = true;
        }

        if (!advancementprogress.func_192108_b())
        {
            func_192742_b(p_192744_1_);
        }

        return flag;
    }

    private void func_193764_b(Advancement p_193764_1_)
    {
        AdvancementProgress advancementprogress = func_192747_a(p_193764_1_);

        if (!advancementprogress.func_192105_a())
        {
            for (Map.Entry<String, Criterion> entry : p_193764_1_.getCriteria().entrySet())
            {
                CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());

                if (criterionprogress != null && !criterionprogress.func_192151_a())
                {
                    ICriterionInstance icriterioninstance = entry.getValue().func_192143_a();

                    if (icriterioninstance != null)
                    {
                        ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.func_192119_a(icriterioninstance.func_192244_a());

                        if (icriteriontrigger != null)
                        {
                            icriteriontrigger.func_192165_a(this, new ICriterionTrigger.Listener(icriterioninstance, p_193764_1_, entry.getKey()));
                        }
                    }
                }
            }
        }
    }

    private void func_193765_c(Advancement p_193765_1_)
    {
        AdvancementProgress advancementprogress = func_192747_a(p_193765_1_);

        for (Map.Entry<String, Criterion> entry : p_193765_1_.getCriteria().entrySet())
        {
            CriterionProgress criterionprogress = advancementprogress.func_192106_c(entry.getKey());

            if (criterionprogress != null && (criterionprogress.func_192151_a() || advancementprogress.func_192105_a()))
            {
                ICriterionInstance icriterioninstance = entry.getValue().func_192143_a();

                if (icriterioninstance != null)
                {
                    ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.func_192119_a(icriterioninstance.func_192244_a());

                    if (icriteriontrigger != null)
                    {
                        icriteriontrigger.func_192164_b(this, new ICriterionTrigger.Listener(icriterioninstance, p_193765_1_, entry.getKey()));
                    }
                }
            }
        }
    }

    public void func_192741_b(EntityPlayerMP p_192741_1_)
    {
        if (!field_192760_h.isEmpty() || !field_192761_i.isEmpty())
        {
            Map<Namespaced, AdvancementProgress> map = Maps.newHashMap();
            Set<Advancement> set = Sets.newLinkedHashSet();
            Set<Namespaced> set1 = Sets.newLinkedHashSet();

            for (Advancement advancement : field_192761_i)
            {
                if (field_192759_g.contains(advancement))
                {
                    map.put(advancement.getId(), field_192758_f.get(advancement));
                }
            }

            for (Advancement advancement1 : field_192760_h)
            {
                if (field_192759_g.contains(advancement1))
                {
                    set.add(advancement1);
                }
                else
                {
                    set1.add(advancement1.getId());
                }
            }

            if (!map.isEmpty() || !set.isEmpty() || !set1.isEmpty())
            {
                p_192741_1_.connection.sendPacket(new SPacketAdvancementInfo(field_192763_k, set, set1, map));
                field_192760_h.clear();
                field_192761_i.clear();
            }
        }

        field_192763_k = false;
    }

    public void func_194220_a(@Nullable Advancement p_194220_1_)
    {
        Advancement advancement = field_194221_k;

        if (p_194220_1_ != null && p_194220_1_.getParent() == null && p_194220_1_.getDisplay() != null)
        {
            field_194221_k = p_194220_1_;
        }
        else
        {
            field_194221_k = null;
        }

        if (advancement != field_194221_k)
        {
            field_192762_j.connection.sendPacket(new SPacketSelectAdvancementsTab(field_194221_k == null ? null : field_194221_k.getId()));
        }
    }

    public AdvancementProgress func_192747_a(Advancement p_192747_1_)
    {
        AdvancementProgress advancementprogress = field_192758_f.get(p_192747_1_);

        if (advancementprogress == null)
        {
            advancementprogress = new AdvancementProgress();
            func_192743_a(p_192747_1_, advancementprogress);
        }

        return advancementprogress;
    }

    private void func_192743_a(Advancement p_192743_1_, AdvancementProgress p_192743_2_)
    {
        p_192743_2_.func_192099_a(p_192743_1_.getCriteria(), p_192743_1_.getRequirements());
        field_192758_f.put(p_192743_1_, p_192743_2_);
    }

    private void func_192742_b(Advancement p_192742_1_)
    {
        boolean flag = func_192738_c(p_192742_1_);
        boolean flag1 = field_192759_g.contains(p_192742_1_);

        if (flag && !flag1)
        {
            field_192759_g.add(p_192742_1_);
            field_192760_h.add(p_192742_1_);

            if (field_192758_f.containsKey(p_192742_1_))
            {
                field_192761_i.add(p_192742_1_);
            }
        }
        else if (!flag && flag1)
        {
            field_192759_g.remove(p_192742_1_);
            field_192760_h.add(p_192742_1_);
        }

        if (flag != flag1 && p_192742_1_.getParent() != null)
        {
            func_192742_b(p_192742_1_.getParent());
        }

        for (Advancement advancement : p_192742_1_.getChildren())
        {
            func_192742_b(advancement);
        }
    }

    private boolean func_192738_c(Advancement p_192738_1_)
    {
        for (int i = 0; p_192738_1_ != null && i <= 2; ++i)
        {
            if (i == 0 && func_192746_d(p_192738_1_))
            {
                return true;
            }

            if (p_192738_1_.getDisplay() == null)
            {
                return false;
            }

            AdvancementProgress advancementprogress = func_192747_a(p_192738_1_);

            if (advancementprogress.func_192105_a())
            {
                return true;
            }

            if (p_192738_1_.getDisplay().func_193224_j())
            {
                return false;
            }

            p_192738_1_ = p_192738_1_.getParent();
        }

        return false;
    }

    private boolean func_192746_d(Advancement p_192746_1_)
    {
        AdvancementProgress advancementprogress = func_192747_a(p_192746_1_);

        if (advancementprogress.func_192105_a())
        {
            return true;
        }
        else
        {
            for (Advancement advancement : p_192746_1_.getChildren())
            {
                if (func_192746_d(advancement))
                {
                    return true;
                }
            }

            return false;
        }
    }
}

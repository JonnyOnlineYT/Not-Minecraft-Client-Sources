package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Getter
@ToString
public class Advancement {
    private final Namespaced id;
    private final Advancement parent;
    private final AdvancementDisplay display;
    private final AdvancementRewards rewards;
    private final Map<String, Criterion> criteria;
    private final String[][] requirements;
    private final Set<Advancement> children = new LinkedHashSet<>();
    private final Component component;

    public Advancement(Namespaced id, @Nullable Advancement parent, @Nullable AdvancementDisplay display, AdvancementRewards rewards, Map<String, Criterion> criteria, String[][] requirements) {
        this.id = id;
        this.display = display;
        this.criteria = ImmutableMap.copyOf(criteria);
        this.parent = parent;
        this.rewards = rewards;
        this.requirements = requirements;
        if (parent != null) parent.addChild(this);
        if (display == null) {
            component = new TextComponent(id.toString());
        } else {
            component = new TextComponent("[");
            component.getStyle().setColor(display.func_192291_d().func_193229_c());
            Component itextcomponent = display.func_192297_a().copy();
            Component itextcomponent1 = new TextComponent("");
            Component itextcomponent2 = itextcomponent.copy();
            itextcomponent2.getStyle().setColor(display.func_192291_d().func_193229_c());
            itextcomponent1.append(itextcomponent2);
            itextcomponent1.append("\n");
            itextcomponent1.append(display.func_193222_b());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, itextcomponent1));
            component.append(itextcomponent);
            component.append("]");
        }
    }

    public Advancement.Builder asBuilder() {
        return new Advancement.Builder(parent == null ? null : parent.getId(), display, rewards, criteria, requirements);
    }

    public void addChild(Advancement child) {
        children.add(child);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Advancement)) return false;
        Advancement o = (Advancement) other;
        return getId().equals(o.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public static class Builder {
        private final Namespaced field_192061_a;
        private Advancement field_192062_b;
        private final AdvancementDisplay field_192063_c;
        private final AdvancementRewards field_192064_d;
        private final Map<String, Criterion> field_192065_e;
        private final String[][] field_192066_f;

        Builder(@Nullable Namespaced p_i47414_1_, @Nullable AdvancementDisplay p_i47414_2_, AdvancementRewards p_i47414_3_, Map<String, Criterion> p_i47414_4_, String[][] p_i47414_5_) {
            field_192061_a = p_i47414_1_;
            field_192063_c = p_i47414_2_;
            field_192064_d = p_i47414_3_;
            field_192065_e = p_i47414_4_;
            field_192066_f = p_i47414_5_;
        }

        public boolean func_192058_a(Function<Namespaced, Advancement> p_192058_1_) {
            if (field_192061_a == null) {
                return true;
            } else {
                field_192062_b = p_192058_1_.apply(field_192061_a);
                return field_192062_b != null;
            }
        }

        public Advancement func_192056_a(Namespaced p_192056_1_) {
            return new Advancement(p_192056_1_, field_192062_b, field_192063_c, field_192064_d, field_192065_e, field_192066_f);
        }

        public void func_192057_a(PacketBuffer p_192057_1_) {
            if (field_192061_a == null) {
                p_192057_1_.writeBoolean(false);
            } else {
                p_192057_1_.writeBoolean(true);
                p_192057_1_.func_192572_a(field_192061_a);
            }

            if (field_192063_c == null) {
                p_192057_1_.writeBoolean(false);
            } else {
                p_192057_1_.writeBoolean(true);
                field_192063_c.func_192290_a(p_192057_1_);
            }

            Criterion.func_192141_a(field_192065_e, p_192057_1_);
            p_192057_1_.writeVarIntToBuffer(field_192066_f.length);

            for (String[] astring : field_192066_f) {
                p_192057_1_.writeVarIntToBuffer(astring.length);

                for (String s : astring) {
                    p_192057_1_.writeString(s);
                }
            }
        }

        public String toString() {
            return "Task Advancement{parentId=" + field_192061_a + ", display=" + field_192063_c + ", rewards=" + field_192064_d + ", criteria=" + field_192065_e + ", requirements=" + Arrays.deepToString(field_192066_f) + '}';
        }

        public static Advancement.Builder func_192059_a(JsonObject p_192059_0_, JsonDeserializationContext p_192059_1_) {
            Namespaced resourcelocation = p_192059_0_.has("parent") ? new Namespaced(JsonUtils.getString(p_192059_0_, "parent")) : null;
            AdvancementDisplay displayinfo = p_192059_0_.has("display") ? AdvancementDisplay.func_192294_a(JsonUtils.getJsonObject(p_192059_0_, "display"), p_192059_1_) : null;
            AdvancementRewards advancementrewards = JsonUtils.deserializeClass(p_192059_0_, "rewards", AdvancementRewards.field_192114_a, p_192059_1_, AdvancementRewards.class);
            Map<String, Criterion> map = Criterion.func_192144_b(JsonUtils.getJsonObject(p_192059_0_, "criteria"), p_192059_1_);

            if (map.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            } else {
                JsonArray jsonarray = JsonUtils.getJsonArray(p_192059_0_, "requirements", new JsonArray());
                String[][] astring = new String[jsonarray.size()][];

                for (int i = 0; i < jsonarray.size(); ++i) {
                    JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonarray.get(i), "requirements[" + i + "]");
                    astring[i] = new String[jsonarray1.size()];

                    for (int j = 0; j < jsonarray1.size(); ++j) {
                        astring[i][j] = JsonUtils.getString(jsonarray1.get(j), "requirements[" + i + "][" + j + "]");
                    }
                }

                if (astring.length == 0) {
                    astring = new String[map.size()][];
                    int k = 0;

                    for (String s2 : map.keySet()) {
                        astring[k++] = new String[]{s2};
                    }
                }

                for (String[] astring1 : astring) {
                    if (astring1.length == 0 && map.isEmpty()) {
                        throw new JsonSyntaxException("Requirement entry cannot be empty");
                    }

                    for (String s : astring1) {
                        if (!map.containsKey(s)) {
                            throw new JsonSyntaxException("Unknown required criterion '" + s + "'");
                        }
                    }
                }

                for (String s1 : map.keySet()) {
                    boolean flag = false;

                    for (String[] astring2 : astring) {
                        if (ArrayUtils.contains(astring2, s1)) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        throw new JsonSyntaxException("Criterion '" + s1 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
                    }
                }

                return new Advancement.Builder(resourcelocation, displayinfo, advancementrewards, map, astring);
            }
        }

        public static Advancement.Builder func_192060_b(PacketBuffer p_192060_0_) throws IOException {
            Namespaced resourcelocation = p_192060_0_.readBoolean() ? p_192060_0_.func_192575_l() : null;
            AdvancementDisplay displayinfo = p_192060_0_.readBoolean() ? AdvancementDisplay.func_192295_b(p_192060_0_) : null;
            Map<String, Criterion> map = Criterion.func_192142_c(p_192060_0_);
            String[][] astring = new String[p_192060_0_.readVarIntFromBuffer()][];

            for (int i = 0; i < astring.length; ++i) {
                astring[i] = new String[p_192060_0_.readVarIntFromBuffer()];

                for (int j = 0; j < astring[i].length; ++j) {
                    astring[i][j] = p_192060_0_.readStringFromBuffer(32767);
                }
            }

            return new Advancement.Builder(resourcelocation, displayinfo, AdvancementRewards.field_192114_a, map, astring);
        }
    }
}

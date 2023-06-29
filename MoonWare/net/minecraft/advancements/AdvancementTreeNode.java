package net.minecraft.advancements;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

public class AdvancementTreeNode
{
    private final Advancement field_192328_a;
    private final AdvancementTreeNode field_192329_b;
    private final AdvancementTreeNode field_192330_c;
    private final int field_192331_d;
    private final List<AdvancementTreeNode> field_192332_e = Lists.newArrayList();
    private AdvancementTreeNode field_192333_f;
    private AdvancementTreeNode field_192334_g;
    private int field_192335_h;
    private float field_192336_i;
    private float field_192337_j;
    private float field_192338_k;
    private float field_192339_l;

    public AdvancementTreeNode(Advancement p_i47466_1_, @Nullable AdvancementTreeNode p_i47466_2_, @Nullable AdvancementTreeNode p_i47466_3_, int p_i47466_4_, int p_i47466_5_)
    {
        if (p_i47466_1_.getDisplay() == null)
        {
            throw new IllegalArgumentException("Can't position an invisible advancement!");
        }
        else
        {
            field_192328_a = p_i47466_1_;
            field_192329_b = p_i47466_2_;
            field_192330_c = p_i47466_3_;
            field_192331_d = p_i47466_4_;
            field_192333_f = this;
            field_192335_h = p_i47466_5_;
            field_192336_i = -1.0F;
            AdvancementTreeNode advancementtreenode = null;

            for (Advancement advancement : p_i47466_1_.getChildren())
            {
                advancementtreenode = func_192322_a(advancement, advancementtreenode);
            }
        }
    }

    @Nullable
    private AdvancementTreeNode func_192322_a(Advancement p_192322_1_, @Nullable AdvancementTreeNode p_192322_2_)
    {
        if (p_192322_1_.getDisplay() != null)
        {
            p_192322_2_ = new AdvancementTreeNode(p_192322_1_, this, p_192322_2_, field_192332_e.size() + 1, field_192335_h + 1);
            field_192332_e.add(p_192322_2_);
        }
        else
        {
            for (Advancement advancement : p_192322_1_.getChildren())
            {
                p_192322_2_ = func_192322_a(advancement, p_192322_2_);
            }
        }

        return p_192322_2_;
    }

    private void func_192320_a()
    {
        if (field_192332_e.isEmpty())
        {
            if (field_192330_c != null)
            {
                field_192336_i = field_192330_c.field_192336_i + 1.0F;
            }
            else
            {
                field_192336_i = 0.0F;
            }
        }
        else
        {
            AdvancementTreeNode advancementtreenode = null;

            for (AdvancementTreeNode advancementtreenode1 : field_192332_e)
            {
                advancementtreenode1.func_192320_a();
                advancementtreenode = advancementtreenode1.func_192324_a(advancementtreenode == null ? advancementtreenode1 : advancementtreenode);
            }

            func_192325_b();
            float f = ((field_192332_e.get(0)).field_192336_i + (field_192332_e.get(field_192332_e.size() - 1)).field_192336_i) / 2.0F;

            if (field_192330_c != null)
            {
                field_192336_i = field_192330_c.field_192336_i + 1.0F;
                field_192337_j = field_192336_i - f;
            }
            else
            {
                field_192336_i = f;
            }
        }
    }

    private float func_192319_a(float p_192319_1_, int p_192319_2_, float p_192319_3_)
    {
        field_192336_i += p_192319_1_;
        field_192335_h = p_192319_2_;

        if (field_192336_i < p_192319_3_)
        {
            p_192319_3_ = field_192336_i;
        }

        for (AdvancementTreeNode advancementtreenode : field_192332_e)
        {
            p_192319_3_ = advancementtreenode.func_192319_a(p_192319_1_ + field_192337_j, p_192319_2_ + 1, p_192319_3_);
        }

        return p_192319_3_;
    }

    private void func_192318_a(float p_192318_1_)
    {
        field_192336_i += p_192318_1_;

        for (AdvancementTreeNode advancementtreenode : field_192332_e)
        {
            advancementtreenode.func_192318_a(p_192318_1_);
        }
    }

    private void func_192325_b()
    {
        float f = 0.0F;
        float f1 = 0.0F;

        for (int i = field_192332_e.size() - 1; i >= 0; --i)
        {
            AdvancementTreeNode advancementtreenode = field_192332_e.get(i);
            advancementtreenode.field_192336_i += f;
            advancementtreenode.field_192337_j += f;
            f1 += advancementtreenode.field_192338_k;
            f += advancementtreenode.field_192339_l + f1;
        }
    }

    @Nullable
    private AdvancementTreeNode func_192321_c()
    {
        if (field_192334_g != null)
        {
            return field_192334_g;
        }
        else
        {
            return !field_192332_e.isEmpty() ? field_192332_e.get(0) : null;
        }
    }

    @Nullable
    private AdvancementTreeNode func_192317_d()
    {
        if (field_192334_g != null)
        {
            return field_192334_g;
        }
        else
        {
            return !field_192332_e.isEmpty() ? field_192332_e.get(field_192332_e.size() - 1) : null;
        }
    }

    private AdvancementTreeNode func_192324_a(AdvancementTreeNode p_192324_1_)
    {
        if (field_192330_c == null)
        {
            return p_192324_1_;
        }
        else
        {
            AdvancementTreeNode advancementtreenode = this;
            AdvancementTreeNode advancementtreenode1 = this;
            AdvancementTreeNode advancementtreenode2 = field_192330_c;
            AdvancementTreeNode advancementtreenode3 = field_192329_b.field_192332_e.get(0);
            float f = field_192337_j;
            float f1 = field_192337_j;
            float f2 = advancementtreenode2.field_192337_j;
            float f3;

            for (f3 = advancementtreenode3.field_192337_j; advancementtreenode2.func_192317_d() != null && advancementtreenode.func_192321_c() != null; f1 += advancementtreenode1.field_192337_j)
            {
                advancementtreenode2 = advancementtreenode2.func_192317_d();
                advancementtreenode = advancementtreenode.func_192321_c();
                advancementtreenode3 = advancementtreenode3.func_192321_c();
                advancementtreenode1 = advancementtreenode1.func_192317_d();
                advancementtreenode1.field_192333_f = this;
                float f4 = advancementtreenode2.field_192336_i + f2 - (advancementtreenode.field_192336_i + f) + 1.0F;

                if (f4 > 0.0F)
                {
                    advancementtreenode2.func_192326_a(this, p_192324_1_).func_192316_a(this, f4);
                    f += f4;
                    f1 += f4;
                }

                f2 += advancementtreenode2.field_192337_j;
                f += advancementtreenode.field_192337_j;
                f3 += advancementtreenode3.field_192337_j;
            }

            if (advancementtreenode2.func_192317_d() != null && advancementtreenode1.func_192317_d() == null)
            {
                advancementtreenode1.field_192334_g = advancementtreenode2.func_192317_d();
                advancementtreenode1.field_192337_j += f2 - f1;
            }
            else
            {
                if (advancementtreenode.func_192321_c() != null && advancementtreenode3.func_192321_c() == null)
                {
                    advancementtreenode3.field_192334_g = advancementtreenode.func_192321_c();
                    advancementtreenode3.field_192337_j += f - f3;
                }

                p_192324_1_ = this;
            }

            return p_192324_1_;
        }
    }

    private void func_192316_a(AdvancementTreeNode p_192316_1_, float p_192316_2_)
    {
        float f = (float)(p_192316_1_.field_192331_d - field_192331_d);

        if (f != 0.0F)
        {
            p_192316_1_.field_192338_k -= p_192316_2_ / f;
            field_192338_k += p_192316_2_ / f;
        }

        p_192316_1_.field_192339_l += p_192316_2_;
        p_192316_1_.field_192336_i += p_192316_2_;
        p_192316_1_.field_192337_j += p_192316_2_;
    }

    private AdvancementTreeNode func_192326_a(AdvancementTreeNode p_192326_1_, AdvancementTreeNode p_192326_2_)
    {
        return field_192333_f != null && p_192326_1_.field_192329_b.field_192332_e.contains(field_192333_f) ? field_192333_f : p_192326_2_;
    }

    private void func_192327_e()
    {
        if (field_192328_a.getDisplay() != null)
        {
            field_192328_a.getDisplay().func_192292_a((float) field_192335_h, field_192336_i);
        }

        if (!field_192332_e.isEmpty())
        {
            for (AdvancementTreeNode advancementtreenode : field_192332_e)
            {
                advancementtreenode.func_192327_e();
            }
        }
    }

    public static void func_192323_a(Advancement p_192323_0_)
    {
        if (p_192323_0_.getDisplay() == null)
        {
            throw new IllegalArgumentException("Can't position children of an invisible root!");
        }
        else
        {
            AdvancementTreeNode advancementtreenode = new AdvancementTreeNode(p_192323_0_, null, null, 1, 0);
            advancementtreenode.func_192320_a();
            float f = advancementtreenode.func_192319_a(0.0F, 0, advancementtreenode.field_192336_i);

            if (f < 0.0F)
            {
                advancementtreenode.func_192318_a(-f);
            }

            advancementtreenode.func_192327_e();
        }
    }
}

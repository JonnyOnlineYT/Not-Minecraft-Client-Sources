package optifine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Namespaced;
import org.lwjgl.opengl.GL11;

public class CustomItemProperties
{
    public String name;
    public String basePath;
    public int type = 1;
    public int[] items;
    public String texture;
    public Map<String, String> mapTextures;
    public String model;
    public Map<String, String> mapModels;
    public RangeListInt damage;
    public boolean damagePercent;
    public int damageMask;
    public RangeListInt stackSize;
    public RangeListInt enchantmentIds;
    public RangeListInt enchantmentLevels;
    public NbtTagValue[] nbtTagValues;
    public int hand;
    public int blend = 1;
    public float speed;
    public float rotation;
    public int layer;
    public float duration = 1.0F;
    public int weight;
    public Namespaced textureLocation;
    public Map mapTextureLocations;
    public TextureAtlasSprite sprite;
    public Map mapSprites;
    public IBakedModel bakedModelTexture;
    public Map<String, IBakedModel> mapBakedModelsTexture;
    public IBakedModel bakedModelFull;
    public Map<String, IBakedModel> mapBakedModelsFull;
    private int textureWidth;
    private int textureHeight;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;
    public static final int TYPE_ELYTRA = 4;
    public static final int HAND_ANY = 0;
    public static final int HAND_MAIN = 1;
    public static final int HAND_OFF = 2;
    public static final String INVENTORY = "inventory";

    public CustomItemProperties(Properties p_i33_1_, String p_i33_2_)
    {
        name = parseName(p_i33_2_);
        basePath = parseBasePath(p_i33_2_);
        type = parseType(p_i33_1_.getProperty("type"));
        items = parseItems(p_i33_1_.getProperty("items"), p_i33_1_.getProperty("matchItems"));
        mapModels = parseModels(p_i33_1_, basePath);
        model = parseModel(p_i33_1_.getProperty("model"), p_i33_2_, basePath, type, mapModels);
        mapTextures = parseTextures(p_i33_1_, basePath);
        boolean flag = mapModels == null && model == null;
        texture = parseTexture(p_i33_1_.getProperty("texture"), p_i33_1_.getProperty("tile"), p_i33_1_.getProperty("source"), p_i33_2_, basePath, type, mapTextures, flag);
        String s = p_i33_1_.getProperty("damage");

        if (s != null)
        {
            damagePercent = s.contains("%");
            s = s.replace("%", "");
            damage = parseRangeListInt(s);
            damageMask = parseInt(p_i33_1_.getProperty("damageMask"), 0);
        }

        stackSize = parseRangeListInt(p_i33_1_.getProperty("stackSize"));
        enchantmentIds = parseRangeListInt(p_i33_1_.getProperty("enchantmentIDs"), new ParserEnchantmentId());
        enchantmentLevels = parseRangeListInt(p_i33_1_.getProperty("enchantmentLevels"));
        nbtTagValues = parseNbtTagValues(p_i33_1_);
        hand = parseHand(p_i33_1_.getProperty("hand"));
        blend = Blender.parseBlend(p_i33_1_.getProperty("blend"));
        speed = parseFloat(p_i33_1_.getProperty("speed"), 0.0F);
        rotation = parseFloat(p_i33_1_.getProperty("rotation"), 0.0F);
        layer = parseInt(p_i33_1_.getProperty("layer"), 0);
        weight = parseInt(p_i33_1_.getProperty("weight"), 0);
        duration = parseFloat(p_i33_1_.getProperty("duration"), 1.0F);
    }

    private static String parseName(String p_parseName_0_)
    {
        String s = p_parseName_0_;
        int i = p_parseName_0_.lastIndexOf(47);

        if (i >= 0)
        {
            s = p_parseName_0_.substring(i + 1);
        }

        int j = s.lastIndexOf(46);

        if (j >= 0)
        {
            s = s.substring(0, j);
        }

        return s;
    }

    private static String parseBasePath(String p_parseBasePath_0_)
    {
        int i = p_parseBasePath_0_.lastIndexOf(47);
        return i < 0 ? "" : p_parseBasePath_0_.substring(0, i);
    }

    private int parseType(String p_parseType_1_)
    {
        if (p_parseType_1_ == null)
        {
            return 1;
        }
        else if (p_parseType_1_.equals("item"))
        {
            return 1;
        }
        else if (p_parseType_1_.equals("enchantment"))
        {
            return 2;
        }
        else if (p_parseType_1_.equals("armor"))
        {
            return 3;
        }
        else if (p_parseType_1_.equals("elytra"))
        {
            return 4;
        }
        else
        {
            Config.warn("Unknown method: " + p_parseType_1_);
            return 0;
        }
    }

    private int[] parseItems(String p_parseItems_1_, String p_parseItems_2_)
    {
        if (p_parseItems_1_ == null)
        {
            p_parseItems_1_ = p_parseItems_2_;
        }

        if (p_parseItems_1_ == null)
        {
            return null;
        }
        else
        {
            p_parseItems_1_ = p_parseItems_1_.trim();
            Set set = new TreeSet();
            String[] astring = Config.tokenize(p_parseItems_1_, " ");
            label57:

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                int j = Config.parseInt(s, -1);

                if (j >= 0)
                {
                    set.add(new Integer(j));
                }
                else
                {
                    if (s.contains("-"))
                    {
                        String[] astring1 = Config.tokenize(s, "-");

                        if (astring1.length == 2)
                        {
                            int k = Config.parseInt(astring1[0], -1);
                            int l = Config.parseInt(astring1[1], -1);

                            if (k >= 0 && l >= 0)
                            {
                                int i1 = Math.min(k, l);
                                int j1 = Math.max(k, l);
                                int k1 = i1;

                                while (true)
                                {
                                    if (k1 > j1)
                                    {
                                        continue label57;
                                    }

                                    set.add(new Integer(k1));
                                    ++k1;
                                }
                            }
                        }
                    }

                    Item item = Item.getByNameOrId(s);

                    if (item == null)
                    {
                        Config.warn("Item not found: " + s);
                    }
                    else
                    {
                        int i2 = Item.getIdFromItem(item);

                        if (i2 <= 0)
                        {
                            Config.warn("Item not found: " + s);
                        }
                        else
                        {
                            set.add(new Integer(i2));
                        }
                    }
                }
            }

            Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
            int[] aint = new int[ainteger.length];

            for (int l1 = 0; l1 < aint.length; ++l1)
            {
                aint[l1] = ainteger[l1].intValue();
            }

            return aint;
        }
    }

    private static String parseTexture(String p_parseTexture_0_, String p_parseTexture_1_, String p_parseTexture_2_, String p_parseTexture_3_, String p_parseTexture_4_, int p_parseTexture_5_, Map<String, String> p_parseTexture_6_, boolean p_parseTexture_7_)
    {
        if (p_parseTexture_0_ == null)
        {
            p_parseTexture_0_ = p_parseTexture_1_;
        }

        if (p_parseTexture_0_ == null)
        {
            p_parseTexture_0_ = p_parseTexture_2_;
        }

        if (p_parseTexture_0_ != null)
        {
            String s2 = ".png";

            if (p_parseTexture_0_.endsWith(s2))
            {
                p_parseTexture_0_ = p_parseTexture_0_.substring(0, p_parseTexture_0_.length() - s2.length());
            }

            p_parseTexture_0_ = fixTextureName(p_parseTexture_0_, p_parseTexture_4_);
            return p_parseTexture_0_;
        }
        else if (p_parseTexture_5_ == 3)
        {
            return null;
        }
        else
        {
            if (p_parseTexture_6_ != null)
            {
                String s = p_parseTexture_6_.get("texture.bow_standby");

                if (s != null)
                {
                    return s;
                }
            }

            if (!p_parseTexture_7_)
            {
                return null;
            }
            else
            {
                String s1 = p_parseTexture_3_;
                int i = p_parseTexture_3_.lastIndexOf(47);

                if (i >= 0)
                {
                    s1 = p_parseTexture_3_.substring(i + 1);
                }

                int j = s1.lastIndexOf(46);

                if (j >= 0)
                {
                    s1 = s1.substring(0, j);
                }

                s1 = fixTextureName(s1, p_parseTexture_4_);
                return s1;
            }
        }
    }

    private static Map parseTextures(Properties p_parseTextures_0_, String p_parseTextures_1_)
    {
        String s = "texture.";
        Map map = getMatchingProperties(p_parseTextures_0_, s);

        if (map.size() <= 0)
        {
            return null;
        }
        else
        {
            Set set = map.keySet();
            Map map1 = new LinkedHashMap();

            for (Object s1 : set)
            {
                String s2 = (String)map.get(s1);
                s2 = fixTextureName(s2, p_parseTextures_1_);
                map1.put(s1, s2);
            }

            return map1;
        }
    }

    private static String fixTextureName(String p_fixTextureName_0_, String p_fixTextureName_1_)
    {
        p_fixTextureName_0_ = TextureUtils.fixResourcePath(p_fixTextureName_0_, p_fixTextureName_1_);

        if (!p_fixTextureName_0_.startsWith(p_fixTextureName_1_) && !p_fixTextureName_0_.startsWith("textures/") && !p_fixTextureName_0_.startsWith("mcpatcher/"))
        {
            p_fixTextureName_0_ = p_fixTextureName_1_ + "/" + p_fixTextureName_0_;
        }

        if (p_fixTextureName_0_.endsWith(".png"))
        {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(0, p_fixTextureName_0_.length() - 4);
        }

        if (p_fixTextureName_0_.startsWith("/"))
        {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(1);
        }

        return p_fixTextureName_0_;
    }

    private static String parseModel(String p_parseModel_0_, String p_parseModel_1_, String p_parseModel_2_, int p_parseModel_3_, Map<String, String> p_parseModel_4_)
    {
        if (p_parseModel_0_ != null)
        {
            String s1 = ".json";

            if (p_parseModel_0_.endsWith(s1))
            {
                p_parseModel_0_ = p_parseModel_0_.substring(0, p_parseModel_0_.length() - s1.length());
            }

            p_parseModel_0_ = fixModelName(p_parseModel_0_, p_parseModel_2_);
            return p_parseModel_0_;
        }
        else if (p_parseModel_3_ == 3)
        {
            return null;
        }
        else
        {
            if (p_parseModel_4_ != null)
            {
                String s = p_parseModel_4_.get("model.bow_standby");

                if (s != null)
                {
                    return s;
                }
            }

            return p_parseModel_0_;
        }
    }

    private static Map parseModels(Properties p_parseModels_0_, String p_parseModels_1_)
    {
        String s = "model.";
        Map map = getMatchingProperties(p_parseModels_0_, s);

        if (map.size() <= 0)
        {
            return null;
        }
        else
        {
            Set set = map.keySet();
            Map map1 = new LinkedHashMap();

            for (Object s1 : set)
            {
                String s2 = (String)map.get(s1);
                s2 = fixModelName(s2, p_parseModels_1_);
                map1.put(s1, s2);
            }

            return map1;
        }
    }

    private static String fixModelName(String p_fixModelName_0_, String p_fixModelName_1_)
    {
        p_fixModelName_0_ = TextureUtils.fixResourcePath(p_fixModelName_0_, p_fixModelName_1_);
        boolean flag = p_fixModelName_0_.startsWith("block/") || p_fixModelName_0_.startsWith("item/");

        if (!p_fixModelName_0_.startsWith(p_fixModelName_1_) && !flag && !p_fixModelName_0_.startsWith("mcpatcher/"))
        {
            p_fixModelName_0_ = p_fixModelName_1_ + "/" + p_fixModelName_0_;
        }

        String s = ".json";

        if (p_fixModelName_0_.endsWith(s))
        {
            p_fixModelName_0_ = p_fixModelName_0_.substring(0, p_fixModelName_0_.length() - s.length());
        }

        if (p_fixModelName_0_.startsWith("/"))
        {
            p_fixModelName_0_ = p_fixModelName_0_.substring(1);
        }

        return p_fixModelName_0_;
    }

    private int parseInt(String p_parseInt_1_, int p_parseInt_2_)
    {
        if (p_parseInt_1_ == null)
        {
            return p_parseInt_2_;
        }
        else
        {
            p_parseInt_1_ = p_parseInt_1_.trim();
            int i = Config.parseInt(p_parseInt_1_, Integer.MIN_VALUE);

            if (i == Integer.MIN_VALUE)
            {
                Config.warn("Invalid integer: " + p_parseInt_1_);
                return p_parseInt_2_;
            }
            else
            {
                return i;
            }
        }
    }

    private float parseFloat(String p_parseFloat_1_, float p_parseFloat_2_)
    {
        if (p_parseFloat_1_ == null)
        {
            return p_parseFloat_2_;
        }
        else
        {
            p_parseFloat_1_ = p_parseFloat_1_.trim();
            float f = Config.parseFloat(p_parseFloat_1_, Float.MIN_VALUE);

            if (f == Float.MIN_VALUE)
            {
                Config.warn("Invalid float: " + p_parseFloat_1_);
                return p_parseFloat_2_;
            }
            else
            {
                return f;
            }
        }
    }

    private RangeListInt parseRangeListInt(String p_parseRangeListInt_1_)
    {
        return parseRangeListInt(p_parseRangeListInt_1_, null);
    }

    private RangeListInt parseRangeListInt(String p_parseRangeListInt_1_, IParserInt p_parseRangeListInt_2_)
    {
        if (p_parseRangeListInt_1_ == null)
        {
            return null;
        }
        else
        {
            String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ");
            RangeListInt rangelistint = new RangeListInt();

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (p_parseRangeListInt_2_ != null)
                {
                    int j = p_parseRangeListInt_2_.parse(s, Integer.MIN_VALUE);

                    if (j != Integer.MIN_VALUE)
                    {
                        rangelistint.addRange(new RangeInt(j, j));
                        continue;
                    }
                }

                RangeInt rangeint = parseRangeInt(s);

                if (rangeint == null)
                {
                    Config.warn("Invalid range list: " + p_parseRangeListInt_1_);
                    return null;
                }

                rangelistint.addRange(rangeint);
            }

            return rangelistint;
        }
    }

    private RangeInt parseRangeInt(String p_parseRangeInt_1_)
    {
        if (p_parseRangeInt_1_ == null)
        {
            return null;
        }
        else
        {
            p_parseRangeInt_1_ = p_parseRangeInt_1_.trim();
            int i = p_parseRangeInt_1_.length() - p_parseRangeInt_1_.replace("-", "").length();

            if (i > 1)
            {
                Config.warn("Invalid range: " + p_parseRangeInt_1_);
                return null;
            }
            else
            {
                String[] astring = Config.tokenize(p_parseRangeInt_1_, "- ");
                int[] aint = new int[astring.length];

                for (int j = 0; j < astring.length; ++j)
                {
                    String s = astring[j];
                    int k = Config.parseInt(s, -1);

                    if (k < 0)
                    {
                        Config.warn("Invalid range: " + p_parseRangeInt_1_);
                        return null;
                    }

                    aint[j] = k;
                }

                if (aint.length == 1)
                {
                    int i1 = aint[0];

                    if (p_parseRangeInt_1_.startsWith("-"))
                    {
                        return new RangeInt(0, i1);
                    }
                    else if (p_parseRangeInt_1_.endsWith("-"))
                    {
                        return new RangeInt(i1, 65535);
                    }
                    else
                    {
                        return new RangeInt(i1, i1);
                    }
                }
                else if (aint.length == 2)
                {
                    int l = Math.min(aint[0], aint[1]);
                    int j1 = Math.max(aint[0], aint[1]);
                    return new RangeInt(l, j1);
                }
                else
                {
                    Config.warn("Invalid range: " + p_parseRangeInt_1_);
                    return null;
                }
            }
        }
    }

    private NbtTagValue[] parseNbtTagValues(Properties p_parseNbtTagValues_1_)
    {
        String s = "nbt.";
        Map map = getMatchingProperties(p_parseNbtTagValues_1_, s);

        if (map.size() <= 0)
        {
            return null;
        }
        else
        {
            List list = new ArrayList();

            for (Object s1 : map.keySet())
            {
                String s2 = (String)map.get(s1);
                String s3 = ((String) s1).substring(s.length());
                NbtTagValue nbttagvalue = new NbtTagValue(s3, s2);
                list.add(nbttagvalue);
            }

            NbtTagValue[] anbttagvalue = (NbtTagValue[])list.toArray(new NbtTagValue[list.size()]);
            return anbttagvalue;
        }
    }

    private static Map getMatchingProperties(Properties p_getMatchingProperties_0_, String p_getMatchingProperties_1_)
    {
        Map map = new LinkedHashMap();

        for (Object s : p_getMatchingProperties_0_.keySet())
        {
            String s1 = p_getMatchingProperties_0_.getProperty((String) s);

            if (((String) s).startsWith(p_getMatchingProperties_1_))
            {
                map.put(s, s1);
            }
        }

        return map;
    }

    private int parseHand(String p_parseHand_1_)
    {
        if (p_parseHand_1_ == null)
        {
            return 0;
        }
        else
        {
            p_parseHand_1_ = p_parseHand_1_.toLowerCase();

            if (p_parseHand_1_.equals("any"))
            {
                return 0;
            }
            else if (p_parseHand_1_.equals("main"))
            {
                return 1;
            }
            else if (p_parseHand_1_.equals("off"))
            {
                return 2;
            }
            else
            {
                Config.warn("Invalid hand: " + p_parseHand_1_);
                return 0;
            }
        }
    }

    public boolean isValid(String p_isValid_1_)
    {
        if (name != null && name.length() > 0)
        {
            if (basePath == null)
            {
                Config.warn("No base path found: " + p_isValid_1_);
                return false;
            }
            else if (type == 0)
            {
                Config.warn("No type defined: " + p_isValid_1_);
                return false;
            }
            else
            {
                if (type == 4 && items == null)
                {
                    items = new int[] {Item.getIdFromItem(Items.ELYTRA)};
                }

                if (type == 1 || type == 3 || type == 4)
                {
                    if (items == null)
                    {
                        items = detectItems();
                    }

                    if (items == null)
                    {
                        Config.warn("No items defined: " + p_isValid_1_);
                        return false;
                    }
                }

                if (texture == null && mapTextures == null && model == null && mapModels == null)
                {
                    Config.warn("No texture or model specified: " + p_isValid_1_);
                    return false;
                }
                else if (type == 2 && enchantmentIds == null)
                {
                    Config.warn("No enchantmentIDs specified: " + p_isValid_1_);
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        else
        {
            Config.warn("No name found: " + p_isValid_1_);
            return false;
        }
    }

    private int[] detectItems()
    {
        Item item = Item.getByNameOrId(name);

        if (item == null)
        {
            return null;
        }
        else
        {
            int i = Item.getIdFromItem(item);
            return i <= 0 ? null : new int[] {i};
        }
    }

    public void updateIcons(TextureMap p_updateIcons_1_)
    {
        if (texture != null)
        {
            textureLocation = getTextureLocation(texture);

            if (type == 1)
            {
                Namespaced resourcelocation = getSpriteLocation(textureLocation);
                sprite = p_updateIcons_1_.registerSprite(resourcelocation);
            }
        }

        if (mapTextures != null)
        {
            mapTextureLocations = new HashMap();
            mapSprites = new HashMap();

            for (String s : mapTextures.keySet())
            {
                String s1 = mapTextures.get(s);
                Namespaced resourcelocation1 = getTextureLocation(s1);
                mapTextureLocations.put(s, resourcelocation1);

                if (type == 1)
                {
                    Namespaced resourcelocation2 = getSpriteLocation(resourcelocation1);
                    TextureAtlasSprite textureatlassprite = p_updateIcons_1_.registerSprite(resourcelocation2);
                    mapSprites.put(s, textureatlassprite);
                }
            }
        }
    }

    private Namespaced getTextureLocation(String p_getTextureLocation_1_)
    {
        if (p_getTextureLocation_1_ == null)
        {
            return null;
        }
        else
        {
            Namespaced resourcelocation = new Namespaced(p_getTextureLocation_1_);
            String s = resourcelocation.getNamespace();
            String s1 = resourcelocation.getPath();

            if (!s1.contains("/"))
            {
                s1 = "textures/items/" + s1;
            }

            String s2 = s1 + ".png";
            Namespaced resourcelocation1 = new Namespaced(s, s2);
            boolean flag = Config.hasResource(resourcelocation1);

            if (!flag)
            {
                Config.warn("File not found: " + s2);
            }

            return resourcelocation1;
        }
    }

    private Namespaced getSpriteLocation(Namespaced p_getSpriteLocation_1_)
    {
        String s = p_getSpriteLocation_1_.getPath();
        s = StrUtils.removePrefix(s, "textures/");
        s = StrUtils.removeSuffix(s, ".png");
        Namespaced resourcelocation = new Namespaced(p_getSpriteLocation_1_.getNamespace(), s);
        return resourcelocation;
    }

    public void updateModelTexture(TextureMap p_updateModelTexture_1_, ItemModelGenerator p_updateModelTexture_2_)
    {
        if (texture != null || mapTextures != null)
        {
            String[] astring = getModelTextures();
            boolean flag = isUseTint();
            bakedModelTexture = makeBakedModel(p_updateModelTexture_1_, p_updateModelTexture_2_, astring, flag);

            if (type == 1 && mapTextures != null)
            {
                for (String s : mapTextures.keySet())
                {
                    String s1 = mapTextures.get(s);
                    String s2 = StrUtils.removePrefix(s, "texture.");

                    if (s2.startsWith("bow") || s2.startsWith("fishing_rod"))
                    {
                        String[] astring1 = {s1};
                        IBakedModel ibakedmodel = makeBakedModel(p_updateModelTexture_1_, p_updateModelTexture_2_, astring1, flag);

                        if (mapBakedModelsTexture == null)
                        {
                            mapBakedModelsTexture = new HashMap<String, IBakedModel>();
                        }

                        String s3 = "item/" + s2;
                        mapBakedModelsTexture.put(s3, ibakedmodel);
                    }
                }
            }
        }
    }

    private boolean isUseTint()
    {
        return true;
    }

    private static IBakedModel makeBakedModel(TextureMap p_makeBakedModel_0_, ItemModelGenerator p_makeBakedModel_1_, String[] p_makeBakedModel_2_, boolean p_makeBakedModel_3_)
    {
        String[] astring = new String[p_makeBakedModel_2_.length];

        for (int i = 0; i < astring.length; ++i)
        {
            String s = p_makeBakedModel_2_[i];
            astring[i] = StrUtils.removePrefix(s, "textures/");
        }

        ModelBlock modelblock = makeModelBlock(astring);
        ModelBlock modelblock1 = p_makeBakedModel_1_.makeItemModel(p_makeBakedModel_0_, modelblock);
        IBakedModel ibakedmodel = bakeModel(p_makeBakedModel_0_, modelblock1, p_makeBakedModel_3_);
        return ibakedmodel;
    }

    private String[] getModelTextures()
    {
        if (type == 1 && items.length == 1)
        {
            Item item = Item.getItemById(items[0]);
            boolean flag = item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;

            if (flag && damage != null && damage.getCountRanges() > 0)
            {
                RangeInt rangeint = damage.getRange(0);
                int i = rangeint.getMin();
                boolean flag1 = (i & 16384) != 0;
                String s5 = getMapTexture(mapTextures, "texture.potion_overlay", "items/potion_overlay");
                String s6 = null;

                if (flag1)
                {
                    s6 = getMapTexture(mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
                }
                else
                {
                    s6 = getMapTexture(mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                }

                return new String[] {s5, s6};
            }

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;

                if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER)
                {
                    String s = "leather";
                    String s1 = "helmet";

                    if (itemarmor.armorType == EntityEquipmentSlot.HEAD)
                    {
                        s1 = "helmet";
                    }

                    if (itemarmor.armorType == EntityEquipmentSlot.CHEST)
                    {
                        s1 = "chestplate";
                    }

                    if (itemarmor.armorType == EntityEquipmentSlot.LEGS)
                    {
                        s1 = "leggings";
                    }

                    if (itemarmor.armorType == EntityEquipmentSlot.FEET)
                    {
                        s1 = "boots";
                    }

                    String s2 = s + "_" + s1;
                    String s3 = getMapTexture(mapTextures, "texture." + s2, "items/" + s2);
                    String s4 = getMapTexture(mapTextures, "texture." + s2 + "_overlay", "items/" + s2 + "_overlay");
                    return new String[] {s3, s4};
                }
            }
        }

        return new String[] {texture};
    }

    private String getMapTexture(Map<String, String> p_getMapTexture_1_, String p_getMapTexture_2_, String p_getMapTexture_3_)
    {
        if (p_getMapTexture_1_ == null)
        {
            return p_getMapTexture_3_;
        }
        else
        {
            String s = p_getMapTexture_1_.get(p_getMapTexture_2_);
            return s == null ? p_getMapTexture_3_ : s;
        }
    }

    private static ModelBlock makeModelBlock(String[] p_makeModelBlock_0_)
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("{\"parent\": \"builtin/generated\",\"textures\": {");

        for (int i = 0; i < p_makeModelBlock_0_.length; ++i)
        {
            String s = p_makeModelBlock_0_[i];

            if (i > 0)
            {
                stringbuffer.append(", ");
            }

            stringbuffer.append("\"layer" + i + "\": \"" + s + "\"");
        }

        stringbuffer.append("}}");
        String s1 = stringbuffer.toString();
        ModelBlock modelblock = ModelBlock.deserialize(s1);
        return modelblock;
    }

    private static IBakedModel bakeModel(TextureMap p_bakeModel_0_, ModelBlock p_bakeModel_1_, boolean p_bakeModel_2_)
    {
        ModelRotation modelrotation = ModelRotation.X0_Y0;
        boolean flag = false;
        String s = p_bakeModel_1_.resolveTextureName("particle");
        TextureAtlasSprite textureatlassprite = p_bakeModel_0_.getAtlasSprite((new Namespaced(s)).toString());
        SimpleBakedModel.Builder simplebakedmodel$builder = (new SimpleBakedModel.Builder(p_bakeModel_1_, p_bakeModel_1_.createOverrides())).setTexture(textureatlassprite);

        for (BlockPart blockpart : p_bakeModel_1_.getElements())
        {
            for (EnumFacing enumfacing : blockpart.mapFaces.keySet())
            {
                BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);

                if (!p_bakeModel_2_)
                {
                    blockpartface = new BlockPartFace(blockpartface.cullFace, -1, blockpartface.texture, blockpartface.blockFaceUV);
                }

                String s1 = p_bakeModel_1_.resolveTextureName(blockpartface.texture);
                TextureAtlasSprite textureatlassprite1 = p_bakeModel_0_.getAtlasSprite((new Namespaced(s1)).toString());
                BakedQuad bakedquad = makeBakedQuad(blockpart, blockpartface, textureatlassprite1, enumfacing, modelrotation, flag);

                if (blockpartface.cullFace == null)
                {
                    simplebakedmodel$builder.addGeneralQuad(bakedquad);
                }
                else
                {
                    simplebakedmodel$builder.addFaceQuad(modelrotation.rotateFace(blockpartface.cullFace), bakedquad);
                }
            }
        }

        return simplebakedmodel$builder.makeBakedModel();
    }

    private static BakedQuad makeBakedQuad(BlockPart p_makeBakedQuad_0_, BlockPartFace p_makeBakedQuad_1_, TextureAtlasSprite p_makeBakedQuad_2_, EnumFacing p_makeBakedQuad_3_, ModelRotation p_makeBakedQuad_4_, boolean p_makeBakedQuad_5_)
    {
        FaceBakery facebakery = new FaceBakery();
        return facebakery.makeBakedQuad(p_makeBakedQuad_0_.positionFrom, p_makeBakedQuad_0_.positionTo, p_makeBakedQuad_1_, p_makeBakedQuad_2_, p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_0_.partRotation, p_makeBakedQuad_5_, p_makeBakedQuad_0_.shade);
    }

    public String toString()
    {
        return "" + basePath + "/" + name + ", type: " + type + ", items: [" + Config.arrayToString(items) + "], textture: " + texture;
    }

    public float getTextureWidth(TextureManager p_getTextureWidth_1_)
    {
        if (textureWidth <= 0)
        {
            if (textureLocation != null)
            {
                ITextureObject itextureobject = p_getTextureWidth_1_.getTexture(textureLocation);
                int i = itextureobject.getGlTextureId();
                int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                GlStateManager.bindTexture(j);
            }

            if (textureWidth <= 0)
            {
                textureWidth = 16;
            }
        }

        return (float) textureWidth;
    }

    public float getTextureHeight(TextureManager p_getTextureHeight_1_)
    {
        if (textureHeight <= 0)
        {
            if (textureLocation != null)
            {
                ITextureObject itextureobject = p_getTextureHeight_1_.getTexture(textureLocation);
                int i = itextureobject.getGlTextureId();
                int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                textureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
                GlStateManager.bindTexture(j);
            }

            if (textureHeight <= 0)
            {
                textureHeight = 16;
            }
        }

        return (float) textureHeight;
    }

    public IBakedModel getBakedModel(Namespaced p_getBakedModel_1_, boolean p_getBakedModel_2_)
    {
        IBakedModel ibakedmodel;
        Map<String, IBakedModel> map;

        if (p_getBakedModel_2_)
        {
            ibakedmodel = bakedModelFull;
            map = mapBakedModelsFull;
        }
        else
        {
            ibakedmodel = bakedModelTexture;
            map = mapBakedModelsTexture;
        }

        if (p_getBakedModel_1_ != null && map != null)
        {
            String s = p_getBakedModel_1_.getPath();
            IBakedModel ibakedmodel1 = map.get(s);

            if (ibakedmodel1 != null)
            {
                return ibakedmodel1;
            }
        }

        return ibakedmodel;
    }

    public void loadModels(ModelBakery p_loadModels_1_)
    {
        if (model != null)
        {
            loadItemModel(p_loadModels_1_, model);
        }

        if (type == 1 && mapModels != null)
        {
            for (String s : mapModels.keySet())
            {
                String s1 = mapModels.get(s);
                String s2 = StrUtils.removePrefix(s, "model.");

                if (s2.startsWith("bow") || s2.startsWith("fishing_rod"))
                {
                    loadItemModel(p_loadModels_1_, s1);
                }
            }
        }
    }

    public void updateModelsFull()
    {
        ModelManager modelmanager = Config.getModelManager();
        IBakedModel ibakedmodel = modelmanager.getMissingModel();

        if (model != null)
        {
            Namespaced resourcelocation = getModelLocation(model);
            ModelNamespaced modelresourcelocation = new ModelNamespaced(resourcelocation, "inventory");
            bakedModelFull = modelmanager.getModel(modelresourcelocation);

            if (bakedModelFull == ibakedmodel)
            {
                Config.warn("Custom Items: Model not found " + modelresourcelocation.getPath());
                bakedModelFull = null;
            }
        }

        if (type == 1 && mapModels != null)
        {
            for (String s : mapModels.keySet())
            {
                String s1 = mapModels.get(s);
                String s2 = StrUtils.removePrefix(s, "model.");

                if (s2.startsWith("bow") || s2.startsWith("fishing_rod"))
                {
                    Namespaced resourcelocation1 = getModelLocation(s1);
                    ModelNamespaced modelresourcelocation1 = new ModelNamespaced(resourcelocation1, "inventory");
                    IBakedModel ibakedmodel1 = modelmanager.getModel(modelresourcelocation1);

                    if (ibakedmodel1 == ibakedmodel)
                    {
                        Config.warn("Custom Items: Model not found " + modelresourcelocation1.getPath());
                    }
                    else
                    {
                        if (mapBakedModelsFull == null)
                        {
                            mapBakedModelsFull = new HashMap<String, IBakedModel>();
                        }

                        String s3 = "item/" + s2;
                        mapBakedModelsFull.put(s3, ibakedmodel1);
                    }
                }
            }
        }
    }

    private static void loadItemModel(ModelBakery p_loadItemModel_0_, String p_loadItemModel_1_)
    {
        Namespaced resourcelocation = getModelLocation(p_loadItemModel_1_);
        ModelNamespaced modelresourcelocation = new ModelNamespaced(resourcelocation, "inventory");

        if (Reflector.ModelLoader.exists())
        {
            try
            {
                Object object = Reflector.ModelLoader_VanillaLoader_INSTANCE.getValue();
                checkNull(object, "vanillaLoader is null");
                Object object1 = Reflector.call(object, Reflector.ModelLoader_VanillaLoader_loadModel, modelresourcelocation);
                checkNull(object1, "iModel is null");
                Map map = (Map)Reflector.getFieldValue(p_loadItemModel_0_, Reflector.ModelLoader_stateModels);
                checkNull(map, "stateModels is null");
                map.put(modelresourcelocation, object1);
                Set set = (Set)Reflector.ModelLoaderRegistry_textures.getValue();
                checkNull(set, "registryTextures is null");
                Collection collection = (Collection)Reflector.call(object1, Reflector.IModel_getTextures);
                checkNull(collection, "modelTextures is null");
                set.addAll(collection);
            }
            catch (Exception exception)
            {
                Config.warn("Error registering model: " + modelresourcelocation + ", " + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
        else
        {
            p_loadItemModel_0_.loadItemModel(resourcelocation.toString(), modelresourcelocation, resourcelocation);
        }
    }

    private static void checkNull(Object p_checkNull_0_, String p_checkNull_1_) throws NullPointerException
    {
        if (p_checkNull_0_ == null)
        {
            throw new NullPointerException(p_checkNull_1_);
        }
    }

    private static Namespaced getModelLocation(String p_getModelLocation_0_)
    {
        return Reflector.ModelLoader.exists() && !p_getModelLocation_0_.startsWith("mcpatcher/") && !p_getModelLocation_0_.startsWith("optifine/") ? new Namespaced("models/" + p_getModelLocation_0_) : new Namespaced(p_getModelLocation_0_);
    }
}

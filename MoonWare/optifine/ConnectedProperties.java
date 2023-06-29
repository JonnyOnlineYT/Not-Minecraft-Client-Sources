package optifine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Namespaced;
import net.minecraft.world.biome.Biome;

public class ConnectedProperties
{
    public String name;
    public String basePath;
    public MatchBlock[] matchBlocks;
    public int[] metadatas;
    public String[] matchTiles;
    public int method;
    public String[] tiles;
    public int connect;
    public int faces = 63;
    public Biome[] biomes;
    public int minHeight;
    public int maxHeight = 1024;
    public int renderPass;
    public boolean innerSeams;
    public int[] ctmTileIndexes;
    public int width;
    public int height;
    public int[] weights;
    public int symmetry = 1;
    public int[] sumWeights;
    public int sumAllWeights = 1;
    public TextureAtlasSprite[] matchTileIcons;
    public TextureAtlasSprite[] tileIcons;
    public MatchBlock[] connectBlocks;
    public String[] connectTiles;
    public TextureAtlasSprite[] connectTileIcons;
    public int tintIndex = -1;
    public IBlockState tintBlockState = Blocks.AIR.getDefaultState();
    public BlockRenderLayer layer;
    public static final int METHOD_NONE = 0;
    public static final int METHOD_CTM = 1;
    public static final int METHOD_HORIZONTAL = 2;
    public static final int METHOD_TOP = 3;
    public static final int METHOD_RANDOM = 4;
    public static final int METHOD_REPEAT = 5;
    public static final int METHOD_VERTICAL = 6;
    public static final int METHOD_FIXED = 7;
    public static final int METHOD_HORIZONTAL_VERTICAL = 8;
    public static final int METHOD_VERTICAL_HORIZONTAL = 9;
    public static final int METHOD_CTM_COMPACT = 10;
    public static final int METHOD_OVERLAY = 11;
    public static final int CONNECT_NONE = 0;
    public static final int CONNECT_BLOCK = 1;
    public static final int CONNECT_TILE = 2;
    public static final int CONNECT_MATERIAL = 3;
    public static final int CONNECT_UNKNOWN = 128;
    public static final int FACE_BOTTOM = 1;
    public static final int FACE_TOP = 2;
    public static final int FACE_NORTH = 4;
    public static final int FACE_SOUTH = 8;
    public static final int FACE_WEST = 16;
    public static final int FACE_EAST = 32;
    public static final int FACE_SIDES = 60;
    public static final int FACE_ALL = 63;
    public static final int FACE_UNKNOWN = 128;
    public static final int SYMMETRY_NONE = 1;
    public static final int SYMMETRY_OPPOSITE = 2;
    public static final int SYMMETRY_ALL = 6;
    public static final int SYMMETRY_UNKNOWN = 128;

    public ConnectedProperties(Properties p_i27_1_, String p_i27_2_)
    {
        ConnectedParser connectedparser = new ConnectedParser("ConnectedTextures");
        name = connectedparser.parseName(p_i27_2_);
        basePath = connectedparser.parseBasePath(p_i27_2_);
        matchBlocks = connectedparser.parseMatchBlocks(p_i27_1_.getProperty("matchBlocks"));
        metadatas = connectedparser.parseIntList(p_i27_1_.getProperty("metadata"));
        matchTiles = parseMatchTiles(p_i27_1_.getProperty("matchTiles"));
        method = parseMethod(p_i27_1_.getProperty("method"));
        tiles = parseTileNames(p_i27_1_.getProperty("tiles"));
        connect = parseConnect(p_i27_1_.getProperty("connect"));
        faces = parseFaces(p_i27_1_.getProperty("faces"));
        biomes = connectedparser.parseBiomes(p_i27_1_.getProperty("biomes"));
        minHeight = connectedparser.parseInt(p_i27_1_.getProperty("minHeight"), -1);
        maxHeight = connectedparser.parseInt(p_i27_1_.getProperty("maxHeight"), 1024);
        renderPass = connectedparser.parseInt(p_i27_1_.getProperty("renderPass"));
        innerSeams = ConnectedParser.parseBoolean(p_i27_1_.getProperty("innerSeams"));
        ctmTileIndexes = parseCtmTileIndexes(p_i27_1_);
        width = connectedparser.parseInt(p_i27_1_.getProperty("width"));
        height = connectedparser.parseInt(p_i27_1_.getProperty("height"));
        weights = connectedparser.parseIntList(p_i27_1_.getProperty("weights"));
        symmetry = parseSymmetry(p_i27_1_.getProperty("symmetry"));
        connectBlocks = connectedparser.parseMatchBlocks(p_i27_1_.getProperty("connectBlocks"));
        connectTiles = parseMatchTiles(p_i27_1_.getProperty("connectTiles"));
        tintIndex = connectedparser.parseInt(p_i27_1_.getProperty("tintIndex"));
        tintBlockState = connectedparser.parseBlockState(p_i27_1_.getProperty("tintBlock"), Blocks.AIR.getDefaultState());
        layer = connectedparser.parseBlockRenderLayer(p_i27_1_.getProperty("layer"), BlockRenderLayer.CUTOUT_MIPPED);
    }

    private int[] parseCtmTileIndexes(Properties p_parseCtmTileIndexes_1_)
    {
        if (tiles == null)
        {
            return null;
        }
        else
        {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();

            for (Object object : p_parseCtmTileIndexes_1_.keySet())
            {
                if (object instanceof String)
                {
                    String s = (String)object;
                    String s1 = "ctm.";

                    if (s.startsWith(s1))
                    {
                        String s2 = s.substring(s1.length());
                        String s3 = p_parseCtmTileIndexes_1_.getProperty(s);

                        if (s3 != null)
                        {
                            int i = Config.parseInt(s2, -1);

                            if (i >= 0 && i <= 46)
                            {
                                int j = Config.parseInt(s3, -1);

                                if (j >= 0 && j < tiles.length)
                                {
                                    map.put(Integer.valueOf(i), Integer.valueOf(j));
                                }
                                else
                                {
                                    Config.warn("Invalid CTM tile index: " + s3);
                                }
                            }
                            else
                            {
                                Config.warn("Invalid CTM index: " + s2);
                            }
                        }
                    }
                }
            }

            if (map.isEmpty())
            {
                return null;
            }
            else
            {
                int[] aint = new int[47];

                for (int k = 0; k < aint.length; ++k)
                {
                    aint[k] = -1;

                    if (map.containsKey(Integer.valueOf(k)))
                    {
                        aint[k] = map.get(Integer.valueOf(k)).intValue();
                    }
                }

                return aint;
            }
        }
    }

    private String[] parseMatchTiles(String p_parseMatchTiles_1_)
    {
        if (p_parseMatchTiles_1_ == null)
        {
            return null;
        }
        else
        {
            String[] astring = Config.tokenize(p_parseMatchTiles_1_, " ");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (s.endsWith(".png"))
                {
                    s = s.substring(0, s.length() - 4);
                }

                s = TextureUtils.fixResourcePath(s, basePath);
                astring[i] = s;
            }

            return astring;
        }
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

    private String[] parseTileNames(String p_parseTileNames_1_)
    {
        if (p_parseTileNames_1_ == null)
        {
            return null;
        }
        else
        {
            List list = new ArrayList();
            String[] astring = Config.tokenize(p_parseTileNames_1_, " ,");
            label65:

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (s.contains("-"))
                {
                    String[] astring1 = Config.tokenize(s, "-");

                    if (astring1.length == 2)
                    {
                        int j = Config.parseInt(astring1[0], -1);
                        int k = Config.parseInt(astring1[1], -1);

                        if (j >= 0 && k >= 0)
                        {
                            if (j > k)
                            {
                                Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseTileNames_1_);
                                continue;
                            }

                            int l = j;

                            while (true)
                            {
                                if (l > k)
                                {
                                    continue label65;
                                }

                                list.add(String.valueOf(l));
                                ++l;
                            }
                        }
                    }
                }

                list.add(s);
            }

            String[] astring2 = (String[])list.toArray(new String[list.size()]);

            for (int i1 = 0; i1 < astring2.length; ++i1)
            {
                String s1 = astring2[i1];
                s1 = TextureUtils.fixResourcePath(s1, basePath);

                if (!s1.startsWith(basePath) && !s1.startsWith("textures/") && !s1.startsWith("mcpatcher/"))
                {
                    s1 = basePath + "/" + s1;
                }

                if (s1.endsWith(".png"))
                {
                    s1 = s1.substring(0, s1.length() - 4);
                }

                String s2 = "textures/blocks/";

                if (s1.startsWith(s2))
                {
                    s1 = s1.substring(s2.length());
                }

                if (s1.startsWith("/"))
                {
                    s1 = s1.substring(1);
                }

                astring2[i1] = s1;
            }

            return astring2;
        }
    }

    private static int parseSymmetry(String p_parseSymmetry_0_)
    {
        if (p_parseSymmetry_0_ == null)
        {
            return 1;
        }
        else if (p_parseSymmetry_0_.equals("opposite"))
        {
            return 2;
        }
        else if (p_parseSymmetry_0_.equals("all"))
        {
            return 6;
        }
        else
        {
            Config.warn("Unknown symmetry: " + p_parseSymmetry_0_);
            return 1;
        }
    }

    private static int parseFaces(String p_parseFaces_0_)
    {
        if (p_parseFaces_0_ == null)
        {
            return 63;
        }
        else
        {
            String[] astring = Config.tokenize(p_parseFaces_0_, " ,");
            int i = 0;

            for (int j = 0; j < astring.length; ++j)
            {
                String s = astring[j];
                int k = parseFace(s);
                i |= k;
            }

            return i;
        }
    }

    private static int parseFace(String p_parseFace_0_)
    {
        p_parseFace_0_ = p_parseFace_0_.toLowerCase();

        if (!p_parseFace_0_.equals("bottom") && !p_parseFace_0_.equals("down"))
        {
            if (!p_parseFace_0_.equals("top") && !p_parseFace_0_.equals("up"))
            {
                if (p_parseFace_0_.equals("north"))
                {
                    return 4;
                }
                else if (p_parseFace_0_.equals("south"))
                {
                    return 8;
                }
                else if (p_parseFace_0_.equals("east"))
                {
                    return 32;
                }
                else if (p_parseFace_0_.equals("west"))
                {
                    return 16;
                }
                else if (p_parseFace_0_.equals("sides"))
                {
                    return 60;
                }
                else if (p_parseFace_0_.equals("all"))
                {
                    return 63;
                }
                else
                {
                    Config.warn("Unknown face: " + p_parseFace_0_);
                    return 128;
                }
            }
            else
            {
                return 2;
            }
        }
        else
        {
            return 1;
        }
    }

    private static int parseConnect(String p_parseConnect_0_)
    {
        if (p_parseConnect_0_ == null)
        {
            return 0;
        }
        else if (p_parseConnect_0_.equals("block"))
        {
            return 1;
        }
        else if (p_parseConnect_0_.equals("tile"))
        {
            return 2;
        }
        else if (p_parseConnect_0_.equals("material"))
        {
            return 3;
        }
        else
        {
            Config.warn("Unknown connect: " + p_parseConnect_0_);
            return 128;
        }
    }

    public static IProperty getProperty(String p_getProperty_0_, Collection p_getProperty_1_)
    {
        for (Object iproperty : p_getProperty_1_)
        {
            if (p_getProperty_0_.equals(((IProperty) iproperty).getName()))
            {
                return (IProperty) iproperty;
            }
        }

        return null;
    }

    private static int parseMethod(String p_parseMethod_0_)
    {
        if (p_parseMethod_0_ == null)
        {
            return 1;
        }
        else if (!p_parseMethod_0_.equals("ctm") && !p_parseMethod_0_.equals("glass"))
        {
            if (p_parseMethod_0_.equals("ctm_compact"))
            {
                return 10;
            }
            else if (!p_parseMethod_0_.equals("horizontal") && !p_parseMethod_0_.equals("bookshelf"))
            {
                if (p_parseMethod_0_.equals("vertical"))
                {
                    return 6;
                }
                else if (p_parseMethod_0_.equals("top"))
                {
                    return 3;
                }
                else if (p_parseMethod_0_.equals("random"))
                {
                    return 4;
                }
                else if (p_parseMethod_0_.equals("repeat"))
                {
                    return 5;
                }
                else if (p_parseMethod_0_.equals("fixed"))
                {
                    return 7;
                }
                else if (!p_parseMethod_0_.equals("horizontal+vertical") && !p_parseMethod_0_.equals("h+v"))
                {
                    if (!p_parseMethod_0_.equals("vertical+horizontal") && !p_parseMethod_0_.equals("v+h"))
                    {
                        if (p_parseMethod_0_.equals("overlay"))
                        {
                            return 11;
                        }
                        else
                        {
                            Config.warn("Unknown method: " + p_parseMethod_0_);
                            return 0;
                        }
                    }
                    else
                    {
                        return 9;
                    }
                }
                else
                {
                    return 8;
                }
            }
            else
            {
                return 2;
            }
        }
        else
        {
            return 1;
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
            else
            {
                if (matchBlocks == null)
                {
                    matchBlocks = detectMatchBlocks();
                }

                if (matchTiles == null && matchBlocks == null)
                {
                    matchTiles = detectMatchTiles();
                }

                if (matchBlocks == null && matchTiles == null)
                {
                    Config.warn("No matchBlocks or matchTiles specified: " + p_isValid_1_);
                    return false;
                }
                else if (method == 0)
                {
                    Config.warn("No method: " + p_isValid_1_);
                    return false;
                }
                else if (tiles != null && tiles.length > 0)
                {
                    if (connect == 0)
                    {
                        connect = detectConnect();
                    }

                    if (connect == 128)
                    {
                        Config.warn("Invalid connect in: " + p_isValid_1_);
                        return false;
                    }
                    else if (renderPass > 0)
                    {
                        Config.warn("Render pass not supported: " + renderPass);
                        return false;
                    }
                    else if ((faces & 128) != 0)
                    {
                        Config.warn("Invalid faces in: " + p_isValid_1_);
                        return false;
                    }
                    else if ((symmetry & 128) != 0)
                    {
                        Config.warn("Invalid symmetry in: " + p_isValid_1_);
                        return false;
                    }
                    else
                    {
                        switch (method)
                        {
                            case 1:
                                return isValidCtm(p_isValid_1_);

                            case 2:
                                return isValidHorizontal(p_isValid_1_);

                            case 3:
                                return isValidTop(p_isValid_1_);

                            case 4:
                                return isValidRandom(p_isValid_1_);

                            case 5:
                                return isValidRepeat(p_isValid_1_);

                            case 6:
                                return isValidVertical(p_isValid_1_);

                            case 7:
                                return isValidFixed(p_isValid_1_);

                            case 8:
                                return isValidHorizontalVertical(p_isValid_1_);

                            case 9:
                                return isValidVerticalHorizontal(p_isValid_1_);

                            case 10:
                                return isValidCtmCompact(p_isValid_1_);

                            case 11:
                                return isValidOverlay(p_isValid_1_);

                            default:
                                Config.warn("Unknown method: " + p_isValid_1_);
                                return false;
                        }
                    }
                }
                else
                {
                    Config.warn("No tiles specified: " + p_isValid_1_);
                    return false;
                }
            }
        }
        else
        {
            Config.warn("No name found: " + p_isValid_1_);
            return false;
        }
    }

    private int detectConnect()
    {
        if (matchBlocks != null)
        {
            return 1;
        }
        else
        {
            return matchTiles != null ? 2 : 128;
        }
    }

    private MatchBlock[] detectMatchBlocks()
    {
        int[] aint = detectMatchBlockIds();

        if (aint == null)
        {
            return null;
        }
        else
        {
            MatchBlock[] amatchblock = new MatchBlock[aint.length];

            for (int i = 0; i < amatchblock.length; ++i)
            {
                amatchblock[i] = new MatchBlock(aint[i]);
            }

            return amatchblock;
        }
    }

    private int[] detectMatchBlockIds()
    {
        if (!name.startsWith("block"))
        {
            return null;
        }
        else
        {
            int i = "block".length();
            int j;

            for (j = i; j < name.length(); ++j)
            {
                char c0 = name.charAt(j);

                if (c0 < '0' || c0 > '9')
                {
                    break;
                }
            }

            if (j == i)
            {
                return null;
            }
            else
            {
                String s = name.substring(i, j);
                int k = Config.parseInt(s, -1);
                return k < 0 ? null : new int[] {k};
            }
        }
    }

    private String[] detectMatchTiles()
    {
        TextureAtlasSprite textureatlassprite = getIcon(name);
        return textureatlassprite == null ? null : new String[] {name};
    }

    private static TextureAtlasSprite getIcon(String p_getIcon_0_)
    {
        TextureMap texturemap = Minecraft.getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(p_getIcon_0_);

        if (textureatlassprite != null)
        {
            return textureatlassprite;
        }
        else
        {
            textureatlassprite = texturemap.getSpriteSafe("blocks/" + p_getIcon_0_);
            return textureatlassprite;
        }
    }

    private boolean isValidCtm(String p_isValidCtm_1_)
    {
        if (tiles == null)
        {
            tiles = parseTileNames("0-11 16-27 32-43 48-58");
        }

        if (tiles.length < 47)
        {
            Config.warn("Invalid tiles, must be at least 47: " + p_isValidCtm_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidCtmCompact(String p_isValidCtmCompact_1_)
    {
        if (tiles == null)
        {
            tiles = parseTileNames("0-4");
        }

        if (tiles.length < 5)
        {
            Config.warn("Invalid tiles, must be at least 5: " + p_isValidCtmCompact_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidOverlay(String p_isValidOverlay_1_)
    {
        if (tiles == null)
        {
            tiles = parseTileNames("0-16");
        }

        if (tiles.length < 17)
        {
            Config.warn("Invalid tiles, must be at least 17: " + p_isValidOverlay_1_);
            return false;
        }
        else if (layer != null && layer != BlockRenderLayer.SOLID)
        {
            return true;
        }
        else
        {
            Config.warn("Invalid overlay layer: " + layer);
            return false;
        }
    }

    private boolean isValidHorizontal(String p_isValidHorizontal_1_)
    {
        if (tiles == null)
        {
            tiles = parseTileNames("12-15");
        }

        if (tiles.length != 4)
        {
            Config.warn("Invalid tiles, must be exactly 4: " + p_isValidHorizontal_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidVertical(String p_isValidVertical_1_)
    {
        if (tiles == null)
        {
            Config.warn("No tiles defined for vertical: " + p_isValidVertical_1_);
            return false;
        }
        else if (tiles.length != 4)
        {
            Config.warn("Invalid tiles, must be exactly 4: " + p_isValidVertical_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidHorizontalVertical(String p_isValidHorizontalVertical_1_)
    {
        if (tiles == null)
        {
            Config.warn("No tiles defined for horizontal+vertical: " + p_isValidHorizontalVertical_1_);
            return false;
        }
        else if (tiles.length != 7)
        {
            Config.warn("Invalid tiles, must be exactly 7: " + p_isValidHorizontalVertical_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidVerticalHorizontal(String p_isValidVerticalHorizontal_1_)
    {
        if (tiles == null)
        {
            Config.warn("No tiles defined for vertical+horizontal: " + p_isValidVerticalHorizontal_1_);
            return false;
        }
        else if (tiles.length != 7)
        {
            Config.warn("Invalid tiles, must be exactly 7: " + p_isValidVerticalHorizontal_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidRandom(String p_isValidRandom_1_)
    {
        if (tiles != null && tiles.length > 0)
        {
            if (weights != null)
            {
                if (weights.length > tiles.length)
                {
                    Config.warn("More weights defined than tiles, trimming weights: " + p_isValidRandom_1_);
                    int[] aint = new int[tiles.length];
                    System.arraycopy(weights, 0, aint, 0, aint.length);
                    weights = aint;
                }

                if (weights.length < tiles.length)
                {
                    Config.warn("Less weights defined than tiles, expanding weights: " + p_isValidRandom_1_);
                    int[] aint1 = new int[tiles.length];
                    System.arraycopy(weights, 0, aint1, 0, weights.length);
                    int i = MathUtils.getAverage(weights);

                    for (int j = weights.length; j < aint1.length; ++j)
                    {
                        aint1[j] = i;
                    }

                    weights = aint1;
                }

                sumWeights = new int[weights.length];
                int k = 0;

                for (int l = 0; l < weights.length; ++l)
                {
                    k += weights[l];
                    sumWeights[l] = k;
                }

                sumAllWeights = k;

                if (sumAllWeights <= 0)
                {
                    Config.warn("Invalid sum of all weights: " + k);
                    sumAllWeights = 1;
                }
            }

            return true;
        }
        else
        {
            Config.warn("Tiles not defined: " + p_isValidRandom_1_);
            return false;
        }
    }

    private boolean isValidRepeat(String p_isValidRepeat_1_)
    {
        if (tiles == null)
        {
            Config.warn("Tiles not defined: " + p_isValidRepeat_1_);
            return false;
        }
        else if (width > 0 && width <= 16)
        {
            if (height > 0 && height <= 16)
            {
                if (tiles.length != width * height)
                {
                    Config.warn("Number of tiles does not equal width x height: " + p_isValidRepeat_1_);
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                Config.warn("Invalid height: " + p_isValidRepeat_1_);
                return false;
            }
        }
        else
        {
            Config.warn("Invalid width: " + p_isValidRepeat_1_);
            return false;
        }
    }

    private boolean isValidFixed(String p_isValidFixed_1_)
    {
        if (tiles == null)
        {
            Config.warn("Tiles not defined: " + p_isValidFixed_1_);
            return false;
        }
        else if (tiles.length != 1)
        {
            Config.warn("Number of tiles should be 1 for method: fixed.");
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidTop(String p_isValidTop_1_)
    {
        if (tiles == null)
        {
            tiles = parseTileNames("66");
        }

        if (tiles.length != 1)
        {
            Config.warn("Invalid tiles, must be exactly 1: " + p_isValidTop_1_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void updateIcons(TextureMap p_updateIcons_1_)
    {
        if (matchTiles != null)
        {
            matchTileIcons = registerIcons(matchTiles, p_updateIcons_1_);
        }

        if (connectTiles != null)
        {
            connectTileIcons = registerIcons(connectTiles, p_updateIcons_1_);
        }

        if (tiles != null)
        {
            tileIcons = registerIcons(tiles, p_updateIcons_1_);
        }
    }

    private static TextureAtlasSprite[] registerIcons(String[] p_registerIcons_0_, TextureMap p_registerIcons_1_)
    {
        if (p_registerIcons_0_ == null)
        {
            return null;
        }
        else
        {
            List list = new ArrayList();

            for (int i = 0; i < p_registerIcons_0_.length; ++i)
            {
                String s = p_registerIcons_0_[i];
                Namespaced resourcelocation = new Namespaced(s);
                String s1 = resourcelocation.getNamespace();
                String s2 = resourcelocation.getPath();

                if (!s2.contains("/"))
                {
                    s2 = "textures/blocks/" + s2;
                }

                String s3 = s2 + ".png";
                Namespaced resourcelocation1 = new Namespaced(s1, s3);
                boolean flag = Config.hasResource(resourcelocation1);

                if (!flag)
                {
                    Config.warn("File not found: " + s3);
                }

                String s4 = "textures/";
                String s5 = s2;

                if (s2.startsWith(s4))
                {
                    s5 = s2.substring(s4.length());
                }

                Namespaced resourcelocation2 = new Namespaced(s1, s5);
                TextureAtlasSprite textureatlassprite = p_registerIcons_1_.registerSprite(resourcelocation2);
                list.add(textureatlassprite);
            }

            TextureAtlasSprite[] atextureatlassprite = (TextureAtlasSprite[])list.toArray(new TextureAtlasSprite[list.size()]);
            return atextureatlassprite;
        }
    }

    public boolean matchesBlockId(int p_matchesBlockId_1_)
    {
        return Matches.blockId(p_matchesBlockId_1_, matchBlocks);
    }

    public boolean matchesBlock(int p_matchesBlock_1_, int p_matchesBlock_2_)
    {
        if (!Matches.block(p_matchesBlock_1_, p_matchesBlock_2_, matchBlocks))
        {
            return false;
        }
        else
        {
            return Matches.metadata(p_matchesBlock_2_, metadatas);
        }
    }

    public boolean matchesIcon(TextureAtlasSprite p_matchesIcon_1_)
    {
        return Matches.sprite(p_matchesIcon_1_, matchTileIcons);
    }

    public String toString()
    {
        return "CTM name: " + name + ", basePath: " + basePath + ", matchBlocks: " + Config.arrayToString(matchBlocks) + ", matchTiles: " + Config.arrayToString(matchTiles);
    }

    public boolean matchesBiome(Biome p_matchesBiome_1_)
    {
        return Matches.biome(p_matchesBiome_1_, biomes);
    }

    public int getMetadataMax()
    {
        int i = -1;
        i = getMax(metadatas, i);

        if (matchBlocks != null)
        {
            for (int j = 0; j < matchBlocks.length; ++j)
            {
                MatchBlock matchblock = matchBlocks[j];
                i = getMax(matchblock.getMetadatas(), i);
            }
        }

        return i;
    }

    private int getMax(int[] p_getMax_1_, int p_getMax_2_)
    {
        if (p_getMax_1_ == null)
        {
            return p_getMax_2_;
        }
        else
        {
            for (int i = 0; i < p_getMax_1_.length; ++i)
            {
                int j = p_getMax_1_[i];

                if (j > p_getMax_2_)
                {
                    p_getMax_2_ = j;
                }
            }

            return p_getMax_2_;
        }
    }
}

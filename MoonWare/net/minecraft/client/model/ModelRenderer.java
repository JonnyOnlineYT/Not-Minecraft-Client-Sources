package net.minecraft.client.model;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.*;
import net.minecraft.util.Namespaced;
import net.optifine.entity.model.anim.ModelUpdater;
import optifine.Config;
import optifine.ModelSprite;

import java.util.ArrayList;
import java.util.List;

public class ModelRenderer
{
    /** The size of the texture file's width in pixels. */
    public float textureWidth;

    /** The size of the texture file's height in pixels. */
    public float textureHeight;

    /** The X offset into the texture used for displaying this model */
    private int textureOffsetX;

    /** The Y offset into the texture used for displaying this model */
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled;

    /** The GL display list rendered by the Tessellator for this model */
    private int displayList;
    public boolean mirror;
    public boolean showModel;

    /** Hides the model. */
    public boolean isHidden;
    public List<ModelBox> cubeList;
    public List<ModelRenderer> childModels;
    public final String boxName;
    private final ModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public List spriteList;
    public boolean mirrorV;
    public float scaleX;
    public float scaleY;
    public float scaleZ;
    private float savedScale;
    private Namespaced textureLocation;
    private String id;
    private ModelUpdater modelUpdater;
    private RenderGlobal renderGlobal;

    public ModelRenderer(ModelBase model, String boxNameIn)
    {
        spriteList = new ArrayList();
        mirrorV = false;
        scaleX = 1.0F;
        scaleY = 1.0F;
        scaleZ = 1.0F;
        textureLocation = null;
        id = null;
        renderGlobal = Config.getRenderGlobal();
        textureWidth = 64.0F;
        textureHeight = 32.0F;
        showModel = true;
        cubeList = Lists.newArrayList();
        baseModel = model;
        model.boxList.add(this);
        boxName = boxNameIn;
        setTextureSize(model.textureWidth, model.textureHeight);
    }

    public ModelRenderer(ModelBase model)
    {
        this(model, null);
    }

    public ModelRenderer(ModelBase model, int texOffX, int texOffY)
    {
        this(model);
        setTextureOffset(texOffX, texOffY);
    }

    /**
     * Sets the current box's rotation points and rotation angles to another box.
     */
    public void addChild(ModelRenderer renderer)
    {
        if (childModels == null)
        {
            childModels = Lists.newArrayList();
        }

        childModels.add(renderer);
    }

    public ModelRenderer setTextureOffset(int x, int y)
    {
        textureOffsetX = x;
        textureOffsetY = y;
        return this;
    }

    public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth)
    {
        partName = boxName + "." + partName;
        TextureOffset textureoffset = baseModel.getTextureOffset(partName);
        setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
        cubeList.add((new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F)).setBoxName(partName));
        return this;
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth)
    {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
        return this;
    }

    public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored)
    {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F, mirrored));
        return this;
    }

    /**
     * Creates a textured box.
     */
    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
    {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, width, height, depth, scaleFactor));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn)
    {
        rotationPointX = rotationPointXIn;
        rotationPointY = rotationPointYIn;
        rotationPointZ = rotationPointZIn;
    }

    public ModelRenderer addCube(float offX, float offY, float offZ, float width, float height, float depth, float delta, boolean mirrored) {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, (int)width, (int)height, (int)depth, delta, mirrored));
        return this;
    }

    public ModelRenderer addCube(float offX, float offY, float offZ, float width, float height, float depth, float delta) {
        cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, offX, offY, offZ, (int)width, (int)height, (int)depth, delta));
        return this;
    }
    public void render(float scale)
    {
        if (!isHidden && showModel)
        {
            if (!compiled)
            {
                compileDisplayList(scale);
            }

            int i = 0;

            if (textureLocation != null && !renderGlobal.renderOverlayDamaged)
            {
                if (renderGlobal.renderOverlayEyes)
                {
                    return;
                }

                i = GlStateManager.getBoundTexture();
                Config.getTextureManager().bindTexture(textureLocation);
            }

            if (modelUpdater != null)
            {
                modelUpdater.update();
            }

            boolean flag = scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F;
            GlStateManager.translate(offsetX, offsetY, offsetZ);

            if (rotateAngleX == 0.0F && rotateAngleY == 0.0F && rotateAngleZ == 0.0F)
            {
                if (rotationPointX == 0.0F && rotationPointY == 0.0F && rotationPointZ == 0.0F)
                {
                    if (flag)
                    {
                        GlStateManager.scale(scaleX, scaleY, scaleZ);
                    }

                    GlStateManager.callList(displayList);

                    if (childModels != null)
                    {
                        for (int l = 0; l < childModels.size(); ++l)
                        {
                            childModels.get(l).render(scale);
                        }
                    }

                    if (flag)
                    {
                        GlStateManager.scale(1.0F / scaleX, 1.0F / scaleY, 1.0F / scaleZ);
                    }
                }
                else
                {
                    GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

                    if (flag)
                    {
                        GlStateManager.scale(scaleX, scaleY, scaleZ);
                    }

                    GlStateManager.callList(displayList);

                    if (childModels != null)
                    {
                        for (int k = 0; k < childModels.size(); ++k)
                        {
                            childModels.get(k).render(scale);
                        }
                    }

                    if (flag)
                    {
                        GlStateManager.scale(1.0F / scaleX, 1.0F / scaleY, 1.0F / scaleZ);
                    }

                    GlStateManager.translate(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale);
                }
            }
            else
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

                if (rotateAngleZ != 0.0F)
                {
                    GlStateManager.rotate(rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }

                if (rotateAngleY != 0.0F)
                {
                    GlStateManager.rotate(rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }

                if (rotateAngleX != 0.0F)
                {
                    GlStateManager.rotate(rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }

                if (flag)
                {
                    GlStateManager.scale(scaleX, scaleY, scaleZ);
                }

                GlStateManager.callList(displayList);

                if (childModels != null)
                {
                    for (int j = 0; j < childModels.size(); ++j)
                    {
                        childModels.get(j).render(scale);
                    }
                }

                GlStateManager.popMatrix();
            }

            GlStateManager.translate(-offsetX, -offsetY, -offsetZ);

            if (i != 0)
            {
                GlStateManager.bindTexture(i);
            }
        }
    }

    public void renderWithRotation(float scale)
    {
        if (!isHidden && showModel)
        {
            if (!compiled)
            {
                compileDisplayList(scale);
            }

            int i = 0;

            if (textureLocation != null && !renderGlobal.renderOverlayDamaged)
            {
                if (renderGlobal.renderOverlayEyes)
                {
                    return;
                }

                i = GlStateManager.getBoundTexture();
                Config.getTextureManager().bindTexture(textureLocation);
            }

            if (modelUpdater != null)
            {
                modelUpdater.update();
            }

            boolean flag = scaleX != 1.0F || scaleY != 1.0F || scaleZ != 1.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

            if (rotateAngleY != 0.0F)
            {
                GlStateManager.rotate(rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (rotateAngleX != 0.0F)
            {
                GlStateManager.rotate(rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            }

            if (rotateAngleZ != 0.0F)
            {
                GlStateManager.rotate(rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
            }

            if (flag)
            {
                GlStateManager.scale(scaleX, scaleY, scaleZ);
            }

            GlStateManager.callList(displayList);

            if (childModels != null)
            {
                for (int j = 0; j < childModels.size(); ++j)
                {
                    childModels.get(j).render(scale);
                }
            }

            GlStateManager.popMatrix();

            if (i != 0)
            {
                GlStateManager.bindTexture(i);
            }
        }
    }

    /**
     * Allows the changing of Angles after a box has been rendered
     */
    public void postRender(float scale)
    {
        if (!isHidden && showModel)
        {
            if (!compiled)
            {
                compileDisplayList(scale);
            }

            if (rotateAngleX == 0.0F && rotateAngleY == 0.0F && rotateAngleZ == 0.0F)
            {
                if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F)
                {
                    GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
                }
            }
            else
            {
                GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

                if (rotateAngleZ != 0.0F)
                {
                    GlStateManager.rotate(rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }

                if (rotateAngleY != 0.0F)
                {
                    GlStateManager.rotate(rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }

                if (rotateAngleX != 0.0F)
                {
                    GlStateManager.rotate(rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }
            }
        }
    }

    /**
     * Compiles a GL display list for this model
     */
    private void compileDisplayList(float scale)
    {
        if (displayList == 0)
        {
            savedScale = scale;
            displayList = GLAllocation.generateDisplayLists(1);
        }

        GlStateManager.glNewList(displayList, 4864);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

        for (int i = 0; i < cubeList.size(); ++i)
        {
            cubeList.get(i).render(bufferbuilder, scale);
        }

        for (int j = 0; j < spriteList.size(); ++j)
        {
            ModelSprite modelsprite = (ModelSprite) spriteList.get(j);
            modelsprite.render(Tessellator.getInstance(), scale);
        }

        GlStateManager.glEndList();
        compiled = true;
    }

    /**
     * Returns the model renderer with the new texture parameters.
     */
    public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn)
    {
        textureWidth = (float)textureWidthIn;
        textureHeight = (float)textureHeightIn;
        return this;
    }

    public void addSprite(float p_addSprite_1_, float p_addSprite_2_, float p_addSprite_3_, int p_addSprite_4_, int p_addSprite_5_, int p_addSprite_6_, float p_addSprite_7_)
    {
        spriteList.add(new ModelSprite(this, textureOffsetX, textureOffsetY, p_addSprite_1_, p_addSprite_2_, p_addSprite_3_, p_addSprite_4_, p_addSprite_5_, p_addSprite_6_, p_addSprite_7_));
    }

    public boolean getCompiled()
    {
        return compiled;
    }

    public int getDisplayList()
    {
        return displayList;
    }

    public void resetDisplayList()
    {
        if (compiled)
        {
            compiled = false;
            compileDisplayList(savedScale);
        }
    }

    public Namespaced getTextureLocation()
    {
        return textureLocation;
    }

    public void setTextureLocation(Namespaced p_setTextureLocation_1_)
    {
        textureLocation = p_setTextureLocation_1_;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String p_setId_1_)
    {
        id = p_setId_1_;
    }

    public void addBox(int[][] p_addBox_1_, float p_addBox_2_, float p_addBox_3_, float p_addBox_4_, float p_addBox_5_, float p_addBox_6_, float p_addBox_7_, float p_addBox_8_)
    {
        cubeList.add(new ModelBox(this, p_addBox_1_, p_addBox_2_, p_addBox_3_, p_addBox_4_, p_addBox_5_, p_addBox_6_, p_addBox_7_, p_addBox_8_, mirror));
    }

    public ModelRenderer getChild(String p_getChild_1_)
    {
        if (p_getChild_1_ == null)
        {
            return null;
        }
        else
        {
            if (childModels != null)
            {
                for (int i = 0; i < childModels.size(); ++i)
                {
                    ModelRenderer modelrenderer = childModels.get(i);

                    if (p_getChild_1_.equals(modelrenderer.getId()))
                    {
                        return modelrenderer;
                    }
                }
            }

            return null;
        }
    }

    public ModelRenderer getChildDeep(String p_getChildDeep_1_)
    {
        if (p_getChildDeep_1_ == null)
        {
            return null;
        }
        else
        {
            ModelRenderer modelrenderer = getChild(p_getChildDeep_1_);

            if (modelrenderer != null)
            {
                return modelrenderer;
            }
            else
            {
                if (childModels != null)
                {
                    for (int i = 0; i < childModels.size(); ++i)
                    {
                        ModelRenderer modelrenderer1 = childModels.get(i);
                        ModelRenderer modelrenderer2 = modelrenderer1.getChildDeep(p_getChildDeep_1_);

                        if (modelrenderer2 != null)
                        {
                            return modelrenderer2;
                        }
                    }
                }

                return null;
            }
        }
    }

    public void setModelUpdater(ModelUpdater p_setModelUpdater_1_)
    {
        modelUpdater = p_setModelUpdater_1_;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("id: " + id + ", boxes: " + (cubeList != null ? cubeList.size() : null) + ", submodels: " + (childModels != null ? childModels.size() : null));
        return stringbuffer.toString();
    }
}

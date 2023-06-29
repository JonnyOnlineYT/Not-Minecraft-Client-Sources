package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Component;

public class ChatLine
{
    /** GUI Update Counter value this Line was created at */
    private final int updateCounterCreated;
    private final Component lineString;
    private float posX;
    private float posY;

    /**
     * int value to refer to existing Chat Lines, can be 0 which means unreferrable
     */
    private final int chatLineID;

    public ChatLine(int p_i45000_1_, Component p_i45000_2_, int p_i45000_3_)
    {
        lineString = p_i45000_2_;
        updateCounterCreated = p_i45000_1_;
        chatLineID = p_i45000_3_;
        posX = -Minecraft.font.getStringWidth(p_i45000_2_.asString());
        posY = -Minecraft.font.getStringHeight(p_i45000_2_.asString());
    }

    public Component getChatComponent() {
        return lineString;
    }

    public int getUpdatedCounter() {
        return updateCounterCreated;
    }

    public int getChatLineID() {
        return chatLineID;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

}

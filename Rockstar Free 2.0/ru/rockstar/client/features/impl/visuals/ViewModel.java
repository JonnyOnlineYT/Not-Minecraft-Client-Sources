package ru.rockstar.client.features.impl.visuals;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventTransformSideFirstPerson;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ViewModel extends Feature
{
    public NumberSetting mainX;
    public NumberSetting mainY;
    public NumberSetting mainZ;
    public NumberSetting offX;
    public NumberSetting offY;
    public NumberSetting offZ;
    public NumberSetting mainAngel;
    public NumberSetting mainRx;
    public NumberSetting mainRy;
    public NumberSetting mainRz;
    public NumberSetting offAngle;
    public NumberSetting offRx;
    public NumberSetting offRy;
    public NumberSetting offRz;
    public static NumberSetting mainScaleX;
    public static NumberSetting mainScaleY;
    public static NumberSetting mainScaleZ;
    public static NumberSetting offScaleX;
    public static NumberSetting offScaleY;
    public static NumberSetting offScaleZ;
    
    public ViewModel() {
        super("ViewModel", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043f\u043e\u0437\u0438\u0446\u0438\u044e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u0432 \u0440\u0443\u043a\u0435", 0, Category.VISUALS);
        this.mainX = new NumberSetting("MainX", 1.2f, 0.0f, 6.0f, 0.1f, () -> true);
        this.mainY = new NumberSetting("MainY", -0.63f, -3.0f, 3.0f, 0.1f, () -> true);
        this.mainZ = new NumberSetting("MainZ", -2.17f, -5.0f, 5.0f, 0.1f, () -> true);
        this.offX = new NumberSetting("OffX", -1.2f, -6.0f, 0.0f, 0.1f, () -> true);
        this.offY = new NumberSetting("OffY", -0.75f, -3.0f, 3.0f, 0.1f, () -> true);
        this.offZ = new NumberSetting("OffZ", -2.67f, -5.0f, 5.0f, 0.1f, () -> true);
        this.mainAngel = new NumberSetting("MainAngle", 0.0f, 0.0f, 360.0f, 0.1f, () -> true);
        this.mainRx = new NumberSetting("MainRotation", 0.0f, -1.0f, 1.0f, 0.1f, () -> true);
        this.mainRy = new NumberSetting("MainRotationY", 0.0f, -1.0f, 1.0f, 0.1f, () -> true);
        this.mainRz = new NumberSetting("MainRotationZ", 0.0f, -1.0f, 1.0f, 0.1f, () -> true);
        this.offAngle = new NumberSetting("OffAngle", 0.0f, 0.0f, 360.0f, 0.1f, () -> true);
        this.offRx = new NumberSetting("OffRotationX", 0.0f, -1.0f, 1.0f, 0.1f, () -> true);
        this.offRy = new NumberSetting("OffRotationY", 0.0f, -1.0f, 1.0f, 0.1f, () -> true);
        this.offRz = new NumberSetting("OffRotationZ", 0.0f, -1.0f, 1.0f, 0.1f, () -> true);
        this.addSettings(this.mainX, this.mainY, this.mainZ, this.offX, this.offY, this.offZ, this.mainAngel, this.mainRx, this.mainRy, this.mainRz, this.offAngle, this.offRx, this.offRy, this.offRz, ViewModel.mainScaleX, ViewModel.mainScaleY, ViewModel.mainScaleZ, ViewModel.offScaleX, ViewModel.offScaleY, ViewModel.offScaleZ);
    }
    
    @EventTarget
    public void onTransformSideFirstPerson(final EventTransformSideFirstPerson event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(this.mainX.getNumberValue(), this.mainY.getNumberValue(), this.mainZ.getNumberValue());
            GlStateManager.scale(ViewModel.mainScaleX.getNumberValue(), ViewModel.mainScaleY.getNumberValue(), ViewModel.mainScaleZ.getNumberValue());
            GlStateManager.rotate(this.mainAngel.getNumberValue(), this.mainRx.getNumberValue(), this.mainRy.getNumberValue(), this.mainRz.getNumberValue());
        }
        else if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(this.offX.getNumberValue(), this.offY.getNumberValue(), this.offZ.getNumberValue());
            GlStateManager.scale(ViewModel.offScaleX.getNumberValue(), ViewModel.offScaleY.getNumberValue(), ViewModel.offScaleZ.getNumberValue());
            GlStateManager.rotate(this.offAngle.getNumberValue(), this.offRx.getNumberValue(), this.offRy.getNumberValue(), this.offRz.getNumberValue());
        }
    }
    
    static {
        ViewModel.mainScaleX = new NumberSetting("MainScaleX", 1.0f, -5.0f, 10.0f, 0.1f, () -> true);
        ViewModel.mainScaleY = new NumberSetting("MainScaleY", 1.0f, -5.0f, 10.0f, 0.1f, () -> true);
        ViewModel.mainScaleZ = new NumberSetting("MainScaleZ", 1.0f, -5.0f, 10.0f, 0.1f, () -> true);
        ViewModel.offScaleX = new NumberSetting("OffScaleX", 1.0f, -5.0f, 10.0f, 0.1f, () -> true);
        ViewModel.offScaleY = new NumberSetting("OffScaleY", 1.0f, -5.0f, 10.0f, 0.1f, () -> true);
        ViewModel.offScaleZ = new NumberSetting("OffScaleZ", 1.0f, -5.0f, 10.0f, 0.1f, () -> true);
    }
}
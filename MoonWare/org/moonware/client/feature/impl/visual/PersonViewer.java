package org.moonware.client.feature.impl.visual;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.NumberSetting;

public class PersonViewer extends Feature {

    public static NumberSetting viewerYaw;
    public static NumberSetting fovModifier;
    public static NumberSetting viewerPitch;

    public PersonViewer() {
        super("PersonViewer", "Повозяляет изменять положение камеры второго и третьего лица", Type.Visuals);
        fovModifier = new NumberSetting("FOV Modifier", 4, 1, 50, 1, () -> true);
        viewerYaw = new NumberSetting("Viewer Yaw", 10, -50, 50, 5, () -> true);
        viewerPitch = new NumberSetting("Viewer Pitch", 10, -50, 50, 5, () -> true);
        addSettings(fovModifier, viewerYaw, viewerPitch);
    }
}

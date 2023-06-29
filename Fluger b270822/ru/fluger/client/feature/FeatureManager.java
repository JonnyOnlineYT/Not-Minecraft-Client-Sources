package ru.fluger.client.feature;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import ru.fluger.client.feature.Feature;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.impl.hud.ArrayList;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.feature.impl.hud.NoOverlay;
import ru.fluger.client.feature.impl.hud.Notifications;
import ru.fluger.client.feature.impl.hud.TargetHUD;
import ru.fluger.client.feature.impl.movement.Timer;

public class FeatureManager {
    public CopyOnWriteArrayList<Feature> features = new CopyOnWriteArrayList();

    public FeatureManager() {
        this.features.add(new NoOverlay());
        this.features.add(new ClickGui());
        this.features.add(new ArrayList());
        this.features.add(new Notifications());
        this.features.add(new TargetHUD());
        this.features.add(new HUD());
        this.features.add(new Timer());
        this.features.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().fontRenderer.getStringWidth(((Feature)m).getLabel())).reversed());
    }

    public List<Feature> getFeatureList() {
        return this.features;
    }

    public List<Feature> getFeaturesForCategory(Type category) {
        java.util.ArrayList<Feature> featureList = new java.util.ArrayList<Feature>();
        for (Feature feature : this.getFeatureList()) {
            if (feature.getType() != category) continue;
            featureList.add(feature);
        }
        return featureList;
    }

    public Feature getFeatureByClass(Class<? extends Feature> classModule) {
        for (Feature feature : this.getFeatureList()) {
            if (feature == null || feature.getClass() != classModule) continue;
            return feature;
        }
        return null;
    }

    public Feature getFeatureByLabel(String name) {
        for (Feature feature : this.getFeatureList()) {
            if (!feature.getLabel().equalsIgnoreCase(name)) continue;
            return feature;
        }
        return null;
    }
}

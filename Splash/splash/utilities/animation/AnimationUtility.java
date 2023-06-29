package splash.utilities.animation;

import splash.api.module.Module;

/**
 * Author: Ice
 * Created: 16:27, 13-Jun-20
 * Project: Client
 */
public class AnimationUtility {

    public static void animate(Module module) {
        if (module.getSlideAnimation() > 0) module.setSlideAnimation(module.getSlideAnimation() - 1);
    }
}

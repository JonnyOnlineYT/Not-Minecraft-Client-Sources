package org.spray.heaven.features.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "CameraClip", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class CameraClip extends Module {
	
	// Blocks the raytracing in the EntityRenderer class of the orientCamera()V method

}

package ru.wendoxd.models.obj;

import java.util.ArrayList;

public class GroupObject {
	public String name;
	public ArrayList<Face> faces = new ArrayList<Face>();
	public int glDrawingMode;

	public GroupObject() {
		this("");
	}

	public GroupObject(String name) {
		this(name, -1);
	}

	public GroupObject(String name, int glDrawingMode) {
		this.name = name;
		this.glDrawingMode = glDrawingMode;
	}

	public void render() {
		if (faces.size() > 0) {
			CustomTessellator tessellator = CustomTessellator.instance;
			tessellator.startDrawing(glDrawingMode);
			render(tessellator);
			tessellator.draw();
		}
	}

	public void render(CustomTessellator tessellator) {
		if (faces.size() > 0) {
			for (Face face : faces) {
				face.addFaceForRender(tessellator);
			}
		}
	}
}
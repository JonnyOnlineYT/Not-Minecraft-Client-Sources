package ru.wendoxd.models;

import ru.wendoxd.models.obj.CustomTessellator;
import ru.wendoxd.models.obj.Face;
import ru.wendoxd.models.obj.GroupObject;
import ru.wendoxd.models.obj.TextureCoordinate;
import ru.wendoxd.models.obj.Vertex;

import java.util.regex.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.zip.*;

import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.*;

public class ModelObject {
	private static Pattern vertexPattern;
	private static Pattern vertexNormalPattern;
	private static Pattern textureCoordinatePattern;
	private static Pattern face_V_VT_VN_Pattern;
	private static Pattern face_V_VT_Pattern;
	private static Pattern face_V_VN_Pattern;
	private static Pattern face_V_Pattern;
	private static Pattern groupObjectPattern;
	private static Matcher vertexMatcher;
	private static Matcher vertexNormalMatcher;
	private static Matcher textureCoordinateMatcher;
	private static Matcher face_V_VT_VN_Matcher;
	private static Matcher face_V_VT_Matcher;
	private static Matcher face_V_VN_Matcher;
	private static Matcher face_V_Matcher;
	private static Matcher groupObjectMatcher;
	public ArrayList<Vertex> vertices;
	public ArrayList<Vertex> vertexNormals;
	public ArrayList<TextureCoordinate> textureCoordinates;
	public ArrayList<GroupObject> groupObjects;
	private GroupObject currentGroupObject;
	private String fileName;
	public int[] lists;

	public ModelObject(final String fileName, final URL url) throws Exception {
		this.vertices = new ArrayList<Vertex>();
		this.vertexNormals = new ArrayList<Vertex>();
		this.textureCoordinates = new ArrayList<TextureCoordinate>();
		this.groupObjects = new ArrayList<GroupObject>();
		this.fileName = fileName;
		try {
			this.loadObjModel(url.openStream());
		} catch (IOException ex) {
			throw new Exception("IO Exception reading model format", ex);
		}
	}

	public ModelObject(final String fileName, final InputStream inputStream) throws Exception {
		this.vertices = new ArrayList<Vertex>();
		this.vertexNormals = new ArrayList<Vertex>();
		this.textureCoordinates = new ArrayList<TextureCoordinate>();
		this.groupObjects = new ArrayList<GroupObject>();
		this.fileName = fileName;
		this.loadObjModel(inputStream);
	}

	public ModelObject(final InputStream inputStream) throws Exception {
		this.vertices = new ArrayList<Vertex>();
		this.vertexNormals = new ArrayList<Vertex>();
		this.textureCoordinates = new ArrayList<TextureCoordinate>();
		this.groupObjects = new ArrayList<>();
		this.fileName = "unknown";
		this.lists = new int[1];
		this.loadObjModel(inputStream);
		this.lists[0] = GL11.glGenLists(1);
		GL11.glNewList(this.lists[0], 4864);
		this.renderAll();
		GL11.glEndList();
	}

	private void loadObjModel(final InputStream inputStream) throws Exception {
		BufferedReader bufferedReader = null;
		String s = null;
		int n = 0;
		try {
			final byte[] allFromAndClose = this.readAllFromAndClose(inputStream);
			try {
				bufferedReader = new BufferedReader(
						new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(allFromAndClose))));
				s = bufferedReader.readLine();
			} catch (Throwable t) {
			}
			if (s == null) {
				bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(allFromAndClose)));
				s = bufferedReader.readLine();
			}
			do {
				++n;
				String trim;
				if (!(trim = s.replaceAll("\\s+", " ").trim()).startsWith("#")) {
					trim = fixLine(trim);
					if (trim.length() == 0) {
						continue;
					}
					if (trim.startsWith("v ")) {
						final Vertex vertex = this.parseVertex(trim, n);
						if (vertex == null) {
							continue;
						}
						this.vertices.add(vertex);
					} else if (trim.startsWith("vn ")) {
						final Vertex vertexNormal = this.parseVertexNormal(trim, n);
						if (vertexNormal == null) {
							continue;
						}
						this.vertexNormals.add(vertexNormal);
					} else if (trim.startsWith("vt ")) {
						final TextureCoordinate textureCoordinate = this.parseTextureCoordinate(trim, n);
						if (textureCoordinate == null) {
							continue;
						}
						this.textureCoordinates.add(textureCoordinate);
					} else if (trim.startsWith("f ")) {
						if (this.currentGroupObject == null) {
							this.currentGroupObject = new GroupObject("Default");
						}
						final Face face;
						if ((face = this.parseFace(trim, n)) == null) {
							continue;
						}
						this.currentGroupObject.faces.add(face);
					} else {
						if (!(trim.startsWith("g ") | trim.startsWith("o "))) {
							continue;
						}
						final GroupObject groupObject = this.parseGroupObject(trim, n);
						if (groupObject != null && this.currentGroupObject != null) {
							this.groupObjects.add(this.currentGroupObject);
						}
						this.currentGroupObject = groupObject;
					}
				}
			} while ((s = bufferedReader.readLine()) != null);
			this.groupObjects.add(this.currentGroupObject);
		} catch (IOException ex) {
			throw new Exception("IO Exception reading model format", ex);
		}
	}

	private byte[] readAllFromAndClose(final InputStream inputStream) throws IOException {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final byte[] array = new byte[1024];
			int read;
			while ((read = inputStream.read(array)) != -1) {
				byteArrayOutputStream.write(array, 0, read);
			}
			inputStream.close();
			return byteArrayOutputStream.toByteArray();
		} finally {
			try {
				inputStream.close();
			} catch (IOException ex) {
			}
		}
	}

	public void renderAll() {
		CustomTessellator tessellator = CustomTessellator.instance;

		if (currentGroupObject != null) {
			tessellator.startDrawing(currentGroupObject.glDrawingMode);
		} else {
			tessellator.startDrawing(GL11.GL_TRIANGLES);
		}
		tessellateAll(tessellator);

		tessellator.draw();
	}

	public void tessellateAll(final CustomTessellator Tessellator) {
		final Iterator<GroupObject> iterator = this.groupObjects.iterator();
		while (iterator.hasNext()) {
			iterator.next().render(Tessellator);
		}
	}

	public void renderOnly(final String... array) {
		for (final GroupObject GroupObject : this.groupObjects) {
			for (int length = array.length, i = 0; i < length; ++i) {
				if (array[i].equalsIgnoreCase(GroupObject.name)) {
					GroupObject.render();
				}
			}
		}
	}

	public void tessellateOnly(final CustomTessellator Tessellator, final String... array) {
		for (final GroupObject GroupObject : this.groupObjects) {
			for (int length = array.length, i = 0; i < length; ++i) {
				if (array[i].equalsIgnoreCase(GroupObject.name)) {
					GroupObject.render(Tessellator);
				}
			}
		}
	}

	public void renderPart(final String s) {
		for (final GroupObject GroupObject : this.groupObjects) {
			System.out.println(GroupObject.name);
			if (s.equalsIgnoreCase(GroupObject.name)) {
				GroupObject.render();
			}
		}
	}

	public void tessellatePart(final CustomTessellator Tessellator, final String s) {
		for (final GroupObject GroupObject : this.groupObjects) {
			if (s.equalsIgnoreCase(GroupObject.name)) {
				GroupObject.render(Tessellator);
			}
		}
	}

	public void renderAllExcept(final String... array) {
		for (final GroupObject GroupObject : this.groupObjects) {
			boolean b = false;
			for (int length = array.length, i = 0; i < length; ++i) {
				if (array[i].equalsIgnoreCase(GroupObject.name)) {
					System.out.println("GroupObject = " + GroupObject.name);
					b = true;
				}
			}
			if (!b) {
				GroupObject.render();
			}
		}
	}

	public void tessellateAllExcept(final CustomTessellator Tessellator, final String... array) {
		for (final GroupObject GroupObject : this.groupObjects) {
			boolean b = false;
			for (int length = array.length, i = 0; i < length; ++i) {
				if (array[i].equalsIgnoreCase(GroupObject.name)) {
					b = true;
				}
			}
			if (!b) {
				GroupObject.render(Tessellator);
			}
		}
	}

	private Vertex parseVertex(String substring, final int n) throws Exception {
		if (isValidVertexLine(substring)) {
			substring = substring.substring(substring.indexOf(" ") + 1);
			final String[] split = substring.split(" ");
			try {
				if (split.length == 2) {
					return new Vertex(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
				}
				if (split.length == 3) {
					return new Vertex(Float.parseFloat(split[0]), Float.parseFloat(split[1]),
							Float.parseFloat(split[2]));
				}
			} catch (NumberFormatException ex) {
				throw new Exception(String.format("Number formatting error at line %d", n), ex);
			}
			return null;
		}
		throw new Exception("Error parsing entry ('" + substring + "', line " + n + ") in file '" + this.fileName
				+ "' - Incorrect format");
	}

	public String fixLine(String input) {
		String[] values = input.split(" ");
		StringBuilder builder = new StringBuilder();
		for (String s : values) {
			if (isDecimal(s)) {
				try {
					double in = Double.valueOf(s);
					builder.append(new DecimalFormat("0.000000").format(in));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				builder.append(s);
			}
			builder.append(" ");
		}
		return builder.toString();
	}

	public boolean isDecimal(String line) {
		char[] buffer = line.toCharArray();
		boolean decimal = false;
		for (char c : buffer) {
			if (c >= '0' && c <= '9' || c == '-')
				decimal = true;
			else if (!decimal)
				return false;
			if (c == '/')
				return false;
		}
		return decimal;
	}

	private Vertex parseVertexNormal(String substring, final int n) throws Exception {
		if (isValidVertexNormalLine(substring)) {
			substring = substring.substring(substring.indexOf(" ") + 1);
			final String[] split = substring.split(" ");
			try {
				if (split.length == 3) {
					return new Vertex(Float.parseFloat(split[0]), Float.parseFloat(split[1]),
							Float.parseFloat(split[2]));
				}
			} catch (NumberFormatException ex) {
				throw new Exception(String.format("Number formatting error at line %d", n), ex);
			}
			return null;
		}
		throw new Exception("Error parsing entry ('" + substring + "', line " + n + ") in file '" + this.fileName
				+ "' - Incorrect format");
	}

	private TextureCoordinate parseTextureCoordinate(String substring, final int n) throws Exception {
		if (isValidTextureCoordinateLine(substring)) {
			substring = substring.substring(substring.indexOf(" ") + 1);
			final String[] split = substring.split(" ");
			try {
				if (split.length == 2) {
					return new TextureCoordinate(Float.parseFloat(split[0]), 1.0f - Float.parseFloat(split[1]));
				}
				if (split.length == 3) {
					return new TextureCoordinate(Float.parseFloat(split[0]), 1.0f - Float.parseFloat(split[1]),
							Float.parseFloat(split[2]));
				}
			} catch (NumberFormatException ex) {
				throw new Exception(String.format("Number formatting error at line %d", n), ex);
			}
			return null;
		}
		throw new Exception("Error parsing entry ('" + substring + "', line " + n + ") in file '" + this.fileName
				+ "' - Incorrect format");
	}

	private Face parseFace(final String s, final int n) throws Exception {
		if (isValidFaceLine(s)) {
			final Face Face = new Face();
			final String[] split = s.substring(s.indexOf(" ") + 1).split(" ");
			if (split.length == 3) {
				if (this.currentGroupObject.glDrawingMode == -1) {
					this.currentGroupObject.glDrawingMode = 4;
				} else if (this.currentGroupObject.glDrawingMode != 4) {
				}
			} else if (split.length == 4) {
				if (this.currentGroupObject.glDrawingMode == -1) {
					this.currentGroupObject.glDrawingMode = 7;
				} else if (this.currentGroupObject.glDrawingMode != 7) {
					throw new Exception("Error parsing entry ('" + s + "', line " + n + ") in file '" + this.fileName
							+ "' - Invalid number of points for face (expected 3, found " + split.length + ")");
				}
			}
			if (isValidFace_V_VT_VN_Line(s)) {
				Face.vertices = new Vertex[split.length];
				Face.textureCoordinates = new TextureCoordinate[split.length];
				Face.vertexNormals = new Vertex[split.length];
				for (int i = 0; i < split.length; ++i) {
					final String[] split2 = split[i].split("/");
					Face.vertices[i] = this.vertices.get(Integer.parseInt(split2[0]) - 1);
					Face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(split2[1]) - 1);
					Face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(split2[2]) - 1);
				}
				Face.faceNormal = Face.calculateFaceNormal();
			} else if (isValidFace_V_VT_Line(s)) {
				Face.vertices = new Vertex[split.length];
				Face.textureCoordinates = new TextureCoordinate[split.length];
				for (int j = 0; j < split.length; ++j) {
					final String[] split3 = split[j].split("/");
					Face.vertices[j] = this.vertices.get(Integer.parseInt(split3[0]) - 1);
					Face.textureCoordinates[j] = this.textureCoordinates.get(Integer.parseInt(split3[1]) - 1);
				}
				Face.faceNormal = Face.calculateFaceNormal();
			} else if (isValidFace_V_VN_Line(s)) {
				Face.vertices = new Vertex[split.length];
				Face.vertexNormals = new Vertex[split.length];
				for (int k = 0; k < split.length; ++k) {
					final String[] split4 = split[k].split("//");
					Face.vertices[k] = this.vertices.get(Integer.parseInt(split4[0]) - 1);
					Face.vertexNormals[k] = this.vertexNormals.get(Integer.parseInt(split4[1]) - 1);
				}
				Face.faceNormal = Face.calculateFaceNormal();
			} else {
				if (!isValidFace_V_Line(s)) {
					throw new Exception("Error parsing entry ('" + s + "', line " + n + ") in file '" + this.fileName
							+ "' - Incorrect format");
				}
				Face.vertices = new Vertex[split.length];
				for (int l = 0; l < split.length; ++l) {
					Face.vertices[l] = this.vertices.get(Integer.parseInt(split[l]) - 1);
				}
				Face.faceNormal = Face.calculateFaceNormal();
			}
			return Face;
		}
		throw new Exception(
				"Error parsing entry ('" + s + "', line " + n + ") in file '" + this.fileName + "' - Incorrect format");
	}

	private GroupObject parseGroupObject(String s, final int n) throws Exception {
		GroupObject GroupObject = null;
		s = s.replace(".", "");
		if (isValidGroupObjectLine(s)) {
			final String substring = s.substring(s.indexOf(" ") + 1);
			if (substring.length() > 0) {
				GroupObject = new GroupObject(substring);
			}
			return GroupObject;
		}
		throw new Exception(
				"Error parsing entry ('" + s + "', line " + n + ") in file '" + this.fileName + "' - Incorrect format");
	}

	private static boolean isValidVertexLine(final String s) {
		if (ModelObject.vertexMatcher != null) {
			ModelObject.vertexMatcher.reset();
		}
		ModelObject.vertexMatcher = ModelObject.vertexPattern.matcher(s);
		return ModelObject.vertexMatcher.matches();
	}

	private static boolean isValidVertexNormalLine(final String s) {
		if (ModelObject.vertexNormalMatcher != null) {
			ModelObject.vertexNormalMatcher.reset();
		}
		ModelObject.vertexNormalMatcher = ModelObject.vertexNormalPattern.matcher(s);
		return ModelObject.vertexNormalMatcher.matches();
	}

	private static boolean isValidTextureCoordinateLine(final String s) {
		if (ModelObject.textureCoordinateMatcher != null) {
			ModelObject.textureCoordinateMatcher.reset();
		}
		ModelObject.textureCoordinateMatcher = ModelObject.textureCoordinatePattern.matcher(s);
		return ModelObject.textureCoordinateMatcher.matches();
	}

	private static boolean isValidFace_V_VT_VN_Line(final String s) {
		if (ModelObject.face_V_VT_VN_Matcher != null) {
			ModelObject.face_V_VT_VN_Matcher.reset();
		}
		ModelObject.face_V_VT_VN_Matcher = ModelObject.face_V_VT_VN_Pattern.matcher(s);
		return ModelObject.face_V_VT_VN_Matcher.matches();
	}

	private static boolean isValidFace_V_VT_Line(final String s) {
		if (ModelObject.face_V_VT_Matcher != null) {
			ModelObject.face_V_VT_Matcher.reset();
		}
		ModelObject.face_V_VT_Matcher = ModelObject.face_V_VT_Pattern.matcher(s);
		return ModelObject.face_V_VT_Matcher.matches();
	}

	private static boolean isValidFace_V_VN_Line(final String s) {
		if (ModelObject.face_V_VN_Matcher != null) {
			ModelObject.face_V_VN_Matcher.reset();
		}
		ModelObject.face_V_VN_Matcher = ModelObject.face_V_VN_Pattern.matcher(s);
		return ModelObject.face_V_VN_Matcher.matches();
	}

	private static boolean isValidFace_V_Line(final String s) {
		if (ModelObject.face_V_Matcher != null) {
			ModelObject.face_V_Matcher.reset();
		}
		ModelObject.face_V_Matcher = ModelObject.face_V_Pattern.matcher(s);
		return ModelObject.face_V_Matcher.matches();
	}

	private static boolean isValidFaceLine(final String s) {
		return isValidFace_V_VT_VN_Line(s) || isValidFace_V_VT_Line(s) || isValidFace_V_VN_Line(s)
				|| isValidFace_V_Line(s);
	}

	private static boolean isValidGroupObjectLine(final String s) {
		if (ModelObject.groupObjectMatcher != null) {
			ModelObject.groupObjectMatcher.reset();
		}
		ModelObject.groupObjectMatcher = ModelObject.groupObjectPattern.matcher(s);
		return ModelObject.groupObjectMatcher.matches();
	}

	public String getType() {
		return "obj";
	}

	static {
		ModelObject.vertexPattern = Pattern
				.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
		ModelObject.vertexNormalPattern = Pattern
				.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
		ModelObject.textureCoordinatePattern = Pattern
				.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
		ModelObject.face_V_VT_VN_Pattern = Pattern
				.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
		ModelObject.face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
		ModelObject.face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
		ModelObject.face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
		ModelObject.groupObjectPattern = Pattern.compile("([go]( [\\w\\d]+) *\\n)|([go]( [\\w\\d]+) *$)");
	}
}

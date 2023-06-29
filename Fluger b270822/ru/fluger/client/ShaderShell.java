/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.GL20
 */
package ru.fluger.client;

import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

public class ShaderShell {
    public static ShaderShell BLUR_SHADER;
    public static ShaderShell MENU_SHADER;
    public static ShaderShell CIRCLE_TEXTURE_SHADER;
    public static ShaderShell CIRCLE_SHADER;
    public static ShaderShell SCROLL_SHADER;
    public static ShaderShell FONTRENDERER_SUBSTRING;
    public static ShaderShell ROUNDED_RECT;
    public static ShaderShell CHAMS_SHADER;
    private int shaderID;

    public ShaderShell(String shaderName, int type) {
        this.parseShaderFromFile(shaderName, type);
    }

    public ShaderShell(String shaderName) {
        this(shaderName, 35632);
    }

    public static void init() {
        BLUR_SHADER = new ShaderShell("#version 120\r\n\r\nuniform sampler2D textureIn, mainTexture;\r\nuniform vec2 texelSize, direction;\r\nuniform float radius;\r\nuniform float weights[256];\r\n\r\n#define offset texelSize * direction\r\n\r\nvoid main() {\r\n    vec4 clr = texture2D(mainTexture, gl_TexCoord[0].xy);\r\n\tif (clr.a >= 0.1) {\r\n        vec3 blr = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];\r\n        for (float f = 1.0; f <= radius; f++) {\r\n            blr += texture2D(textureIn, gl_TexCoord[0].st + f * offset).rgb * (weights[int(abs(f))]);\r\n            blr += texture2D(textureIn, gl_TexCoord[0].st - f * offset).rgb * (weights[int(abs(f))]);\r\n        }\r\n        gl_FragColor = vec4(blr, 1.0);\r\n\t} else {\r\n\t    gl_FragColor = clr; \r\n\t}\r\n}\r\n");
        MENU_SHADER = new ShaderShell("#version 120PIDORASuniform sampler2D texture;PIDORASuniform sampler2D textureBG;PIDORASuniform vec3 color;PIDORASuniform vec2 oneTexel;PIDORASuniform float alpha;PIDORASPIDORASvoid main() {PIDORAS    vec2 pos = gl_TexCoord[0];PIDORAS    vec4 tc = texture2D(texture, pos);PIDORAS\tvec4 tbg = texture2D(textureBG, pos);PIDORAS\tvec4 c = vec4(vec3(tbg.r + tbg.g + tbg.b) / 3, 1);PIDORAS\tc.r = c.r * (1 + color.r * alpha);PIDORAS\tc.g = c.g * (1 + color.g * alpha);PIDORAS\tc.b = c.b * (1 + color.b * alpha);PIDORAS\tgl_FragColor = mix(c, tc, tc.a * alpha);PIDORAS}PIDORAS");
        CIRCLE_SHADER = new ShaderShell("#version 120PIDORASuniform vec4 color;PIDORASuniform vec2 resolution;PIDORASuniform vec2 start;PIDORASuniform vec2 end;PIDORASuniform float glow;PIDORASuniform float radius;PIDORASPIDORASvoid main() {PIDORAS    vec2 pos = gl_FragCoord.xy;PIDORAS\tpos.y = resolution.y - pos.y;PIDORAS\tpos = (pos - start) / (end - start);PIDORAS\tfloat len = 1 - length(pos - vec2(0.5));PIDORAS\tfloat smo = smoothstep(radius, radius + glow, len);PIDORAS\tgl_FragColor = vec4(smo * color);PIDORAS}PIDORAS");
        CIRCLE_TEXTURE_SHADER = new ShaderShell("#version 120PIDORASuniform sampler2D texture;PIDORASuniform float radius;PIDORASuniform float glow;PIDORASPIDORASvoid main() {PIDORAS    vec2 texCoord = gl_TexCoord[0];PIDORAS\tvec4 pixel = texture2D(texture, texCoord);PIDORAS\tfloat dst = length(vec2(0.5) - texCoord);PIDORAS\tfloat f = smoothstep(radius, radius + glow, 1 - dst);PIDORAS\tgl_FragColor = pixel * f;PIDORAS}PIDORAS");
        SCROLL_SHADER = new ShaderShell("#version 120PIDORASuniform sampler2D texture;PIDORASuniform sampler2D textureBG;PIDORASuniform vec2 resolution;PIDORASuniform int maxY;PIDORASuniform int minY;PIDORASPIDORASvoid main() {PIDORAS    vec2 pos = gl_TexCoord[0];PIDORAS\tvec2 rpos = vec2(gl_FragCoord.x, resolution.y - gl_FragCoord.y);PIDORAS\tvec4 tbg = texture2D(textureBG, pos);PIDORAS    vec4 tc = texture2D(texture, pos);PIDORAS\tif (tc.a != 0) {PIDORAS\t    if (rpos.y > maxY - 40) {PIDORAS\t\t    tc.a = tc.a * smoothstep(0, 0.5, length(vec2(gl_FragCoord.x, maxY) - rpos) / 40);PIDORAS\t    }PIDORAS\t    if (rpos.y < minY + 40) {PIDORAS\t\t    tc.a = tc.a * smoothstep(0, 0.5, length(vec2(gl_FragCoord.x, minY) - rpos) / 40);PIDORAS\t    }PIDORAS\t}PIDORAS\tgl_FragColor = mix(tbg, tc, tc.a);PIDORAS}PIDORAS");
        FONTRENDERER_SUBSTRING = new ShaderShell("#version 120PIDORASuniform sampler2D font;PIDORASuniform vec4 inColor;PIDORASuniform float width;PIDORASuniform float maxWidth;PIDORASPIDORASvoid main() {PIDORAS\tfloat f = clamp(smoothstep(0.5, 1, 1 - (gl_FragCoord.x - maxWidth) / width), 0, 1);PIDORAS    vec2 pos = gl_TexCoord[0].xy;PIDORAS\tvec4 color = texture2D(font, pos);PIDORAS\tif(color.a > 0) {PIDORAS\t   color.a = color.a * f;PIDORAS\t}PIDORAS\tgl_FragColor = color * inColor;PIDORAS}PIDORAS");
        ROUNDED_RECT = new ShaderShell("#version 120PIDORASuniform vec4 color;PIDORASuniform vec2 resolution;PIDORASuniform vec2 center;PIDORASuniform vec2 dst;PIDORASuniform float radius;PIDORASPIDORASfloat rect(vec2 pos, vec2 center, vec2 size) {  PIDORAS    return length(max(abs(center - pos) - (size / 2), 0));PIDORAS}PIDORASPIDORASvoid main() {PIDORAS    vec2 pos = gl_FragCoord.xy;PIDORAS\tpos.y = resolution.y - pos.y;PIDORAS\tgl_FragColor = vec4(vec3(color), (1 - rect(pos, center, dst) / radius) * color.a);PIDORAS}PIDORAS");
    }

    public void attach() {
        ARBShaderObjects.glUseProgramObjectARB((int)this.shaderID);
    }

    public void set1I(String name, int value0) {
        ARBShaderObjects.glUniform1iARB((int)ARBShaderObjects.glGetUniformLocationARB((int)this.shaderID, (CharSequence)name), (int)value0);
    }

    public void set1F(String name, float value0) {
        ARBShaderObjects.glUniform1fARB((int)ARBShaderObjects.glGetUniformLocationARB((int)this.shaderID, (CharSequence)name), (float)value0);
    }

    public void set2F(String name, float value0, float value1) {
        ARBShaderObjects.glUniform2fARB((int)ARBShaderObjects.glGetUniformLocationARB((int)this.shaderID, (CharSequence)name), (float)value0, (float)value1);
    }

    public void set3F(String name, float value0, float value1, float value2) {
        ARBShaderObjects.glUniform3fARB((int)ARBShaderObjects.glGetUniformLocationARB((int)this.shaderID, (CharSequence)name), (float)value0, (float)value1, (float)value2);
    }

    public void set4F(String name, float value0, float value1, float value2, float value3) {
        ARBShaderObjects.glUniform4fARB((int)ARBShaderObjects.glGetUniformLocationARB((int)this.shaderID, (CharSequence)name), (float)value0, (float)value1, (float)value2, (float)value3);
    }

    public void detach() {
        ARBShaderObjects.glUseProgramObjectARB((int)0);
    }

    private void parseShaderFromFile(String source, int i) {
        try {
            this.localInit(source.replace("PIDORAS", "\n").replace("gl_TexCoord[0];", "gl_TexCoord[0].xy;"), i);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupUniforms(float offset) {
        Minecraft mc = Minecraft.getMinecraft();
        this.set1I("textureIn", 2);
        this.set1I("mainTexture", 0);
        this.set2F("texelSize", 1.0f / (float)mc.displayWidth, 1.0f / (float)mc.displayHeight);
        this.set2F("direction", 1.0f, 1.0f);
        this.set1F("radius", offset);
        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer((int)256);
        int i = 0;
        while ((float)i <= offset) {
            weightBuffer.put(ShaderShell.calculateGaussianValue(i, offset / 2.0f));
            ++i;
        }
        weightBuffer.rewind();
        ARBShaderObjects.glUniform1ARB((int)ARBShaderObjects.glGetUniformLocationARB((int)this.shaderID, (CharSequence)"weights"), (FloatBuffer)weightBuffer);
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(6.283185306 * (double)(sigma * sigma));
        return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(sigma * sigma))));
    }

    void localInit(String str, int i) {
        int shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
        if (shaderProgram == 0) {
            System.out.println("PC Issued");
            System.exit(0);
            return;
        }
        int shader = ARBShaderObjects.glCreateShaderObjectARB((int)i);
        ARBShaderObjects.glShaderSourceARB((int)shader, (CharSequence)str);
        ARBShaderObjects.glCompileShaderARB((int)shader);
        if (GL20.glGetShaderi((int)shader, (int)35713) == 0) {
            String s = GL20.glGetShaderInfoLog((int)shader, (int)500);
            System.out.println("Error [ " + s + " ]");
        } else {
            System.out.println("Ok");
        }
        ARBShaderObjects.glAttachObjectARB((int)shaderProgram, (int)shader);
        ARBShaderObjects.glLinkProgramARB((int)shaderProgram);
        this.shaderID = shaderProgram;
    }
}


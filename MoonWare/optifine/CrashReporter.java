package optifine;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import shadersmod.client.Shaders;

public class CrashReporter {
    public static void extendCrashReport(CrashReportCategory report) {
        report.addCrashSection("OptiFine Version", Config.getVersion());
        if (Config.getGameSettings() != null) {
            report.addCrashSection("Render Distance Chunks", "" + Config.getChunkViewDistance());
            report.addCrashSection("Mipmaps", "" + Config.getMipmapLevels());
            report.addCrashSection("Anisotropic Filtering", "" + Config.getAnisotropicFilterLevel());
            report.addCrashSection("Antialiasing", "" + Config.getAntialiasingLevel());
            report.addCrashSection("Multitexture", "" + Config.isMultiTexture());
        }
        report.addCrashSection("Shaders", "" + Shaders.getShaderPackName());
        report.addCrashSection("OpenGlVersion", "" + Config.openGlVersion);
        report.addCrashSection("OpenGlRenderer", "" + Config.openGlRenderer);
        report.addCrashSection("OpenGlVendor", "" + Config.openGlVendor);
        report.addCrashSection("CpuCount", "" + Config.getAvailableProcessors());
    }
}

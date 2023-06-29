package net.minecraft.util;

import com.google.common.base.MoreObjects;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.FutureTask;

public enum OS {
    WINDOWS,
    OSX,
    LINUX,
    UNKNOWN;

    public static OS getCurrent() {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (s.contains("win")) return WINDOWS;
        if (s.contains("mac")) return OSX;
        if (s.contains("linux") || s.contains("unix")) return LINUX;
        return UNKNOWN;
    }

    public static void openUri(String uri) {
        try {
            Desktop.getDesktop().browse(new URI(uri));
        } catch (Exception ignored) {
            Sys.openURL(uri);
        }
    }

    public static void openUri(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (Exception ignored) {
            Sys.openURL(uri.toString());
        }
    }

    public static void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception ignored) {
            Sys.openURL(file.toURI().toString());
        }
    }

    public static String getClipboard() {
        try {
            Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) t.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ignored) {}
        return MoreObjects.firstNonNull(Sys.getClipboard(), "");
    }

    public static void setClipboard(String s) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
        } catch (Exception ignored) {}
    }

    @Nullable
    public static <V> V runTask(FutureTask<V> task, Logger logger) {
        try {
            task.run();
            return task.get();
        } catch (Exception e) {}
        return null;
    }

    public static <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);
    }
}

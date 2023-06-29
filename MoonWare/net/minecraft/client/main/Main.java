package net.minecraft.client.main;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.InputStream;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("fullscreen");
        OptionSpec<File> gameDirOpt = parser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        OptionSpec<String> usernameOpt = parser.accepts("username").withRequiredArg().defaultsTo("Unknown" + Minecraft.getSystemTime() % 1000000L);
        OptionSpec<String> uuidOpt = parser.accepts("uuid").withRequiredArg().defaultsTo("0");
        OptionSpec<String> accessTokenOpt = parser.accepts("accessToken").withRequiredArg().required();
        OptionSpec<Integer> widthOpt = parser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        OptionSpec<Integer> heightOpt = parser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        OptionSet set = parser.parse(args);
        Thread.currentThread().setName("Client thread");
        Minecraft.start(new Session(usernameOpt.value(set), uuidOpt.value(set), accessTokenOpt.value(set), "msa"), gameDirOpt.value(set), widthOpt.value(set), heightOpt.value(set), set.has("fullscreen"));
    }
}

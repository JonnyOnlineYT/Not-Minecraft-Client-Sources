package net.minecraft.client.resources;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Sets;
import net.minecraft.util.OS;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Set;

public class FolderResourcePack extends AbstractResourcePack {
    private static final boolean field_191386_b = OS.getCurrent() == OS.WINDOWS;
    private static final CharMatcher field_191387_c = CharMatcher.is('\\');

    public FolderResourcePack(File resourcePackFileIn) {
        super(resourcePackFileIn);
    }

    protected static boolean func_191384_a(File p_191384_0_, String p_191384_1_) throws IOException {
        String s = p_191384_0_.getCanonicalPath();

        if (field_191386_b) {
            s = field_191387_c.replaceFrom(s, '/');
        }

        return s.endsWith(p_191384_1_);
    }

    protected InputStream getInputStreamByName(String name) throws IOException {
        File file1 = func_191385_d(name);

        if (file1 == null) {
            throw new ResourcePackFileNotFoundException(resourcePackFile, name);
        } else {
            return new BufferedInputStream(new FileInputStream(file1));
        }
    }

    protected boolean hasResourceName(String name) {
        return func_191385_d(name) != null;
    }

    @Nullable
    private File func_191385_d(String p_191385_1_) {
        try {
            File file1 = new File(resourcePackFile, p_191385_1_);

            if (file1.isFile() && func_191384_a(file1, p_191385_1_)) {
                return file1;
            }
        } catch (IOException var3) {
        }

        return null;
    }

    public Set<String> getResourceDomains() {
        Set<String> set = Sets.newHashSet();
        File file1 = new File(resourcePackFile, "assets/");

        if (file1.isDirectory()) {
            for (File file2 : file1.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
                String s = AbstractResourcePack.getRelativeName(file1, file2);

                if (s.equals(s.toLowerCase(java.util.Locale.ROOT))) {
                    set.add(s.substring(0, s.length() - 1));
                } else {
                    logNameNotLowercase(s);
                }
            }
        }

        return set;
    }
}

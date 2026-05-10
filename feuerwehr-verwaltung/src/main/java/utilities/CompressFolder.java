/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressFolder {
    static byte[] buffer = new byte[8192];
    static int len = 0;

    public static void ZipErstellen(String zuzipdatei, String ausgangdatei) throws Exception {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zuzipdatei));
        File file = new File(ausgangdatei);
        CompressFolder.treeWalkAndCompressTo(file, file.getParentFile().toURI(), zipOutputStream);
        zipOutputStream.close();
    }

    static void treeWalkAndCompressTo(File file, URI relateTo, ZipOutputStream zipOutputStream) throws Exception {
        if (!file.isDirectory()) {
            URI relativePath = relateTo.relativize(file.toURI());
            ZipEntry entry = new ZipEntry(relativePath.toString());
            zipOutputStream.putNextEntry(entry);
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            fis.close();
            zipOutputStream.closeEntry();
        } else {
            File[] children = file.listFiles();
            int i = 0;
            while (i < children.length) {
                File child = children[i];
                CompressFolder.treeWalkAndCompressTo(child, relateTo, zipOutputStream);
                ++i;
            }
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;

public class Datei {
    public static void copyFileAusf\u00fchren(File in, String out) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream(in);
            FileOutputStream fileOutputStream = new FileOutputStream(out);
            FileChannel inputChannel = fileInputStream.getChannel();
            FileChannel outputChannel = fileOutputStream.getChannel();
            Datei.transfer(inputChannel, outputChannel, in.length(), 0x2000000L, true, true);
            fileInputStream.close();
            fileOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delFolder(File dir) {
        if (dir.isDirectory()) {
            String[] entries = dir.list();
            int x = 0;
            while (x < entries.length) {
                File aktFile = new File(dir.getPath(), entries[x]);
                Datei.delFolder(aktFile);
                ++x;
            }
            return dir.delete();
        }
        return dir.delete();
    }

    public static void transfer(FileChannel fileChannel, ByteChannel byteChannel, long lengthInBytes, long chunckSizeInBytes, boolean verbose, boolean fromFile) throws IOException {
        long overallBytesTransfered = 0L;
        long time = -System.currentTimeMillis();
        while (overallBytesTransfered < lengthInBytes) {
            long bytesTransfered = 0L;
            bytesTransfered = fromFile ? fileChannel.transferTo(0L, Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered), byteChannel) : fileChannel.transferFrom(byteChannel, overallBytesTransfered, Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered));
            overallBytesTransfered += bytesTransfered;
            if (!verbose) continue;
            System.out.printf("overall bytes transfered: %s progress %s%%\n", overallBytesTransfered, Math.round((double)overallBytesTransfered / (double)lengthInBytes * 100.0));
        }
        time += System.currentTimeMillis();
        if (verbose) {
            System.out.printf("Transfered: %s bytes in: %s s -> %s kbytes/s", overallBytesTransfered, time / 1000L, (double)overallBytesTransfered / 1024.0 / ((double)time / 1000.0));
        }
    }

    public static double getDirSize(File dir) {
        double size = 0.0;
        File[] files = dir.listFiles();
        if (files != null) {
            int i = 0;
            while (i < files.length) {
                size = files[i].isDirectory() ? (size += Datei.getDirSize(files[i])) : (size += (double)files[i].length());
                ++i;
            }
        }
        return size;
    }
}


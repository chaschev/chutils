package com.chaschev.chutils.io;

import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

/**
 * User: chaschev
 * Date: 22/07/12
 */
public class FileUtils {
    public static String LINE_SEPARATOR = "\n";

    public static ByteBuffer readFile(File file) throws Exception {
        return readFile(file, -1);
    }

    public static String readFileToString(File file) throws Exception {
        return ByteBufferUtils.string(FileUtils.readFile(file));
    }

    public static String readFileToString(File file, Charset charset) throws Exception {
        return ByteBufferUtils.string(FileUtils.readFile(file), charset);
    }

    public static ByteBuffer readFile(File file, int limit) throws Exception {
        long length = Math.min(file.length(), limit == -1 ? Integer.MAX_VALUE : limit);

        if (length == 0) {
            return ByteBuffer.allocate(0);
        }

        ByteBuffer buf = ByteBuffer.allocateDirect((int) length);


        FileInputStream fis = null;
        ReadableByteChannel channel = null;

        try {
            fis = new FileInputStream(file);
            channel = fis.getChannel();

            int pos = 0;
            while (pos >= 0 && buf.remaining() > 0) {
                pos = channel.read(buf);
            }


            buf.rewind();

            return buf;
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(channel);
        }
    }

    public static byte[] toByteArray(InputStream inputStream, int limit, @Nullable ByteArrayOutputStream reusableStream) throws IOException {
        if (limit == -1) {
            limit = Integer.MAX_VALUE;
        }

        if (reusableStream == null) {
            reusableStream = new ByteArrayOutputStream(256 * 1024);
        }

        final int chunkSize = 16384;
        byte[] buffer = new byte[chunkSize];
        int lastRead = 0;
        int totalRead = 0;

        while (lastRead != -1 && totalRead < limit) {
            if (limit - totalRead < chunkSize) {
                lastRead = inputStream.read(buffer, 0, limit - totalRead);
            } else {
                lastRead = inputStream.read(buffer);
            }

            if (lastRead > 0) {
                reusableStream.write(buffer, 0, lastRead);

                totalRead += lastRead;
            }
        }

        return reusableStream.toByteArray();
    }

    public static String humanReadableByteCount(final long bytes, final boolean si) {
        return humanReadableByteCount(bytes, si, true);
    }

    public static String humanReadableByteCount(final long bytes, final boolean si, boolean addByte) {
        final int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + (addByte ? " B" : "");
        final int exp = (int) (Math.log(bytes) / Math.log(unit));
        final char pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1);
        return
            addByte ?
                String.format("%.1f %cB", bytes / Math.pow(unit, exp), pre) :
                String.format("%.1f%c", bytes / Math.pow(unit, exp), pre)
            ;
    }

    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, false);
    }
}

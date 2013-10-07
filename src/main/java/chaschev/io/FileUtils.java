package chaschev.io;

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

    public static ByteCount humanReadableByteCount(final double bytes) {
        return humanReadableByteCount(bytes, false);
    }

    public static ByteCount humanReadableByteCount(final double bytes, final boolean si) {
        return humanReadableByteCount(bytes, si, true);
    }

     public static class ByteCount{
        final double bytes;
        final double count;
        final String unit;

        public ByteCount(double bytes, final boolean si, final boolean addByte) {
            this.bytes = bytes;

            final int unitInt = si ? 1000 : 1024;
            if (bytes < unitInt) {
                this.count = bytes;
                unit = (addByte ? " B" : "");
                return;
            }

            final int exp = (int) (Math.log(bytes) / Math.log(unitInt));
            final char pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1);

            count = bytes / Math.pow(unitInt, exp);
            unit = pre + (addByte ? "B" : "");
        }

        public String toString(int fractionLength) {
            return String.format("%." +
                fractionLength +
                "f %s", count, unit);
        }

        @Override
        public String toString() {
            return toString(1);
        }
    }

    public static ByteCount humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, false);
    }

    public static ByteCount humanReadableByteCount(final double bytes, final boolean si, boolean addByte) {
        return new ByteCount(bytes, si, addByte);
    }
}

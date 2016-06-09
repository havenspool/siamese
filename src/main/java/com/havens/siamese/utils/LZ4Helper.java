package com.havens.siamese.utils;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by havens on 15-8-11.
 */
public class LZ4Helper{
    public static void main(String[] args) throws Exception {
        example();

        byte[] data = "123547".getBytes("UTF-8");
        byte[] compressed=compressed(data);
        System.out.println("compressor:"+new String(compressed));
        System.out.println("compressor:"+new String(Uncompress(compressed)));
    }

    private static void example() throws UnsupportedEncodingException {
        LZ4Factory factory = LZ4Factory.fastestInstance();

        byte[] data = "12345345234572".getBytes("UTF-8");
        final int decompressedLength = data.length;
        System.out.println("data:"+new String(data));

        // compress data
        LZ4Compressor compressor = factory.fastCompressor();
        int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
        byte[] compressed = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);
        System.out.println("compressor:"+new String(compressed));

        // decompress data
        // - method 1: when the decompressed length is known
        LZ4FastDecompressor decompressor = factory.fastDecompressor();
        byte[] restored = new byte[decompressedLength];
        int compressedLength2 = decompressor.decompress(compressed, 0, restored, 0, decompressedLength);
        // compressedLength == compressedLength2
        System.out.println("decompressor:"+new String(restored));

        // - method 2: when the compressed length is known (a little slower)
        // the destination buffer needs to be over-sized
        LZ4SafeDecompressor decompressor2 = factory.safeDecompressor();
        int decompressedLength2 = decompressor2.decompress(compressed, 0, compressedLength, restored, 0);
        // decompressedLength == decompressedLength2
    }

    private static final LZ4Factory factory = LZ4Factory.fastestInstance();
    private static final LZ4Compressor compressor=factory.fastCompressor();
    private static final LZ4FastDecompressor decompressor= factory.fastDecompressor();

    public static byte[] compressed(byte[] data) {
        int maxCompressedLength = compressor.maxCompressedLength(data.length);
        byte[] compressed = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(data, 0, data.length, compressed, 0, maxCompressedLength);
        return compressed;
    }


    public static byte[] Uncompress(byte[] data) {
        byte[] restored = new byte[data.length];
        byte[] compressed = new byte[data.length];
        int compressedLength2 = decompressor.decompress(compressed, 0, restored, 0, data.length);
        return restored;
    }
}

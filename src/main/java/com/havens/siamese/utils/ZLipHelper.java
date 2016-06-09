package com.havens.siamese.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Created by havens on 15-8-11.
 */
public class ZLipHelper {
    public static void main(String[] args)throws Exception{
        byte[] data = "123547".getBytes("UTF-8");
        System.out.println("data:"+data.length+":"+new String(data));
        byte[] compress=compress(data);
        System.out.println("compress:"+compress.length+":"+new String(compress));
        byte[] decompress=decompress(compress);
        System.out.println("decompress:"+decompress.length+":"+new String(decompress));
    }

    private final static Object DECOMPRESS = new Object();
    private final static Object COMPRESS = new Object();
    private final static Deflater compresser = new Deflater(Deflater.BEST_SPEED, true);
    private final static Inflater decompresser = new Inflater(true);
    private static ByteArrayOutputStream bos;
    private static DeflaterOutputStream dos;
    private static ByteArrayOutputStream bos_de;
    private static InflaterInputStream iis;

    public static byte[] compress(byte[] data) {
        synchronized (COMPRESS){
            byte[] output = new byte[0];
            compresser.reset();
            compresser.setInput(data);
            compresser.finish();
            bos = new ByteArrayOutputStream(data.length);
            try {
                byte[] buf = new byte[1024];
                while (!compresser.finished()) {
                    int i = compresser.deflate(buf);
                    bos.write(buf, 0, i);
                }
                output = bos.toByteArray();
            } catch (Exception e) {
                output = data;
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            compresser.end();
            return output;
        }
    }

    public static void compress(byte[] data, OutputStream os) {
        synchronized (COMPRESS){
            dos = new DeflaterOutputStream(os);
            try {
                dos.write(data, 0, data.length);
                dos.finish();
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] decompress(byte[] data) {
        synchronized (DECOMPRESS){
            byte[] output = new byte[0];
            decompresser.reset();
            decompresser.setInput(data);
            bos_de = new ByteArrayOutputStream(data.length);
            try {
                byte[] buf = new byte[1024];
                while (!decompresser.finished()) {
                    int i = decompresser.inflate(buf);
                    bos_de.write(buf, 0, i);
                }
                output = bos_de.toByteArray();
            } catch (Exception e) {
                output = data;
                e.printStackTrace();
            } finally {
                try {
                    bos_de.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            decompresser.end();
            return output;
        }
    }

    public static byte[] decompress(InputStream is) {
        synchronized (DECOMPRESS){
            iis = new InflaterInputStream(is);
            bos_de= new ByteArrayOutputStream(1024);
            try {
                int i = 1024;
                byte[] buf = new byte[i];

                while ((i = iis.read(buf, 0, i)) > 0) {
                    bos_de.write(buf, 0, i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bos_de.toByteArray();
        }
    }
}

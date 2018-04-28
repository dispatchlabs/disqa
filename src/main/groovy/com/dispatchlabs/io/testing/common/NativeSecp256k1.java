package com.dispatchlabs.io.testing.common;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NativeSecp256k1 {

    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static final Lock readLock = reentrantReadWriteLock.readLock();
    private static ThreadLocal<ByteBuffer> nativeECDSABuffer = new ThreadLocal<ByteBuffer>();
    private static long context = -1;

    private static native byte[] secp256k1_sign_and_serialize_compact(ByteBuffer byteBuff, long context);

    private static native long secp256k1_init_context();

    public static byte[] sign(byte[] privateKey,byte[] hash){
        if(context == -1){
            System.loadLibrary("secp256k1");
            context = secp256k1_init_context();
        }
        ByteBuffer byteBuff = nativeECDSABuffer.get();
        if (byteBuff == null || byteBuff.capacity() < 64) {
            byteBuff = ByteBuffer.allocateDirect(64);
            byteBuff.order(ByteOrder.nativeOrder());
            nativeECDSABuffer.set(byteBuff);
        }
        byteBuff.rewind();
        byteBuff.put(privateKey);
        byteBuff.put(hash);

        readLock.lock();
        try {
            return secp256k1_sign_and_serialize_compact(byteBuff, context);
        } finally {
            readLock.unlock();
        }
    }
}

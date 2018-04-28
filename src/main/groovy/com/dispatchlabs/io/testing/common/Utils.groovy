package com.dispatchlabs.io.testing.common

import com.jayway.restassured.RestAssured
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.builder.ResponseSpecBuilder
import com.jayway.restassured.config.LogConfig
import com.jayway.restassured.config.SSLConfig
import com.jayway.restassured.filter.log.LogDetail
import com.jayway.restassured.http.ContentType
import com.jayway.restassured.specification.RequestSpecification

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkArgument;

public class Utils {

    /**
     * <p>
     * The regular {@link BigInteger#toByteArray()} includes the sign bit of the number and
     * might result in an extra byte addition. This method removes this extra byte.
     * </p>
     * <p>
     * Assuming only positive numbers, it's possible to discriminate if an extra byte
     * is added by checking if the first element of the array is 0 (0000_0000).
     * Due to the minimal representation provided by BigInteger, it means that the bit sign
     * is the least significant bit 0000_000<b>0</b> .
     * Otherwise the representation is not minimal.
     * For example, if the sign bit is 0000_00<b>0</b>0, then the representation is not minimal due to the rightmost zero.
     * </p>
     * @param b the integer to format into a byte array
     * @param numBytes the desired size of the resulting byte array
     * @return numBytes byte long array.
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        checkArgument(b.signum() >= 0, "b must be positive or zero");
        checkArgument(numBytes > 0, "numBytes must be positive");
        byte[] src = b.toByteArray();
        byte[] dest = new byte[numBytes];
        boolean isFirstByteOnlyForSign = src[0] == 0;
        int length = isFirstByteOnlyForSign ? src.length - 1 : src.length;
        checkArgument(length <= numBytes, "The given number does not fit in " + numBytes);
        int srcPos = isFirstByteOnlyForSign ? 1 : 0;
        int destPos = numBytes - length;
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }

    /**
     *
     * @param s
     * @return
     */
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    /**
     *
     * @param value
     * @return
     */
    public static byte[] longToBytes(long value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putLong(0, value);
        return byteBuffer.array();
    }

    public static sign(String privateKey,String hash){
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            Process proc = "${System.getenv("SIGN_EXE_DIR")}\\sign.exe ${privateKey} ${hash}".execute([], new File(System.getenv("SIGN_EXE_DIR")))
            proc.waitFor()
            return proc.text
        }
        else{
            Process proc = "${System.getenv("SIGN_EXE_DIR")}/sign ${privateKey} ${hash}".execute([], new File(System.getenv("SIGN_EXE_DIR")))
            proc.waitFor()
            return proc.text
            //return NativeSecp256k1.sign(DatatypeConverter.parseHexBinary(privateKey),DatatypeConverter.parseHexBinary(hash))
        }
    }

    public static setupRestAssured(){
        RequestSpecification requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).log(LogDetail.ALL).
                setAccept(ContentType.JSON).build()

        RestAssured.requestSpecification = requestSpecification
    }
}


package jp.f_matano44.jfloatwavio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** converter class that isn't allowed make instance. */
public final class Converter {
    private Converter() {
        throw new IllegalStateException("Converter isn't allowed to create instance.");
    }


    /**
     * Converter from byte array signal to double array signal.
     *
     * @param byteArray Signal: byte[]
     * @param nBits Encoding Bit Depth: int
     * @param isBigEndian boolean
     * @return Signal: double[]
     */
    public static final double[] byte2double (
        final byte[] byteArray, final int nBits, final boolean isBigEndian
    ) {
        final int SIGN = 1; 
        final int sampleSize = nBits / 8;
        final double nBitsMax = Math.pow(2, nBits - SIGN);

        final List<Double> doubleList = new ArrayList<>();
        for (int i = 0; i < byteArray.length; i += sampleSize) {
            // Copy array
            final byte[] temp = new byte[sampleSize];
            for (int j = 0; j < sampleSize; j++) {
                temp[j] = byteArray[i + j];
            }

            // enforce big-endian
            final byte[] bigEndian;
            if (!isBigEndian) {
                bigEndian = switchEndian(temp);
            } else {
                bigEndian = temp;
            }

            // enforce 32-bit (big-endian only)
            final byte[] byteNum = extendByteArrayTo32Bit(bigEndian, sampleSize);
            // byte[4] to int
            final int intNum = ByteBuffer.wrap(byteNum).getInt();
            // int to double (-1.0 ~ 1.0)
            final double doubleNum = ((double) intNum) / nBitsMax;
            // add List
            doubleList.add(doubleNum);
        }

        return doubleList.stream().mapToDouble(Double::doubleValue).toArray();
    }


    /**
     * Converter from byte array signal to double array signal.
     *
     * @param doubleArray Signal: double[]
     * @param nBits Encoding Bit Depth: int
     * @param isBigEndian boolean
     * @return Signal: byte[]
     */
    public static final byte[] double2byte (
        final double[] doubleArray, final int nBits, final boolean isBigEndian
    ) {
        final int SIGN = 1;
        final byte intIs4Bytes = 4;
        final int sampleSize = nBits / 8;
        final double nBitsMax = Math.pow(2, nBits - SIGN);

        final byte[] byteArray = new byte[doubleArray.length * sampleSize];
        for (int i = 0; i < doubleArray.length; i++) {
            // double (-1.0 ~ 1.0) -> int
            final int intNum = (int) (doubleArray[i] * nBitsMax);
            // int -> byte[4]
            final byte[] byte_4 = 
                ByteBuffer.allocate(4).putInt(intNum).array();
            // byte[4] -> byte[sampleSize]
            final byte[] bigEndian =
                Arrays.copyOfRange(byte_4, intIs4Bytes - sampleSize, intIs4Bytes);

            // if wanted little endian, convert to it.
            final byte[] byteNum;
            if (!isBigEndian) {
                byteNum = Converter.switchEndian(bigEndian);
            } else { // Big-endian
                byteNum = bigEndian;
            }

            // insert byte array
            System.arraycopy(
                byteNum, 0, byteArray, i * sampleSize, byteNum.length
            );
        }

        return byteArray;
    }


    private static final byte[] switchEndian(final byte[] byteArray) {
        final int tail = byteArray.length - 1;
        for (int i = 0; i < (byteArray.length / 2); i++) {
            final byte temp = byteArray[tail - i];
            byteArray[tail - i] = byteArray[i];
            byteArray[i] = temp;
        }

        return byteArray;
    }


    // example (16 bit -> 32 bit)
    // 00000000 00001111 -> 00000000 00000000 00000000 00001111
    // 10111111 11110000 -> 11111111 11111111 10111111 11110000
    private static final byte[] extendByteArrayTo32Bit(
        final byte[] array, final int sampleSize
    ) {
        final byte intIs4Bytes = 4;
        final int fillDigit = intIs4Bytes - sampleSize;

        // fillNum -> 0 or -1
        //  0(10) == 00000000(2)
        // -1(10) == 11111111(2)
        final byte fillNum = (byte) (((array[0] >> 7) & 1) * (-1));

        final byte[] ret = new byte[intIs4Bytes];
        for (int i = 0; i < fillDigit; i++) {
            ret[i] = fillNum;
        }
        for (int i = 0; i < sampleSize; i++) {
            ret[fillDigit + i] = array[i];
        }

        return ret;
    }
}

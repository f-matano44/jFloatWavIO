package jp.f_matano44.jfloatwavio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

/** Unit test for random case. */
class ConverterTestForRandom {
    private static void randomCheck(int nBits, boolean isBigEndian) {
        final byte[] expected = new byte[nBits * 100000];
        new Random(System.currentTimeMillis()).nextBytes(expected);
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }


    // ----------------------------------------
    // nBits = 8
    // ----------------------------------------
    @Test void randomCase_08bit_BigEndian() {
        final int nBits = 8;
        final boolean isBigEndian = true;
        randomCheck(nBits, isBigEndian);
    }

    @Test void randomCase_08bit_LittleEndian() {
        final int nBits = 8;
        final boolean isBigEndian = false;
        randomCheck(nBits, isBigEndian);
    }


    // ----------------------------------------
    // nBits = 16
    // ----------------------------------------
    @Test void randomCase_16bit_BigEndian() {
        final int nBits = 16;
        final boolean isBigEndian = true;
        randomCheck(nBits, isBigEndian);
    }

    @Test void randomCase_16bit_LittleEndian() {
        final int nBits = 16;
        final boolean isBigEndian = false;
        randomCheck(nBits, isBigEndian);
    }


    // ----------------------------------------
    // nBits = 24
    // ----------------------------------------
    @Test void randomCase_24bit_BigEndian() {
        final int nBits = 24;
        final boolean isBigEndian = true;
        randomCheck(nBits, isBigEndian);
    }

    @Test void randomCase_24bit_LittleEndian() {
        final int nBits = 24;
        final boolean isBigEndian = false;
        randomCheck(nBits, isBigEndian);
    }


    // ----------------------------------------
    // nBits = 32
    // ----------------------------------------
    @Test void randomCase_32bit_BigEndian() {
        final int nBits = 32;
        final boolean isBigEndian = true;
        randomCheck(nBits, isBigEndian);
    }

    @Test void randomCase_32bit_LittleEndian() {
        final int nBits = 32;
        final boolean isBigEndian = false;
        randomCheck(nBits, isBigEndian);
    }
}

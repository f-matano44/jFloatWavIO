package jp.f_matano44.jfloatwavio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

public class ConverterTestForSpecialCase {
    // ----------------------------------------
    // Zero-input
    // ----------------------------------------
    @Test void ZeroInput_BigEndian() {
        final int nBits = 32;
        final boolean isBigEndian = true;

        final byte[] expected = {0, 0, 0, 0};
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }

    @Test void ZeroInput_LittleEndian() {
        final int nBits = 32;
        final boolean isBigEndian = false;
        
        final byte[] expected = {0, 0, 0, 0};
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }


    // ----------------------------------------
    // Corner case
    // ----------------------------------------
    @Test void CornerCase_MAX_BigEndian() {
        final int nBits = 32;
        final boolean isBigEndian = true;

        final byte[] expected = 
            ByteBuffer.allocate(4).putInt(Integer.MAX_VALUE).array();
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }

    @Test void CornerCase_MAX_LittleEndian() {
        final int nBits = 32;
        final boolean isBigEndian = false;
        
        final byte[] expected = 
            ByteBuffer.allocate(4).putInt(Integer.MAX_VALUE).array();
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }

    @Test void CornerCase_MIN_BigEndian() {
        final int nBits = 32;
        final boolean isBigEndian = true;

        final byte[] expected = 
            ByteBuffer.allocate(4).putInt(Integer.MIN_VALUE).array();
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }

    @Test void CornerCase_MIN_LittleEndian() {
        final int nBits = 32;
        final boolean isBigEndian = false;
        
        final byte[] expected = 
            ByteBuffer.allocate(4).putInt(Integer.MIN_VALUE).array();
        final double[] doubleArray = Converter.byte2double(expected, nBits, isBigEndian);
        final byte[] actual = Converter.double2byte(doubleArray, nBits, isBigEndian);

        assertArrayEquals(expected, actual);
    }


    // ----------------------------------------
    // Illegal-input
    // ----------------------------------------
    @Test void IllegalInput_byte2ddouble_case1() {
        final byte[] byteArray = {0, 1};
        final int nBits = 24;
        final boolean isBigEndian = true;
        assertThrows(
            IllegalArgumentException.class,
            () -> Converter.byte2double(byteArray, nBits, isBigEndian)
        );
    }

    @Test void IllegalInput_byte2ddouble_case2() {
        final byte[] byteArray = {0, 1, 2, 3};
        final int nBits = 24;
        final boolean isBigEndian = true;
        assertThrows(
            IllegalArgumentException.class,
            () -> Converter.byte2double(byteArray, nBits, isBigEndian)
        );
    }

    @Test void IllegalInput_double2byte_case1() {
        final double[] doubleArray = {-1.0001};
        final int nBits = 32;
        final boolean isBigEndian = true;
        assertThrows(
            IllegalArgumentException.class,
            () -> Converter.double2byte(doubleArray, nBits, isBigEndian)
        );
    }

    @Test void IllegalInput_double2byte_case2() {
        final double[] doubleArray = {1.0001};
        final int nBits = 32;
        final boolean isBigEndian = true;
        assertThrows(
            IllegalArgumentException.class,
            () -> Converter.double2byte(doubleArray, nBits, isBigEndian)
        );
    }
}

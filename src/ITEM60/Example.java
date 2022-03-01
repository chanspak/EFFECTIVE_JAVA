package ITEM60;

import java.nio.ByteBuffer;

public class Example {
    public static void main(String[] args) throws Exception {
        System.out.println(1.03 - 0.42);        //0.6100000000000001 출력
        System.out.println(1.00 - 9 * 0.10);    //0.09999999999999998 출력

        double douleData = 22.22;
        float floatData = 22.22f;

        //System.out.print(Double.toHexString(douleData));
        System.out.println("========= Dobule Binary[" + douleData + "] =========");
        printByteArray(convertDoubleToByteArray(douleData));
        System.out.println("========= Float Binary[" + floatData + "] =========");
        printByteArray(convertFloatToByteArray(floatData));
    }
    
    private static byte [] convertDoubleToByteArray(double number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.putDouble(number);
        return byteBuffer.array();
    }

    private static byte [] convertFloatToByteArray(float number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Float.BYTES);
        byteBuffer.putFloat(number);
        return byteBuffer.array();
    }

    private static void printByteArray(byte [] by) {
        for(byte b: by) {
            System.out.print(String.format("Hex: %02x ", b));
            System.out.print("Binary: ");
            for (int i = 0; i < 8; i++) {
                System.out.print((((0x80 >>> i) & b) == 0 ? '0' : '1'));
            }
            System.out.println();
        }
    }
}


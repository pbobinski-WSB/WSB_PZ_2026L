package wsb.merito.pz.cw04.files;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryFileManipulation {
    public static void main(String[] args) throws IOException {
        int numberToWrite = 1234567;

        String filePath = "file/test_binary_file.bin";
        writeToBinaryFile(filePath, numberToWrite);
        System.out.println(readFromBinaryFile(filePath));
        System.out.println(readFromBinaryFileWithResource(filePath));
    }

    private static int readFromBinaryFile(String filePath) throws IOException {
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(new FileInputStream(filePath));
            return inputStream.readInt();
        } finally {
          if (inputStream != null) {
              inputStream.close();
          }
        }
    }

    private static void writeToBinaryFile(String filePath, int number) throws IOException {
        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(new FileOutputStream(filePath));
            outputStream.writeInt(number);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private static int readFromBinaryFileWithResource(String filePath) throws IOException {

        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(filePath))){
            return inputStream.readInt();
        }
    }

}

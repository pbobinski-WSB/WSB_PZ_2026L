package wsb.merito.pz.cw04.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileManipulation {
    public static void main(String[] args) throws IOException {
        int numberToWrite = 1234567;
        String filePath = "file/test_text_file.txt";
        writeToTextFile(filePath, numberToWrite);
        System.out.println(readFromTextFile(filePath));
        System.out.println(readFromTextFileWithResource(filePath));
    }

    private static int readFromTextFile(String filePath) throws IOException {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
            String number = fileReader.readLine();
            return Integer.parseInt(number);
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    private static void writeToTextFile(String filePath, int number) throws IOException {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(Integer.toString(number));
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    private static int readFromTextFileWithResource(String filePath) throws IOException {

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            return Integer.parseInt(fileReader.readLine());
        }
    }

}

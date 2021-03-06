package geektime.spring.springbucks.waiter;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TestDemo {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\outputTest.txt");

        if(!file.exists() && file.createNewFile()){
            throw new IOException();
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.flush();
        String text = "写入数据\n";
        out.write(text);
        out.flush();
        out.close();

    }

    public static void method2(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

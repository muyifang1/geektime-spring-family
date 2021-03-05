package geektime.spring.springbucks.waiter;

import java.util.Arrays;
import java.util.StringTokenizer;

public class TestDemo {

    public static void main(String[] args) {
        StringTokenizer st = new StringTokenizer("sh -c 'this is a command'");
        String [] cmdarray = new String[st.countTokens()];

        for(int i=0 ; st.hasMoreTokens(); i++){
            cmdarray[i] = st.nextToken();
        }

        System.out.println("cmdarray = " + Arrays.toString(cmdarray));
    }
}

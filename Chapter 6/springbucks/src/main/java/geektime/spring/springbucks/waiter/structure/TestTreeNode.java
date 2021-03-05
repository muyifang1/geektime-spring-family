package geektime.spring.springbucks.waiter.structure;

import java.util.ArrayList;

public class TestTreeNode {

    public static void main(String[] args) {
        Node demo = Node.builder().name("testName").type("string").minOccurs("0").maxOccurs("1").build();
        Node child = Node.builder().name("Testchild").type("string").minOccurs("0").maxOccurs("1").build();
        demo.addKids(child);
        demo.addFields(Node.builder().name("testField").build());

        System.out.println("demo = " + demo);

        System.out.println("demo.name = " + demo.getName());
        System.out.println("demo.kids = " + demo.getKids());
        System.out.println("demo.fields = " + demo.getFields());
    }
}

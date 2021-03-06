package geektime.spring.springbucks.waiter.structure;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Builder
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    private String name;

    private String type;

    private String minOccurs;

    private String maxOccurs;

    // 子节点
    private HashMap<String,Node> kids;
    // 属性
    private HashMap<String,Node> fields;

    public void addKids(Node kid){
        // 如果kids为空，则初始化一个 kids
        if(kids == null){
            kids = new HashMap<String,Node>();
        }
        // todo 这里可以判定重复，对比是否一致，来校验文档是否自洽
        kids.put(kid.name,kid);
    }

    public void addFields(Node field){
        // 如果fields为空，则初始化一个 fields
        if(fields == null){
            fields = new HashMap<String,Node>();
        }
        // todo 这里可以判定重复，对比是否一致，来校验文档是否自洽
        fields.put(field.name,field);
    }

}

package geektime.spring.springbucks.waiter.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItem {

    @XmlElement(name = "GOODS_NAME")
    private String goodsName;

    @XmlElement(name = "NUM")
    private Integer num;

}

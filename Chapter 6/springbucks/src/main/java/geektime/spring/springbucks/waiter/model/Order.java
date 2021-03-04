package geektime.spring.springbucks.waiter.model;

import geektime.spring.springbucks.waiter.util.DateAdapter;
import lombok.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "ORDER")// <1>
@XmlAccessorType(XmlAccessType.FIELD)// <1>
public class Order {

    @XmlElement(name = "ORDER_NO")// <1>
    private String orderNo;

    @XmlElement(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;

    @XmlElement(name = "CREATE_TIME")
    @XmlJavaTypeAdapter(DateAdapter.class) // <2>
    private Date createTime;

    @XmlElementWrapper(name = "ORDER_ITEMS") // <3>
    @XmlElement(name = "ORDER_ITEM")
    private List<OrderItem> orderItems;

}

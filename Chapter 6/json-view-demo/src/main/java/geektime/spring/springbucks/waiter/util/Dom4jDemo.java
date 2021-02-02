package geektime.spring.springbucks.waiter.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author hongliang.dinghl Dom4j 生成XML文档与解析XML文档
 */
public class Dom4jDemo {

    /**
     * 把xml文件转换为map形式，其中key为有值的节点名称，并以其所有的祖先节点为前缀，用
     * "."相连接。如：SubscribeServiceReq.Send_Address.Address_Info.DeviceType
     *
     * @param xmlStr
     *            xml内容
     * @return Map 转换为map返回
     */
    public static TreeMap<String, String> xml2Map(String xmlStr) throws JDOMException, IOException {
        TreeMap<String, String> rtnMap = new TreeMap<String, String>();
        SAXBuilder builder = new SAXBuilder();
        Document doc = (Document) builder.build(new StringReader(xmlStr));
        // 得到根节点
        Element root = doc.getRootElement();
        String rootName = root.getName();
        rtnMap.put("root.name", rootName);
        // 调用递归函数，得到所有最底层元素的名称和值，加入map中
        convert(root, rtnMap, rootName);
        return rtnMap;
    }

    /**
     * 递归函数，找出最下层的节点并加入到map中，由xml2Map方法调用。
     *
     * @param e
     *            xml节点，包括根节点
     * @param map
     *            目标map
     * @param lastname
     *            从根节点到上一级节点名称连接的字串
     */
    @SuppressWarnings("rawtypes")
    public static void convert(Element e, Map<String, String> map, String lastname) {
        if (e.getAttributes().size() > 0) {
            Iterator it_attr = e.getAttributes().iterator();
            while (it_attr.hasNext()) {
                Attribute attribute = (Attribute) it_attr.next();
                String attrname = attribute.getName();
                String attrvalue = e.getAttributeValue(attrname);
                // map.put( attrname, attrvalue);
                map.put(lastname + "." + attrname, attrvalue); // key 根据根节点 进行生成
            }
        }
        List children = e.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element child = (Element) it.next();
            /* String name = lastname + "." + child.getName(); */
            String name = child.getName();
            // 如果有子节点，则递归调用
            if (child.getChildren().size() > 0) {
                convert(child, map, lastname + "." + child.getName());
            } else {
                // 如果没有子节点，则把值加入map
                map.put(name, child.getText());
                // 如果该节点有属性，则把所有的属性值也加入map
                if (child.getAttributes().size() > 0) {
                    Iterator attr = child.getAttributes().iterator();
                    while (attr.hasNext()) {
                        Attribute attribute = (Attribute) attr.next();
                        String attrname = attribute.getName();
                        String attrvalue = child.getAttributeValue(attrname);
                        map.put(lastname + "." + child.getName() + "." + attrname, attrvalue);
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws JDOMException, IOException {
//        String str = "<xml><appid><a>aaaa</a><b>bbbbceshi</b></appid><attach>支付测试</attach><body>APP支付测试</body><mch_id>10000100</mch_id><nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>"
//                + "<notify_url>http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url><out_trade_no>1415659990</out_trade_no><spbill_create_ip>14.23.150.211</spbill_create_ip>"
//                + "<total_fee>1</total_fee><trade_type>APP</trade_type><sign>0CB01533B8C1EF103065174F50BCA001</sign></xml>";

//        String str ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                "<RETVAL success='true'>\n" +
//                "    <Shop>\n" +
//                "        <sid>1</sid>\n" +
//                "        <name>北京鑫和易通贸易有限公司</name>\n" +
//                "    </Shop>\n" +
//                "    <Shop>\n" +
//                "        <sid>2</sid>\n" +
//                "        <name>张家口市金九福汽车服务有限公司</name>\n" +
//                "    </Shop>\n" +
//                "</RETVAL>";
        String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?><RETVAL success='true'><Shop><sid>1</sid><name>北京鑫和易通贸易有限公司</name></Shop><Shop><sid>2</sid><name>张家口市金九福汽车服务有限公司</name></Shop></RETVAL>";
        System.out.println(xml2Map(str));
    }
}

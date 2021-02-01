package geektime.spring.springbucks.waiter.util;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XmlUtils
 *
 * @author YangQi
 */
public class XmlUtils {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map Xml2Map(String xml) throws JDOMException, IOException {
        Map map = new HashMap();
        StringReader reader = new StringReader(xml);
        SAXBuilder builder = new SAXBuilder();
        Document build = builder.build(reader);
        Element root = build.getRootElement();
        List<Element> elements = root.getChildren();
        for (Element e : elements) {
            List<Element> cs = e.getChildren();
            if (cs.isEmpty()) {
                map.put(e.getName(), e.getText());
            } else {
                map.put(e.getName(), Ele2Map(e));
            }
        }
        return map;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map Ele2Map(Element e) {
        Map map = new HashMap();
        List list = e.getChildren();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();

                if (iter.getChildren().size() > 0) {
                    Map m = Ele2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static void main(String[] args) throws JDOMException, IOException {
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//                +"<ROOT>"
//                +"<PRODUCTS>"
//                +"<PRODUCT>"
//                +"<PRODUCT_ID>1300511110031100200000101</PRODUCT_ID>"
//                +"<NAME>10000001</NAME>"
//                +"</PRODUCT>"
//                +"<PRODUCT>"
//                +"<PRODUCT_ID>1300511110031100200000102</PRODUCT_ID>"
//                +"<NAME>10000002</NAME>"
//                +"</PRODUCT>"
//                +"</PRODUCTS>"
//                +"</ROOT>";

        String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RETVAL success='true'>\n" +
                "    <Shop>\n" +
                "        <sid>1</sid>\n" +
                "        <name>北京鑫和易通贸易有限公司</name>\n" +
                "    </Shop>\n" +
                "    <Shop>\n" +
                "        <sid>2</sid>\n" +
                "        <name>张家口市金九福汽车服务有限公司</name>\n" +
                "    </Shop>\n" +
                "</RETVAL>";

        Map map = Xml2Map(xml);
        System.out.println(map);
    }

}

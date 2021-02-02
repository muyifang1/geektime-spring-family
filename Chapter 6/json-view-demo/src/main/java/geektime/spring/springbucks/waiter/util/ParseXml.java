package geektime.spring.springbucks.waiter.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

public class ParseXml {

    /**
     * xml转换成TreeMap
     * @param xml
     * @return
     */
    public static Map<String, Object> xmlToTreeMap(String xml) {
        Map<String, Object> map = new TreeMap<String, Object>();
        try {
            if (xml == null || "".equals(xml.replaceAll(" ", ""))) {
                return null;
            } else {
                Document doc = (new SAXBuilder()).build(new StringReader(xml));
                Element element = doc.getRootElement();
                if (element == null) {
                    throw new Exception("XML包格式错误:没有根元素");
                }
                List it = element.getChildren();
                int len = it.size();
                if (len == 0) {
                    throw new Exception("XML包格式错误:没有子元素");
                }
                map = new TreeMap<String, Object>();
                for (int i = 0; i < len; i++) {
                    Element subelement = (Element) it.get(i);
                    List itc = subelement.getChildren();
                    if (itc != null && itc.size() > 0) {
                        for (int n = 0; n < itc.size(); n++) {
                            Element sub = (Element) itc.get(n);
                            map.put(sub.getName().toLowerCase(), sub.getText());
                        }
                    }
                    map.put(subelement.getName().toLowerCase(), subelement.getText());
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * xml转换成HashMap
     *
     * @param xml
     * @return
     */
    public static Map<String, Object> xmlToHashMap(String xml) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (xml == null || "".equals(xml.replaceAll(" ", ""))) {
                return null;
            } else {
                Document doc = (new SAXBuilder()).build(new StringReader(xml));
                Element element = doc.getRootElement();
                if (element == null) {
                    throw new Exception("XML包格式错误:没有根元素");
                }
                List it = element.getChildren();
                int len = it.size();
                if (len == 0) {
                    throw new Exception("XML包格式错误:没有子元素");
                }
                map = new HashMap<String, Object>();
                for (int i = 0; i < len; i++) {
                    Element subelement = (Element) it.get(i);
                    List itc = subelement.getChildren();
                    if (itc != null && itc.size() > 0) {
                        for (int n = 0; n < itc.size(); n++) {
                            Element sub = (Element) itc.get(n);
                            map.put(sub.getName().toLowerCase(), sub.getText());
                        }
                    }
                    map.put(subelement.getName().toLowerCase(), subelement.getText());
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Title: parseXmlToListMap
     * @Description: xml转listmap
     * @param @param xml
     * @param @return
     * @return List<Map> 返回类型
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> parseXmlToListMap(String xml) {
        List<Map<String, Object>> argMapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            StringReader read = new StringReader(xml);
// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
// 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
// 通过输入源构造一个Document
            Document doc = (Document) sb.build(source);
            Element root = doc.getRootElement();// 指向根节点
            if(root != null){
                List<Element> es = root.getChildren();
                if (es != null && es.size() != 0) {
                    for (Element element : es) {
                        retMap.put(element.getName(), element.getText());
                    }
                }
            }
            argMapList.add(retMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return argMapList;
    }

    public static void main(String[] args) {
//        String xml = "<xml><code><![CDATA[code]]></code><msg><![CDATA[msg]]></msg><responseId>12345678</responseId></xml>";

//        String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><RETVAL success='true'><Shop><sid>1</sid><name>北京鑫和易通贸易有限公司</name></Shop><Shop><sid>2</sid><name>张家口市金九福汽车服务有限公司</name></Shop></RETVAL>";
        System.out.println(parseXmlToListMap(xml).get(0));
    }

}

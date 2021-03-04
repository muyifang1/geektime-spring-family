package geektime.spring.springbucks.waiter.util;

import geektime.spring.springbucks.waiter.model.Order;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class XML {

    public static String toXmlString(Object obj) {
        String result;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T> T parseObject(String input, Class<T> claaz) {
        Object result;
        try {
            JAXBContext context = JAXBContext.newInstance(claaz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            result = unmarshaller.unmarshal(new StringReader(input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) result;
    }

    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(new File("./src/main/resources/demoOrderXml.xml"));

            Order testOrder = new Order();
            System.out.println("testOrder.getOrderNo()+testOrder.toString() = " + testOrder.getOrderNo()+testOrder.toString());

            testOrder = XML.parseObject(doc.asXML(), Order.class);
            System.out.println("testOrder value from xml = " + testOrder.toString());

            System.out.println("doc to XML = " + doc.asXML());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}

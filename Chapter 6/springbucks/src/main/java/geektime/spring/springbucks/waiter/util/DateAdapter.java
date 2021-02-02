package geektime.spring.springbucks.waiter.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    static ThreadLocal<DateFormat> sdf ;

    static {
        sdf =new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
        };
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return sdf.get().parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return sdf.get().format(v);
    }
}

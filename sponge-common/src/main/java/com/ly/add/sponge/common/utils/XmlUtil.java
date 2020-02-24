package com.ly.add.sponge.common.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author : qqy48861
 * date : 2018/9/13.
 */
public class XmlUtil {

    public static <T> String Object2Xml(T t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JAXBContext context;
        String str = null;
        try {
            context = JAXBContext.newInstance(t.getClass());
            Marshaller marshal = context.createMarshaller();
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshal.marshal(t, baos);
            try {
                str = baos.toString("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return str;
    }

}
package com.ly.add.sponge.common.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

/**
 * @author : qqy48861
 * date : 2019/10/11.
 */
public class HttpTools {

    public static String paramConverter(Map<String, Object> map, String firstchar, Boolean urlEncode) {
        Boolean first = true;
        Set<String> keys = map.keySet();
        StringBuilder params = new StringBuilder();
        for (String key : keys) {
            if (first) {
                params.append(firstchar).append(key).append("=");
                if (urlEncode) {
                    params.append(URLEncoder.encode(map.get(key).toString()));
                } else {
                    params.append(map.get(key));
                }
                first = false;
                continue;
            }
            params.append("&").append(key).append("=");
            if (urlEncode) {
                params.append(URLEncoder.encode(map.get(key).toString()));
            } else {
                params.append(map.get(key));
            }
        }
        return params.toString();
    }

    public static String paramConverter(Map<String, Object> map, String firstchar) {
        return paramConverter(map, firstchar, true);
    }

    /**
     * author: qqy48861
     * HttpResponse 2 String
     */
    public static String read(HttpResponse response, Charset charSet) throws ParseException, IOException {
        HttpEntity httpEntity = response.getEntity();
        String stringEntity = toString(httpEntity);
        return new String(stringEntity.getBytes(Consts.ISO_8859_1), charSet);
    }

    /**
     * author: qqy48861
     * HttpEntity 2 String
     */
    private static String toString(HttpEntity entity) throws IOException, ParseException {
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return "";
        }
        try {
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                    "HTTP entity too large to be buffered in memory");
            int i = (int) entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = Consts.ISO_8859_1;

            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        } finally {
            instream.close();
        }
    }

}
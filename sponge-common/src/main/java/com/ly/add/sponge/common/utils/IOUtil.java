package com.ly.add.sponge.common.utils;

import java.io.*;
import java.util.Iterator;

public class IOUtil {
    public static Iterable<String> lineSeq(final String path) throws FileNotFoundException {
        return lineSeq(path, "utf-8");
    }
    
	public static Iterable<String> lineSeq(final String path, final String encode) throws FileNotFoundException {

        try {
            return new Iterable<String>() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                                          path), encode));

                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        private String content;

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }

                        public String next() {
                            return content;
                        }

                        public boolean hasNext() {
                            try {
                                content = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (content != null) {
                                return true;
                            }

                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return false;
                        }
                    };
                }

            };
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
}

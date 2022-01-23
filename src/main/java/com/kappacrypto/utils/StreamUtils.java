package com.kappacrypto.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class StreamUtils {

    public static String convertBufferToString(InputStream stream) throws IOException {
        BufferedReader bR = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        StringBuffer streamContent = new StringBuffer();
        String inputLine;

        while ((inputLine = bR.readLine()) != null) {
            streamContent.append(inputLine);
        }

        sb.append(streamContent);
        bR.close();

        return sb.toString();
    }

    public static String convertGzipResponseToString(InputStream stream) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(stream);
        BufferedReader bR = new BufferedReader(new InputStreamReader(gzip));
        StringBuilder sb = new StringBuilder();
        StringBuffer streamContent = new StringBuffer();
        String inputLine;

        while ((inputLine = bR.readLine()) != null) {
            streamContent.append(inputLine);
        }

        sb.append(streamContent);
        bR.close();

        return sb.toString();
    }
}

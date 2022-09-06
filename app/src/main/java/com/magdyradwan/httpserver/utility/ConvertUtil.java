package com.magdyradwan.httpserver.utility;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConvertUtil {

//    public String convertInputStreamToString(InputStream in) throws IOException {
//        InputStreamReader reader = new InputStreamReader(in);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        StringBuilder stringBuilder = new StringBuilder();
//        String line = bufferedReader.readLine();
//        while(line != null) {
//            stringBuilder.append(line);
//            line = bufferedReader.readLine();
//        }
//        bufferedReader.close();
//        reader.close();
//        return stringBuilder.toString();
//    }

    public String convertInputStreamToString(InputStream in) throws IOException {
        DataInputStream reader = new DataInputStream(in);
        String result = reader.readUTF();
        reader.close();
        return result;
    }
}

package io.mateu.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

    public static String run(String cmd) throws IOException {

        System.out.println("ejecutando " + cmd);

        Process p = Runtime.getRuntime().exec( cmd );

        StringBuffer inBuffer = new StringBuffer();
        InputStream inStream = p.getInputStream();
        new InputStreamHandler( inBuffer, inStream );

        //StringBuffer errBuffer = new StringBuffer();
        InputStream errStream = p.getErrorStream();
        new InputStreamHandler( inBuffer , errStream );

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return inBuffer.toString();
    }
}

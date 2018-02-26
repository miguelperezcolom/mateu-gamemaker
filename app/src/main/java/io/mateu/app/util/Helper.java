package io.mateu.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {

    public static String run(String cmd) throws IOException {

        System.out.println("ejecutando " + cmd);

        Process p = Runtime.getRuntime().exec( cmd );

        StringBuffer sb = new StringBuffer();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(p.getInputStream()) );
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            sb.append(line);
        }
        in.close();

        return sb.toString();
    }
}

package com.whatgameapps.firefly;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ReadCollinCad {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://collincad.org/propertysearch?prop=19991&year=2017\n");
        URLConnection yc = url.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //   if(inputLine.contains("Total Improvement Main Area"))
                System.out.println(inputLine);
            }
        }
    }
}

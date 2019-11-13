/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.myapps.easybusiness;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;


import org.xml.sax.helpers.ParserAdapter;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public class StarterApplication extends Application {
    ParseObject object;
    String ip = "";

    @Override
    public void onCreate() {
        super.onCreate();
      //  parseSettingAndInit();
      parseSettingAndInitLocal();
    }

    public void createParseObject(String objectname) {
        object = new ParseObject(objectname);
        object.put("score", 10);
        object.put("name", "Adnan");

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {


                } else {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void parseSettingAndInit() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("671a046e488d2cb5657ab3c77e82b0d29a40942c")
                .clientKey("48c1b128e39ad2a90bc833ce744aae5db2ccd626")
                .server("http://18.218.220.100:80/parse/")
                .build()
        );

        // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }

    public void parseSettingAndInitLocal() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("adnanApp")
                .clientKey("2211")
                //.server("http://10.0.2.2:1337/parse/") // for emulator
                .server("http://192.168.0.101:1337/parse/") // for real android device from house
                //.server("http://172.22.202.195:1337/parse/") // for FH Dortmund
                .build()
        );

        // ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }


    public void readFromParseServer() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Score");
        parseQuery.getInBackground("dubHda9E3Q", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    // no errors
                } else {
                    // ERROR !!
                }
            }
        });
    }

    public void updateObjectInParseServer() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Score");
        // using the user id "Wk7AlKkYma" will give us only the object with this id back
        parseQuery.getInBackground("dubHda9E3Q", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // no errors
                    object.put("score", 20);
                    object.saveInBackground();
                } else {
                    // ERROR !!
                }
            }
        });
    }

    public String getAllObjectInParseServer() {
        final String[] result = {""};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ExampleObject");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            result[0] += object.getString("myString") + " , " + object.getString("myNumber") + "\n";
                        }
                    }
                }
            }
        });
        return result[0];
    }

    public void searchNachBestimmteResult() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ExampleObject");
        query.whereEqualTo("myString", "yasin");
        query.setLimit(2);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        String result = "";
                        for (ParseObject object : objects) {
                            result += object.getString("myString") + " , " + object.getString("myNumber") + "\n";
                        }
                    }
                }
            }
        });
    }

    public void createUserAndSingUp() {
        ParseUser user = new ParseUser();
        user.setUsername("Adnan");
        user.setPassword("222111");
        user.setEmail("adnan.nassar90@gmail.com");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                }
            }
        });

    }

    public void logIn() {
        ParseUser.logInInBackground("Adnan", "1122", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                } else {
                }
            }
        });
    }

    public void logOut() {
        ParseUser.getCurrentUser().logOut();
    }

    public void isLogedIn() {
        if (ParseUser.getCurrentUser() != null) {
            Log.i("Current User ", "USer is Logged in");
        } else {
            Log.i("Current User ", "USer is not Logged in, Logged Out");
        }
    }

    public static class Utils {

        /**
         * Convert byte array to hex string
         *
         * @param bytes toConvert
         * @return hexValue
         */
        public static String bytesToHex(byte[] bytes) {
            StringBuilder sbuf = new StringBuilder();
            for (int idx = 0; idx < bytes.length; idx++) {
                int intVal = bytes[idx] & 0xff;
                if (intVal < 0x10) sbuf.append("0");
                sbuf.append(Integer.toHexString(intVal).toUpperCase());
            }
            return sbuf.toString();
        }

        /**
         * Get utf8 byte array.
         *
         * @param str which to be converted
         * @return array of NULL if error was found
         */
        public static byte[] getUTF8Bytes(String str) {
            try {
                return str.getBytes("UTF-8");
            } catch (Exception ex) {
                return null;
            }
        }

        /**
         * Load UTF8withBOM or any ansi text file.
         *
         * @param filename which to be converted to string
         * @return String value of File
         * @throws java.io.IOException if error occurs
         */
        public static String loadFileAsString(String filename) throws java.io.IOException {
            final int BUFLEN = 1024;
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
                byte[] bytes = new byte[BUFLEN];
                boolean isUTF8 = false;
                int read, count = 0;
                while ((read = is.read(bytes)) != -1) {
                    if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                        isUTF8 = true;
                        baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                    } else {
                        baos.write(bytes, 0, read);
                    }
                    count += read;
                }
                return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
            } finally {
                try {
                    is.close();
                } catch (Exception ignored) {
                }
            }
        }

        /**
         * Returns MAC address of the given interface name.
         *
         * @param interfaceName eth0, wlan0 or NULL=use first interface
         * @return mac address or empty string
         */
        public static String getMACAddress(String interfaceName) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    if (interfaceName != null) {
                        if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                    }
                    byte[] mac = intf.getHardwareAddress();
                    if (mac == null) return "";
                    StringBuilder buf = new StringBuilder();
                    for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                    if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                    return buf.toString();
                }
            } catch (Exception ignored) {
            } // for now eat exceptions
            return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
        }

        /**
         * Get IP address from first non-localhost interface
         *
         * @param useIPv4 true=return ipv4, false=return ipv6
         * @return address or empty string
         */
        public static String getIPAddress(boolean useIPv4) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            // boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            boolean isIPv4 = sAddr.indexOf(':') < 0;

                            if (useIPv4) {
                                if (isIPv4)
                                    return sAddr;
                            } else {
                                if (!isIPv4) {
                                    int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                    return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            } // for now eat exceptions
            return "";
        }

        private static void refereshArp(Context ctx) {
            //IP aquisition from http://stackoverflow.com/a/6071963/1896516
            WifiManager wm = (WifiManager) ctx.getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            String[] parts = ip.split(".");
            String subnet = parts[0] + "." + parts[1] + "." + parts[2] + ".";
            Runtime rt = Runtime.getRuntime();
            for (int i = 0; i < 256; i++) {
                try {
                    rt.exec("ping -c1 " + subnet + i);
                } catch (IOException e) {
                    continue;
                }
            }

        }

    }

}



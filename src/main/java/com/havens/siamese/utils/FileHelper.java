package com.havens.siamese.utils;

import com.havens.siamese.server.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

/**
 * Created by havens on 15-8-11.
 */
public class FileHelper {

    /**
     * get file input stream in classpath
     *
     * @param filename file name
     * @return input stream
     */
    public static InputStream getInputStream(String filename) {
        ClassLoader cL = Thread.currentThread().getContextClassLoader();
        if (cL == null) {
            cL = FileHelper.class.getClassLoader();
        }
        return cL.getResourceAsStream(filename);
    }


    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        try {
            InputStream in = getInputStream(filename);
            if (in != null)
                properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Properties getPropertiesByAbsFile(String filename) {
        Properties properties = new Properties();
        try {
            InputStream in = new FileInputStream(filename);
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * @param filename   file name
     * @param validating validate
     * @return Document
     * @throws Exception
     */
    private static Document parseXML(String filename,
                                     boolean validating) throws Exception {
        // Create a builder factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setExpandEntityReferences(false);
        InputStream in = getInputStream(filename);
        return factory.newDocumentBuilder().parse(in);
    }

    public static Map<String, Class> getServices() {
        Map<String, Class> services = new HashMap<String, Class>();
        try {
            Document dd = parseXML(Server.SERVICES_FILE, false);

            NodeList list = dd.getElementsByTagName("service");
            for (int i = 0; i < list.getLength(); i++) {
                Element tE = (Element) list.item(i);
                String code = tE.getAttribute("code");
                String clazzName = tE.getTextContent();
                try {
                    Class clazz = Class.forName(clazzName);
                    services.put(code, clazz);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return services;
    }

    public static Set<Class> getInstances() {
        Set<Class> services = new HashSet<Class>();
        try {
            Document dd = parseXML(Server.SERVICES_FILE, false);

            NodeList list = dd.getElementsByTagName("instance");
            for (int i = 0; i < list.getLength(); i++) {
                Element tE = (Element) list.item(i);
                String clazzName = tE.getTextContent();
                Class clazz = Class.forName(clazzName);
                services.add(clazz);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return services;
    }

    public static String getContent(String filename) {
        String content = "";
        try {
            StringBuilder sb = new StringBuilder();
            InputStream in = getInputStream(filename);
            if (in == null) {
                return content;
            }
            BufferedReader input = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString();
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
        return content;
    }

    public static String getContentByAbsFile(String filename) {
        String content = "";
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return content;
            }
            StringBuilder sb = new StringBuilder();
            InputStream in = new FileInputStream(filename);
            BufferedReader input = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    public static Set<String> getContentLine(String filename) {
        Set<String> content = new HashSet<String>();
        try {
            InputStream in = getInputStream(filename);
            BufferedReader input = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                content.add(line.trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }


    public static void copyFile(File inFile, File outFile) {
        try {

            if (inFile.exists()) {
                InputStream inStream = new FileInputStream(inFile);
                FileOutputStream fs = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteread;
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.flush();
                fs.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
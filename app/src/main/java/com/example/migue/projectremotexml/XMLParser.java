package com.example.migue.projectremotexml;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by admin on 10/12/2015.
 */
public class XMLParser {

    public String getXmlFromUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            return convertStreamToString(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();

            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("erro: ", e.getMessage());
            return null;
        }catch (SAXException e) {
            Log.e("erro: ", e.getMessage());
            return null;
        } catch(IOException e){
            Log.e("erro: ", e.getMessage());
            return null;
        }
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null){
            if (elem.hasChildNodes()) {
                for ( child = elem.getFirstChild(); child != null;
                      child = child.getNextSibling()){
                    if (child.getNodeType() == Node.TEXT_NODE ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public String convertStreamToString(InputStream is)
            throws IOException {

        if (is != null ) {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader( new InputStreamReader(is, "UTF-8") );
                int n;
                while ((n= reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);

                }
            }finally { is.close(); }
            return writer.toString();

        } else { return ""; }


    }
}

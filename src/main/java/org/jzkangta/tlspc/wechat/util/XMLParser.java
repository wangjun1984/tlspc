package org.jzkangta.tlspc.wechat.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XMLParser {

	public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream tInputStringStream = null;
        if (xmlString != null && !xmlString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(xmlString.getBytes());
        }
        
        Document document = null;
        try{
        	document = builder.parse(tInputStringStream);
        } catch(Exception e) {
        	e.printStackTrace();
        }

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }

	
	public static String objectToXml(Object object) {
		xstream.alias("xml", object.getClass());
		return xstream.toXML(object);
	}
	
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = true;
				
				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}
				
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}

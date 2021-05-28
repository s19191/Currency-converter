package calculator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

public class XmlParser {
    String fileOutput;
    URL inputFile;

    public XmlParser(String fileOutput, URL inputFile) throws IOException {
        this.fileOutput = fileOutput;
        this.inputFile = inputFile;
        downloadFile();
    }

    public void downloadFile() throws IOException {
        InputStream in = inputFile.openStream();
        ReadableByteChannel rbc = Channels.newChannel(in);
        FileOutputStream fos = new FileOutputStream(fileOutput);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public Map<String, Double> getCurrencyMap() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(fileOutput));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Cube");

        Map<String, Double> currencyRates = new HashMap<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (!eElement.getAttribute("currency").isEmpty()) {
                    currencyRates.put(eElement.getAttribute("currency"), Double.parseDouble(eElement.getAttribute("rate")));
                }
            }
        }
        return currencyRates;
    }
}
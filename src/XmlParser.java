import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParser {
    String fileOutput;
    URL inputFile;

    public XmlParser(String fileOutput, URL inputFile) {
        this.fileOutput = fileOutput;
        this.inputFile = inputFile;
        try {
            downloadFile(inputFile, fileOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(URL url, String outputFileName) throws IOException {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    public Map<String, Double> getCurrencyMap() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileOutput));
        Map<String, Double> currencyRates = new HashMap<>();
        for (String line : lines) {
            Pattern p01 = Pattern.compile("<Cube currency='(\\w{3})' rate='(\\d+\\.\\d+)'/>");
            Matcher m01 = p01.matcher(line.trim());
            if (m01.matches()) {
                currencyRates.put(m01.group(1), Double.parseDouble(m01.group(2)));
            }
        }
        return currencyRates;
    }

    public Map<String, Double> getCurrencyMap2() throws IOException, ParserConfigurationException, SAXException {
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
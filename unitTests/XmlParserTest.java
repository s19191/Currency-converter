import calculator.XmlParser;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class XmlParserTest {

    @Test()
    public void creationCorrect() {
        try {
            XmlParser xmlParser = new XmlParser("exchangeRate.xml", new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-dailye.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void creationFiled() throws IOException {
        XmlParser xmlParser = new XmlParser("exchangeRate.xml", new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-dailyeee.xml"));
    }

    @Test()
    public void getCurrencyMap_Correct() {
        try {
            XmlParser xmlParser = new XmlParser("exchangeRate.xml", new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-dailye.xml"));
            xmlParser.getCurrencyMap();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}

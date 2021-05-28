import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

public class CurrencyConverter {
    public static void main(String[] args) {
        CurrencyCalculator currencyCalculator = new CurrencyCalculator();
        try {
            XmlParser xmlParser = new XmlParser("exchangeRate.txt", new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"));
            currencyCalculator.showGui(xmlParser.getCurrencyMap2());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
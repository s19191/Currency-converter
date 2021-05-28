import java.io.IOException;
import java.net.URL;

public class CurrencyConverter {
    public static void main(String[] args) {
        CurrencyCalculator currencyCalculator = new CurrencyCalculator();
        try {
            XmlParser xmlParser = new XmlParser("exchangeRate.txt", new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"));
            currencyCalculator.showGui(xmlParser.getCurrencyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
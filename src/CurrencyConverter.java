import calculator.CurrencyCalculator;
import calculator.XmlParser;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class CurrencyConverter {
    public static void main(String[] args) {
        CurrencyCalculator currencyCalculator = new CurrencyCalculator();
        try {
            XmlParser xmlParser = new XmlParser("exchangeRate.xml", new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"));
            try {
                currencyCalculator.showGui(xmlParser.getCurrencyMap());
            } catch(Exception exc) {
                JOptionPane.showMessageDialog(null, "Creation failed, " + exc);
            }
        } catch (IOException exe) {
            exe.printStackTrace();
        }
    }
}
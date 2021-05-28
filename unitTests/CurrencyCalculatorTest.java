import calculator.CurrencyCalculator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CurrencyCalculatorTest {

    @Test()
    public void showGuiCorrect() {
        Map<String, Double> mockMap = new HashMap<>();
        mockMap.put("PLN", 2.0);
        CurrencyCalculator currencyCalculator = new CurrencyCalculator();
        currencyCalculator.showGui(mockMap);
    }

    @Test(expected = NullPointerException.class)
    public void showGuiFiled() {
        CurrencyCalculator currencyCalculator = new CurrencyCalculator();
        currencyCalculator.showGui(null);
    }
}

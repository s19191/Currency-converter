import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

public class CurrencyConverter {
    public static void main(String[] args) {
        CurrencyConverter currencyConverter = new CurrencyConverter();
        try {
            currencyConverter.showGui();
        } catch(Exception exc) {
            JOptionPane.showMessageDialog(null, "Creation failed, " + exc);
        }
    }

    public void downloadFile(URL url, String outputFileName) throws IOException
    {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }


    public Map<String, Double> getCurrencyMap() throws IOException {
        downloadFile(new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"),"exchangeRate.txt");
        List<String> lines = Files.readAllLines(Paths.get("exchangeRate.txt"));
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
        downloadFile(new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"),"exchangeRate.txt");
        Map<String, Double> currencyRates = new HashMap<>();
        File inputFile = new File("exchangeRate.txt");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("Cube");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (!eElement.getAttribute("currency").isEmpty()) {
                    currencyRates.put(eElement.getAttribute("currency"),Double.parseDouble(eElement.getAttribute("rate")));
                }
            }
        }
        return currencyRates;
    }

    public Double calculate(double currency, double howMany) {
        return currency * howMany;
    }

    public void showGui() {
        SwingUtilities.invokeLater(() -> {
            JFrame jf = new JFrame("Currency converter");
            jf.setPreferredSize(new Dimension(1080,720));

            DefaultListModel<String> l1 = new DefaultListModel<>();
            Map<String, Double> currencyMap = null;
            try {
                currencyMap = getCurrencyMap();
                for (String s: currencyMap.keySet()) {
                    l1.addElement(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JList currencyJList = new JList(l1);
            currencyJList.setDragEnabled(false);
            JPanel currencyJPanel = new JPanel();
            currencyJPanel.setLayout(new GridLayout(0,1));
            currencyJPanel.add(currencyJList);

            JLabel resultJLabel = new JLabel();
            resultJLabel.setHorizontalAlignment(JLabel.CENTER);

            JLabel enterJLabel = new JLabel("Enter the amount in EUR:");

            JLabel incorrectValueJLabel = new JLabel();
            incorrectValueJLabel.setForeground(Color.RED);

            JButton calculateButton = new JButton("Calculate");
            calculateButton.setEnabled(false);

            JTextField numberJTextField = new JTextField();
            numberJTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        Double d = Double.parseDouble(numberJTextField.getText().replace(",", "."));
                        incorrectValueJLabel.setText("");
                        calculateButton.setEnabled(true);
                    } catch (NumberFormatException exception) {
                        incorrectValueJLabel.setText("Incorrect value, it has to be number!");
                        calculateButton.setEnabled(false);
                    }
                }
            });

            Map<String, Double> finalCurrencyMap = currencyMap;
            calculateButton.addActionListener(e -> {
                if (currencyJList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have to choose currency!");
                } else {
                    resultJLabel.setText(calculate(finalCurrencyMap.get((String) currencyJList.getSelectedValue()), Double.parseDouble(numberJTextField.getText().replace(",","."))) + " " + currencyJList.getSelectedValue());
                }
            });

            JPanel buttonPanel = new JPanel(new GridLayout(0,4));
            buttonPanel.add(enterJLabel);
            buttonPanel.add(numberJTextField);
            buttonPanel.add(calculateButton);
            buttonPanel.add(resultJLabel);
            buttonPanel.add(incorrectValueJLabel);

            jf.setLayout(new BorderLayout());
            jf.add(currencyJPanel,BorderLayout.CENTER);
            jf.add(buttonPanel, BorderLayout.SOUTH);
            jf.pack();
            jf.setLocationRelativeTo(null);
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jf.setVisible(true);
        });
    }
}
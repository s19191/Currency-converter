import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class CurrencyCalculator {

    public void showGui(Map<String, Double> currencyRates) throws NullPointerException {
        if (currencyRates == null) throw new NullPointerException();
        SwingUtilities.invokeLater(() -> {
            JFrame jf = new JFrame("Currency converter");
            jf.setPreferredSize(new Dimension(1080,720));

            DefaultListModel<String> l1 = new DefaultListModel<>();
            for (String s: currencyRates.keySet()) {
                l1.addElement(s);
            }
            JList currencyJList = new JList(l1);
            currencyJList.setDragEnabled(false);
            currencyJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

            calculateButton.addActionListener(e -> {
                if (currencyJList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "You have to choose currency!");
                } else {
                    resultJLabel.setText(calculate(
                            currencyRates.get((String) currencyJList.getSelectedValue()),
                            Double.parseDouble(numberJTextField.getText().replace(",","."))) + " " + currencyJList.getSelectedValue()
                    );
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

    public Double calculate(double currency, double howMany) {
        return currency * howMany;
    }
}

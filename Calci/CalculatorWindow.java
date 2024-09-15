import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculatorWindow extends JFrame {
    private JTextField textField;
    private JPanel panel;
    private Font displayFont = new Font("Arial", Font.BOLD, 24); // Increased font size for display
    private Font buttonFont = new Font("Arial", Font.PLAIN, 16); // Reduced font size for buttons

    public CalculatorWindow() {
        super("Calculator");
        setLayout(new BorderLayout());

        textField = new JTextField(20);
        textField.setFont(displayFont);
        textField.setHorizontalAlignment(SwingConstants.RIGHT); // Align text to the right
        add(textField, BorderLayout.NORTH);

        panel = new JPanel(new GridLayout(5, 4, 5, 5)); // Updated to 5 rows to include AC button
        add(panel, BorderLayout.CENTER);

        String[] buttons = {"AC", "", "", "",
                            "7", "8", "9", "+",
                            "4", "5", "6", "-",
                            "1", "2", "3", "*",
                            "0", ".", "=", "/"};

        for (String button : buttons) {
            if (!button.isEmpty()) {
                JButton b = new JButton(button);
                b.setFont(buttonFont);
                b.addActionListener(new ButtonListener());
                panel.add(b);
            } else {
                panel.add(new JLabel()); // Adding empty labels for spacing
            }
        }

        setSize(300, 400); // Increased size for the larger display
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("AC")) {
                textField.setText("0");
            } else if (command.equals("=")) {
                try {
                    textField.setText(String.valueOf(eval(textField.getText())));
                } catch (Exception ex) {
                    textField.setText("Error");
                }
            } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
                textField.setText(textField.getText() + command);
            } else {
                if (textField.getText().equals("0")) {
                    textField.setText(command); // Replace initial "0" with the number
                } else {
                    textField.setText(textField.getText() + command);
                }
            }
        }

        private double eval(String expression) {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('/')) x /= parseFactor(); // division
                        else return x;
                    }
                }

                double parseFactor() {
                    double x;
                    int startPos = this.pos;
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else { // numbers
                        int startPos2 = this.pos;
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos2, this.pos));
                    }

                    return x;
                }
            }.parse();
        }
    }

    public static void main(String[] args) {
        new CalculatorWindow();
    }
}

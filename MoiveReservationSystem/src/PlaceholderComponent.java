import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderComponent {
    public static JComponent createPlaceholderTextComponent(String placeholder, boolean isTextArea) {
        JComponent component = isTextArea ? new JTextArea(placeholder) : new JTextField(placeholder);

        component.setForeground(Color.GRAY);

        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(Color.BLACK);
                    }
                } else if (component instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) component;
                    if (textArea.getText().equals(placeholder)) {
                        textArea.setText("");
                        textArea.setForeground(Color.BLACK);
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    if (textField.getText().isEmpty()) {
                        textField.setText(placeholder);
                        textField.setForeground(Color.GRAY);
                    }
                } else if (component instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) component;
                    if (textArea.getText().isEmpty()) {
                        textArea.setText(placeholder);
                        textArea.setForeground(Color.GRAY);
                    }
                }
            }
        });

        return component;
    }

    public static Component getComponent(Container container, int index) {
        Component[] components = container.getComponents();
        if (index < 0 || index >= components.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return components[index];
    }
}

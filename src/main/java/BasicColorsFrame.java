import javax.swing.*;
import java.awt.*;

public abstract class BasicColorsFrame extends JFrame {
    protected Color buttonColor = new Color(212, 143, 93);
    protected Color buttonHoverColor = new Color(245, 179, 132);
    protected Color fontColor = new Color(218, 217, 223);
    protected Color menuBackgroundColor = new Color(36, 36, 45);
    protected Color graphBackgroundColor = new Color(176, 177, 190);
    protected Font labelFont = new Font("Lato", Font.PLAIN, 16);
    protected Font buttonFont = new Font("Lato", Font.PLAIN, 13);

    protected void setLabelProperties(JLabel label) {
        label.setForeground(fontColor);
        label.setFont(labelFont);
    }

    protected void setButtonProperties(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonHoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor);
            }
        });

        button.setBackground(buttonColor);
        button.setForeground(Color.white);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
}
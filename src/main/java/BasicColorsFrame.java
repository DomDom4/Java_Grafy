import javax.swing.*;
import java.awt.*;

public abstract class BasicColorsFrame extends JFrame {
    /**Kolor przycisków w aplikacji*/
    protected Color buttonColor = new Color(212, 143, 93);
    /**Kolor przycisków w aplikacji po najechaniu na nie*/
    protected Color buttonHoverColor = new Color(245, 179, 132);
    /**Kolor czcionki w aplikacji*/
    protected Color fontColor = new Color(218, 217, 223);
    /**Kolor tła menu w aplikacji*/
    protected Color menuBackgroundColor = new Color(36, 36, 45);
    /**Kolor tła grafu we aplikacji*/
    protected Color graphBackgroundColor = new Color(176, 177, 190);
    /**Czcionka etykiet aplikacji*/
    protected Font labelFont = new Font("Lato", Font.PLAIN, 16);
    /**Czcionka przycisków aplikacji*/
    protected Font buttonFont = new Font("Lato", Font.PLAIN, 13);

    /**
     * Ustawia parametry podanego obiektu JLabel tak aby były spójne z wyglądem całego programu.
     * @param label obiekt, którego właściwości trzeba zmienić
     */
    protected void setLabelProperties(JLabel label) {
        label.setForeground(fontColor);
        label.setFont(labelFont);
    }

    /**
     * Ustawia parametry podanego obiektu JButton tak aby były spójne z wyglądem całego programu.
     * @param button guzik, którego właściwości trzeba zmienić
     */
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

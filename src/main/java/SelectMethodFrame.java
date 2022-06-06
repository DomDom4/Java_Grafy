import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectMethodFrame extends BasicColorsFrame implements ActionListener {
    private static final int BUTTON_PANEL_HEIGHT = 47;
    private static final int LABEL_PANEL_HEIGHT = 37;
    private static final int DEFAULT_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 121;
    private JButton readParams;
    private JButton readFile;

    public SelectMethodFrame() {
        JPanel buttonPanel = new JPanel();
        JPanel labelPanel = new JPanel();
        JLabel label = new JLabel("Select a method of creating a graph");

        labelPanel.setBounds(0, 0, DEFAULT_WIDTH, LABEL_PANEL_HEIGHT);
        labelPanel.setBackground(menuBackgroundColor);

        buttonPanel.setBounds(0, LABEL_PANEL_HEIGHT, DEFAULT_WIDTH, BUTTON_PANEL_HEIGHT);
        buttonPanel.setBackground(menuBackgroundColor);

        setLabelProperties(label);

        readParams = new JButton("Generate");
        setButtonProperties(readParams);
        readParams.addActionListener(this);

        readFile = new JButton("From file");
        setButtonProperties(readFile);
        readFile.addActionListener(this);

        labelPanel.add(label);
        buttonPanel.add(readFile);
        buttonPanel.add(readParams);

        this.add(labelPanel);
        this.add(buttonPanel);
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(DEFAULT_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readParams) {
            this.dispose();
            new ParamFrame();
        } else if (e.getSource() == readFile) {
            this.dispose();
            new FileFrame();
        }
    }
}

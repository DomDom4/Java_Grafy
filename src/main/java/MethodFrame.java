import javax.swing.*;
import java.awt.*;

public abstract class MethodFrame extends BasicColorsFrame {
    private static final int MENU_HEIGHT = 37;
    private static final int BACK_PANEL_HEIGHT = 37;
    private static final int WINDOW_SIZE_NO_GRAPH = 111;
    protected JPanel menu;
    protected JPanel backPanel;
    protected GraphPanel graphPanel;
    protected JButton delete;
    protected JButton back;
    protected JButton integrity;
    protected Graph graph;
    protected JFileChooser actionFile;

    public MethodFrame(int width) {
        back = new JButton("<-Back");
        setButtonProperties(back);
        back.setAlignmentX(0);

        integrity = new JButton("BFS");
        setButtonProperties(integrity);

        backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBounds(0, 0, width, BACK_PANEL_HEIGHT);
        backPanel.setBackground(menuBackgroundColor);
        backPanel.add(back);

        menu = new JPanel();
        menu.setBounds(0, BACK_PANEL_HEIGHT, width, MENU_HEIGHT);
        menu.setBackground(menuBackgroundColor);
        menu.setForeground(fontColor);

        this.add(backPanel);
        this.add(menu);
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(width, WINDOW_SIZE_NO_GRAPH);
        this.setLocationRelativeTo(null);

        actionFile = new JFileChooser();
    }

    protected void deleteGraph(int width) {
        menu.remove(delete);
        menu.setSize(width, MENU_HEIGHT);
        menu.remove(integrity);

        backPanel.setSize(width, BACK_PANEL_HEIGHT);

        this.remove(graphPanel);
        this.setSize(width, WINDOW_SIZE_NO_GRAPH);
        this.setLocationRelativeTo(null);
    }

}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphFrame extends JFrame implements ActionListener {
    private JButton readParams;
    private JButton readFile;
    private JButton generate;
    private JButton save;
    private JButton delete;
    private JButton open;
    private JTextField width;
    private JTextField length;
    private JTextField lower;
    private JTextField upper;
    private JTextField outFile;
    private JTextField inFile;
    private JPanel menu;
    private JPanel graphPanel;

    Graph graph;

    public GraphFrame(String title) {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
    }

    public void init() {
        menu = new JPanel();
        menu.setBounds(0, 0, 300, 75);
        menu.setBackground(Color.ORANGE);

        readParams = new JButton("Generate");
        readParams.addActionListener(this);

        readFile = new JButton("From file");
        readFile.addActionListener(this);

        menu.add(readFile);
        menu.add(readParams);

        this.setSize(300, 75);
        this.setVisible(true);
        this.add(menu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readParams) {
            menu.remove(readParams);
            menu.remove(readFile);
            getParameters();
        } else if (e.getSource() == readFile) {
            menu.remove(readParams);
            menu.remove(readFile);
            getFile();
        } else if (e.getSource() == generate) {
            generateChosen();
        } else if (e.getSource() == open) {
            menu.remove(open);
            menu.remove(inFile);
            fileChosen();
        } else if (e.getSource() == save) {
            graph.printGraphToFile(outFile.getText());
        } else if (e.getSource() == delete) {

        }
    }

    private void getParameters() {
        generate = new JButton("Generate");
        save = new JButton("Save");
        delete = new JButton("Delete");
        width = new JTextField();
        length = new JTextField();
        upper = new JTextField();
        lower = new JTextField();

        width.setText("3");
        width.setPreferredSize(new Dimension(100, 25));
        length.setText("3");
        length.setPreferredSize(new Dimension(100, 25));
        lower.setText("0");
        lower.setPreferredSize(new Dimension(100, 25));
        upper.setText("5");
        upper.setPreferredSize(new Dimension(100, 25));
        save.setEnabled(false);
        delete.setEnabled(false);

        generate.addActionListener(this);
        save.addActionListener(this);
        delete.addActionListener(this);

        menu.setBounds(0, 0, 900, 37);
        menu.add(width);
        menu.add(length);
        menu.add(lower);
        menu.add(upper);
        menu.add(generate);
        menu.add(save);
        menu.add(delete);

        this.setSize(900, 74);
        this.setVisible(true);
    }

    private void getFile() {
        inFile = new JTextField();
        open = new JButton("Open");
        open.addActionListener(this);

        this.add(inFile);
        this.add(open);
        this.pack();
        this.setVisible(true);
    }

    private void generateChosen() {
        graphPanel = new JPanel();
        graphPanel.setBounds(0, 37, 900, 500);
        graphPanel.setBackground(Color.PINK);
        save.setEnabled(true);
        delete.setEnabled(true);

        graph = new Graph(
                Integer.parseInt(width.getText()),
                Integer.parseInt(length.getText()),
                Double.parseDouble(upper.getText()),
                Double.parseDouble(lower.getText())
        );

        this.add(graphPanel);
        this.setSize(900, 574);
    }

    private void fileChosen() {
    }

}

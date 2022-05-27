import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParamFrame extends MethodFrame implements ActionListener {
    private static final int DEFAULT_WIDTH = 1100;
    private JButton generate;
    private JButton save;
    private JTextField width;
    private JTextField length;
    private JTextField upper;
    private JTextField lower;

    public ParamFrame() {
        super(DEFAULT_WIDTH);
        generate = new JButton("Generate");
        save = new JButton("Save");
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

        generate.addActionListener(this);
        setButtonProperties(generate);

        save.addActionListener(this);
        setButtonProperties(save);

        back.addActionListener(this);

        addLabel(menu, "Width");
        menu.add(width);
        addLabel(menu, "Length");
        menu.add(length);
        addLabel(menu, "Lower range limit:");
        menu.add(lower);
        addLabel(menu, "Upper range limit:");
        menu.add(upper);

        menu.add(generate);
        menu.add(save);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generate) {
            try {
                showGraphPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showConfirmDialog(
                        null,
                        "Dimensions should be integers and range doubles",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE
                );
            }
            save.setEnabled(true);
        } else if (e.getSource() == save) {
            saveGraph();
        } else if (e.getSource() == delete) {
            deleteGraph(DEFAULT_WIDTH);
            save.setEnabled(false);
            //generate.setEnabled(true);
        } else if (e.getSource() == back) {
            this.dispose();
            new SelectMethodFrame();
        }
    }

    private void showGraphPanel() throws NumberFormatException {
        graph = new Graph(
                Integer.parseInt(width.getText()),
                Integer.parseInt(length.getText()),
                Double.parseDouble(upper.getText()),
                Double.parseDouble(lower.getText())
        );

        if (delete == null) {
            delete = new JButton("Delete");
            setButtonProperties(delete);
            delete.addActionListener(this);
        }

        graphPanel = new GraphPanel(graph);
        graphPanel.setBackground(graphBackgroundColor);
        graphPanel.repaint();

        menu.add(delete);

        save.setEnabled(true);
        //generate.setEnabled(false);

        this.add(graphPanel);
        this.setSize(DEFAULT_WIDTH, graphPanel.getHeight() + 111);
        this.setLocationRelativeTo(null);
    }

    private void saveGraph() {
        if (actionFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            graph.printGraphToFile(actionFile.getSelectedFile().getAbsolutePath());
            JOptionPane.showConfirmDialog(
                    null,
                    "Graph saved",
                    "Data saved",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
        }
    }

    private void addLabel(JPanel panel, String text) {
        JLabel newLabel = new JLabel(text);
        setLabelProperties(newLabel);
        panel.add(newLabel);
    }


}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileFrame extends MethodFrame implements ActionListener {
    private static final int DEFAULT_WIDTH = 400;
    private static final int GRAPH_WIDTH = 1100;
    private JButton open;
    private JButton select;
    private JButton integrity;
    private JLabel selectedFile;

    public FileFrame() {
        super(DEFAULT_WIDTH);

        selectedFile = new JLabel("Select a file   ");
        setLabelProperties(selectedFile);

        select = new JButton("Select");
        setButtonProperties(select);
        select.addActionListener(this);

        open = new JButton("Open");
        setButtonProperties(open);
        open.addActionListener(this);
        open.setEnabled(false);

        integrity = new JButton("BFS");
        setButtonProperties(integrity);
        integrity.addActionListener(this);
        integrity.setEnabled(false);

        back.addActionListener(this);

        menu.add(selectedFile);
        menu.add(select);
        menu.add(open);
        menu.add(integrity);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            selectFile();
        } else if (e.getSource() == open) {
            try {
                showGraphPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showConfirmDialog(
                        null,
                        "Wrong file format",
                        "Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (e.getSource() == integrity) {
            if (graph.checkIntegrity()) {
                JOptionPane.showConfirmDialog(
                        null,
                        "Graph is connected",
                        "Integrity",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showConfirmDialog(
                        null,
                        "Graph is not connected",
                        "Integrity",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else if (e.getSource() == delete) {
            deleteGraph(DEFAULT_WIDTH);
        } else if (e.getSource() == back) {
            this.dispose();
            new SelectMethodFrame();
        }
    }

    private void selectFile() {
        if (actionFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile.setText("Selected file: " + actionFile.getSelectedFile().getName());
            select.setText("Change");
            open.setEnabled(true);
        }
    }

    private void showGraphPanel() throws NumberFormatException {
        if (actionFile.getSelectedFile().exists()) {
            graph = new Graph(actionFile.getSelectedFile().getAbsolutePath());
            integrity.setEnabled(true);
            if (delete == null) {
                delete = new JButton("Delete");
                setButtonProperties(delete);
                delete.addActionListener(this);
            }

            graphPanel = new GraphPanel(graph);
            graphPanel.setBackground(graphBackgroundColor);
            graphPanel.repaint();

            menu.add(integrity);
            menu.add(delete);
            menu.setSize(1100, 37);

            backPanel.setSize(1100, 37);

            this.add(graphPanel);
            this.setSize(GRAPH_WIDTH, graphPanel.getHeight() + 111);
            this.setLocationRelativeTo(null);
        }
    }

}

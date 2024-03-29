import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileFrame extends MethodFrame implements ActionListener {
    private static final int DEFAULT_WIDTH = 400;
    private static final int GRAPH_WIDTH = 1100;
    /**Powoduje wczytanie grafu z wybranego pliku*/
    private JButton open;
    /**Otwiera okno dialogowe do wybrania pliku*/
    private JButton select;
    /**Sprawdza spójność grafu po kliknięciu*/
    private JButton integrity;
    /**Informuje o stanie wybrania pliku*/
    private JLabel selectedFile;

    public FileFrame() {
        super(DEFAULT_WIDTH);

        selectedFile = new JLabel("Select a file   ");
        setLabelProperties(selectedFile);

        select = new JButton("Select");
        setButtonProperties(select);
        select.addActionListener(this);

        open = new JButton("Open");
        open.setEnabled(false);
        setButtonProperties(open);
        open.setBackground(new Color(245, 179, 132));
        open.addActionListener(this);

        integrity = new JButton("BFS");
        integrity.setEnabled(false);
        setButtonProperties(integrity);
        integrity.addActionListener(this);

        back.addActionListener(this);

        menu.add(selectedFile);
        menu.add(select);
        menu.add(open);

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

    /**
     * Obsługuje wybieranie pliku do wczytania grafu.
     */
    private void selectFile() {
        if (actionFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile.setText("Selected file: " + actionFile.getSelectedFile().getName());
            select.setText("Change");
            open.setEnabled(true);
            setButtonProperties(open);
        }
    }

    /**
     * Sprawia ze panel z grafem jest widoczny w aplikacji.
     */
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

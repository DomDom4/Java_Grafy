import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileFrame extends GraphFrame implements ActionListener {
    private static final int DEFAULT_WIDTH = 400;
    private JButton open;
    private JButton select;
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
            showGraphPanel();
        } else if (e.getSource() == delete) {
            deleteGraph(DEFAULT_WIDTH);
        } else if (e.getSource() == back) {
            this.dispose();
            SelectMethodFrame init = new SelectMethodFrame();
        }
    }

    private void selectFile() {
        if (actionFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile.setText("Selected file: "+ actionFile.getSelectedFile().getName());
            select.setText("Change");
            open.setEnabled(true);
        }
    }

    private void showGraphPanel() {
        if (actionFile.getSelectedFile().exists()) {
            if (delete == null) {
                delete = new JButton("Delete");
                setButtonProperties(delete);
                delete.addActionListener(this);
            }

            if (graphPanel == null) {
                graphPanel = new JPanel();
                graphPanel.setBounds(0, 74, 900, 500);
                graphPanel.setBackground(graphBackgroundColor);
            }

            menu.add(delete);
            menu.setSize(900,37);

            backPanel.setSize(900,37);

            this.add(graphPanel);
            this.setSize(900, 611);
            graph = new Graph(actionFile.getSelectedFile().getAbsolutePath());
        }
    }

}

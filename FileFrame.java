import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileFrame extends JFrame implements ActionListener {
    JPanel chooseFile;
    JPanel graphPanel;
    JButton open;
    JButton select;
    JButton delete;
    JLabel selectedFile;
    JFileChooser inFile;
    Graph readGraph;

    public FileFrame() {
        chooseFile = new JPanel();
        chooseFile.setBounds(0, 0, 350, 37);

        selectedFile = new JLabel("Select a file\t");

        select = new JButton("Select");
        select.addActionListener(this);

        open = new JButton("Open");
        open.addActionListener(this);
        open.setEnabled(false);

        chooseFile.add(selectedFile);
        chooseFile.add(select);
        chooseFile.add(open);

        this.add(chooseFile);
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(350, 74);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            selectFile();
        } else if (e.getSource() == open) {
            openGraph();
        } else if (e.getSource() == delete) {
            deleteGraph();
        }
    }

    private void selectFile() {
        inFile = new JFileChooser();
        if (inFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile.setText(inFile.getSelectedFile().getName());
            select.setText("Change");
            open.setEnabled(true);
            readGraph = new Graph(inFile.getSelectedFile().getAbsolutePath());
        }
    }

    private void openGraph() {
        if (inFile.getSelectedFile().exists()) {
            if(delete == null) {
                delete = new JButton("Delete");
                delete.addActionListener(this);
            }

            if( graphPanel==null) {
                graphPanel = new JPanel();
                graphPanel.setBounds(0, 37, 900, 500);
                graphPanel.setBackground(Color.PINK);
            }

            chooseFile.add(delete);
            chooseFile.setBounds(0, 0, 900, 37);

            this.add(graphPanel);
            this.setSize(900, 574);
            readGraph = new Graph(inFile.getSelectedFile().getAbsolutePath());
        }
    }

    private void deleteGraph(){
        chooseFile.remove(delete);
        chooseFile.setBounds(0, 0, 350, 37);

        this.remove(graphPanel);
        this.setSize(350,74);
    }
}

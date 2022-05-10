import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParamFrame extends JFrame implements ActionListener {
    private JButton generate;
    private JButton save;
    private JButton delete;
    private JTextField width;
    private JTextField length;
    private JTextField upper;
    private JTextField lower;
    private JPanel menu;
    private JPanel graphPanel;
    private Graph genGraph;

    public ParamFrame() {
        generate = new JButton("Generate");
        save = new JButton("Save");
        delete = new JButton("Delete");
        width = new JTextField();
        length = new JTextField();
        upper = new JTextField();
        lower = new JTextField();
        menu = new JPanel();

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

        menu.setBounds(0, 0, 1100, 37);

        menu.add(new JLabel("Width:"));
        menu.add(width);

        menu.add(new JLabel("Length:"));
        menu.add(length);

        menu.add(new JLabel("Lower range limit:"));
        menu.add(lower);

        menu.add(new JLabel("Upper range limit:"));
        menu.add(upper);

        menu.add(generate);
        menu.add(save);
        menu.add(delete);

        this.add(menu);
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1100, 74);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generate) {
            drawGraph();
            save.setEnabled(true);
            delete.setEnabled(true);
        } else if (e.getSource() == save) {
            saveGraph();
        } else if (e.getSource() == delete) {
            deleteGraph();
        }
    }

    private void drawGraph(){
        graphPanel = new JPanel();
        graphPanel.setBounds(0, 37, 1100, 500);
        graphPanel.setBackground(Color.PINK);
        save.setEnabled(true);
        delete.setEnabled(true);

        genGraph = new Graph(
                Integer.parseInt(width.getText()),
                Integer.parseInt(length.getText()),
                Double.parseDouble(upper.getText()),
                Double.parseDouble(lower.getText())
        );

        this.add(graphPanel);
        this.setSize(1100, 574);
    }

    private void saveGraph(){
        JFileChooser outFile = new JFileChooser();
        if( outFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            genGraph.printGraphToFile(outFile.getSelectedFile().getAbsolutePath());
        }
    }

    private void deleteGraph(){
        this.remove(graphPanel);
        this.setSize(1100, 74);
    }
}

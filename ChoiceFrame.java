import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChoiceFrame extends JFrame implements ActionListener {
    private JPanel choice;
    private JButton readParams;
    private JButton readFile;
    private JLabel label;

    public ChoiceFrame() {
        choice = new JPanel();
        choice.setBounds(0, 0, 350, 37);
        choice.setBackground(Color.ORANGE);

        label = new JLabel("Select a method");

        readParams = new JButton("Generate");
        readParams.addActionListener(this);

        readFile = new JButton("From file");
        readFile.addActionListener(this);

        choice.add(label);
        choice.add(readFile);
        choice.add(readParams);

        this.add(choice);
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(350, 74);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readParams) {
            this.dispose();
            ParamFrame paramFrame = new ParamFrame();
        } else if (e.getSource() == readFile) {
            this.dispose();
            FileFrame fileFrame = new FileFrame();
        }
    }
}

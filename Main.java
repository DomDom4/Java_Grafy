import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] options = {"From file", "Generate", "Cancel"};

        int methodChoice = JOptionPane.showOptionDialog(null, "Make graph:", "Graph making method",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (methodChoice == 0) { //wczytaj
            String inFileName = JOptionPane.showInputDialog("File name: ");
        } else if (methodChoice == 1) { //generuj
            JTextField width = new JTextField();
            JTextField length = new JTextField();
            JTextField lower = new JTextField();
            JTextField upper = new JTextField();
            JTextField outFile = new JTextField();
            Object[] parameters = {
                    "Width: ", width,
                    "Length: ", length,
                    "Lower range: ", lower,
                    "Upper range: ", upper,
                    "Save file name: ", outFile
            };
            int option = JOptionPane.showConfirmDialog(null, parameters, "Parameters", JOptionPane.OK_CANCEL_OPTION);
            if (option == 2)
                return;

            Graph graph = new Graph(
                    Integer.parseInt(width.getText()),
                    Integer.parseInt(length.getText()),
                    Double.parseDouble(upper.getText()),
                    Double.parseDouble(lower.getText())
            );

            graph.printGraphToFile(outFile.getText());
        } else {
            return;
        }
    }
}

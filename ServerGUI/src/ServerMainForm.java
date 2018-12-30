import javax.swing.*;

/**
 * Created by Proinina Maria
 */
public class ServerMainForm {
    private JPanel rootPanel;

    public ServerMainForm() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServerMainForm");
        frame.setContentPane(new ServerMainForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;

public class GUI {
    private JPanel Panel;
    private JButton button1;
    private JTextArea textArea1;

    public GUI() throws SocketException {
        MainRun m = new MainRun();
        m.setup();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String s = m.makeTransaction();
                    System.out.println(s);
                    textArea1.append(s + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws SocketException {
        JFrame frame = new JFrame("Blockchain");
        frame.setSize(800,600);
        frame.setContentPane(new GUI().Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

import java.awt.*;
import javax.swing.*;

public class GUI extends Frame {
int x,y;

    JLabel label;
    JFrame frame;

    JScrollPane scrollPane;
    JTextArea textArea;

    JButton resetButton;
    JButton classifyButton;

    SVMClassifier classifier;

    public GUI(int x, int y){
        this.x = x;
        this.y = y;
        frame = new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(7,9,15));

        frame.setSize(x, y);
        label = new JLabel("Cargando espere...", SwingConstants.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 50));
        label.setForeground(new Color(255,255,255));
        frame.add(label);
        frame.setResizable(false);
        frame.setVisible(true);

        classifyButton = new JButton();
        classifyButton.setBackground(new Color(142,166,4));
        classifyButton.setText("Clasificar");
        classifyButton.setBounds(100,350,100,40);
        classifyButton.setForeground(new Color(255,255,255));
        resetButton = new JButton();      
        resetButton.setBackground(new Color(191,49,0));
        resetButton.setText("Reset");
        resetButton.setBounds(500,350,100,40);
        resetButton.setForeground(new Color(255,255,255)); 
        
        resetButton.addActionListener(e -> clearToPredict());
    }

    //Ya se que esto es feo y deberia estar afuera pero no tengo tiempo ok?
    public void setClassifier(SVMClassifier classifier){
        this.classifier = classifier;
    }

    public void setPredictionState(){
        label.setText("Texto a evaluar");
        label.setBounds((x / 2) - (700/2), (int) (y / 10), 700, 40);
        
        textArea = new JTextArea(10, 512);
        textArea.setLineWrap(true);
        textArea.setFont(new Font(label.getFont().getName(), Font.PLAIN, 15));
        
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(100, 125, 500, 200);
        
        frame.add(label);
        frame.add(scrollPane);
        frame.add(resetButton);
        frame.add(classifyButton);

        frame.setVisible(false);
        frame.setVisible(true);
    }
    public void clearToPredict(){
        textArea.setText("");
    }
}
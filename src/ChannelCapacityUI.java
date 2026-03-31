import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ChannelCapacityUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChannelCapacityUI::createUI);
    }

    private static void createUI() {

        JFrame frame = new JFrame("Adaptive Channel Capacity Analyzer");
        frame.setSize(750, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ===== Theme Colors =====
        Color bgColor = new Color(18, 18, 18);
        Color panelColor = new Color(28, 28, 28);
        Color accent = new Color(0, 173, 181);
        Color textColor = Color.WHITE;

        frame.getContentPane().setBackground(bgColor);

        // ===== Title =====
        JLabel title = new JLabel("Adaptive Channel Capacity Analyzer", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(accent);
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        frame.add(title, BorderLayout.NORTH);

        // ===== Main Panel =====
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(bgColor);

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 15));
        inputPanel.setBackground(panelColor);
        inputPanel.setBorder(new TitledBorder(new LineBorder(accent), "Input Parameters", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), accent));

        JTextField bandwidth = createField();
        JTextField levels = createField();
        JTextField signalPower = createField();
        JTextField noisePower = createField();

        inputPanel.add(createLabel("Bandwidth (Hz):"));
        inputPanel.add(bandwidth);

        inputPanel.add(createLabel("Signal Levels (L):"));
        inputPanel.add(levels);

        inputPanel.add(createLabel("Signal Power (S):"));
        inputPanel.add(signalPower);

        inputPanel.add(createLabel("Noise Power (N):"));
        inputPanel.add(noisePower);

        JButton calculate = new JButton("Calculate");
        JButton reset = new JButton("Reset");

        styleButton(calculate, accent);
        styleButton(reset, Color.GRAY);

        inputPanel.add(calculate);
        inputPanel.add(reset);

        // ===== Output Panel =====
        JPanel outputPanel = new JPanel();

        // ===== Graph Panel =====
        GraphPanel graphPanel = new GraphPanel();

        outputPanel.setLayout(new GridLayout(6, 1, 10, 15));
        outputPanel.setBackground(panelColor);
        outputPanel.setBorder(new TitledBorder(new LineBorder(accent), "Results", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), accent));

        JLabel nyquistLabel = createOutput("Nyquist Bit Rate: ");
        JLabel shannonLabel = createOutput("Shannon Capacity: ");
        JLabel snrLabel = createOutput("SNR (dB): ");
        JLabel modulationLabel = createOutput("Recommended Modulation: ");
        JLabel statusLabel = createOutput("");
        JLabel snrQualityLabel = createOutput("SNR Quality: ");

        outputPanel.add(nyquistLabel);
        outputPanel.add(shannonLabel);
        outputPanel.add(snrLabel);
        outputPanel.add(modulationLabel);
        outputPanel.add(statusLabel);
        outputPanel.add(snrQualityLabel);

        // Add graph below results
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(panelColor);
        rightPanel.add(outputPanel, BorderLayout.NORTH);
        rightPanel.add(graphPanel, BorderLayout.CENTER);

        mainPanel.add(inputPanel);
        mainPanel.add(rightPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        // ===== Actions =====
        calculate.addActionListener(e -> {
            try {
                double B = Double.parseDouble(bandwidth.getText());
                int L = Integer.parseInt(levels.getText());
                double S = Double.parseDouble(signalPower.getText());
                double N = Double.parseDouble(noisePower.getText());

                double nyquist = Calculator.calculateNyquist(B, L);
                double shannon = Calculator.calculateShannon(B, S, N);
                double snrDb = Calculator.calculateSNRdB(S, N);
                String modulation = ModulationSelector.selectModulation(snrDb);

                String snrQuality;
                Color qualityColor;

                if (snrDb < 10) {
                    snrQuality = "LOW";
                    qualityColor = Color.RED;
                } else if (snrDb < 20) {
                    snrQuality = "MEDIUM";
                    qualityColor = Color.ORANGE;
                } else {
                    snrQuality = "HIGH";
                    qualityColor = Color.GREEN;
                }

                nyquistLabel.setText("Nyquist Bit Rate: " + String.format("%.2f", nyquist) + " bps");
                shannonLabel.setText("Shannon Capacity: " + String.format("%.2f", shannon) + " bps");
                snrLabel.setText("SNR (dB): " + String.format("%.2f", snrDb));
                modulationLabel.setText("Recommended Modulation: " + modulation);
                snrQualityLabel.setText("SNR Quality: " + snrQuality);
                snrQualityLabel.setForeground(qualityColor);
                graphPanel.updateValues(nyquist, shannon);
                statusLabel.setText("✔ Calculation Successful");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reset.addActionListener(e -> {
            bandwidth.setText("");
            levels.setText("");
            signalPower.setText("");
            noisePower.setText("");

            nyquistLabel.setText("Nyquist Bit Rate: ");
            shannonLabel.setText("Shannon Capacity: ");
            snrLabel.setText("SNR (dB): ");
            modulationLabel.setText("Recommended Modulation: ");
            statusLabel.setText("Reset Complete");
            snrQualityLabel.setText("SNR Quality: ");
            snrQualityLabel.setForeground(Color.WHITE);
        });

        frame.setVisible(true);
    }

    private static JTextField createField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(40, 40, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new LineBorder(new Color(0, 173, 181)));
        return field;
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private static JLabel createOutput(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}

class GraphPanel extends JPanel {

    private double nyquist = 0;
    private double shannon = 0;

    public GraphPanel() {
        setPreferredSize(new Dimension(300, 200));
        setBackground(new Color(28, 28, 28));
    }

    public void updateValues(double nyquist, double shannon) {
        this.nyquist = nyquist;
        this.shannon = shannon;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (nyquist == 0 && shannon == 0) return;

        int width = getWidth();
        int height = getHeight();

        int barWidth = 60;
        int gap = 50;

        double max = Math.max(nyquist, shannon);

        int nyHeight = (int) ((nyquist / max) * (height - 50));
        int shHeight = (int) ((shannon / max) * (height - 50));

        // Nyquist bar
        g.setColor(new Color(0, 173, 181));
        g.fillRect(100, height - nyHeight - 20, barWidth, nyHeight);
        g.setColor(Color.WHITE);
        g.drawString("Nyquist", 100, height - 5);

        // Shannon bar
        g.setColor(Color.GREEN);
        g.fillRect(100 + barWidth + gap, height - shHeight - 20, barWidth, shHeight);
        g.setColor(Color.WHITE);
        g.drawString("Shannon", 100 + barWidth + gap, height - 5);
    }
}
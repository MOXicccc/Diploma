import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EncryptionGUI extends JFrame {

    private JPanel sidePanel, headerPanel, mainPanel;
    private JTextArea filePathsArea;
    private JButton browseButton, encryptButton;
    private JComboBox<String> algorithmComboBox;
    private JComboBox<String> storageComboBox;
    private JPanel resultsPanel;

    private File[] selectedFiles;

    public EncryptionGUI() {
        setTitle("Hybrid Storage & Encryption System");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setUndecorated(true);

        initSidePanel();
        initHeaderPanel();
        initMainPanel();
    }

    private void initSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setBackground(new Color(54, 33, 89));
        sidePanel.setBounds(0, 0, 250, 500);
        sidePanel.setLayout(null);

        JLabel title = new JLabel("Encryption App");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(30, 30, 200, 30);
        sidePanel.add(title);

        add(sidePanel);
    }

    private void initHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(122, 72, 221));
        headerPanel.setBounds(250, 0, 650, 70);
        headerPanel.setLayout(null);

        JLabel header = new JLabel("Encrypt & Analyze");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        header.setBounds(20, 20, 300, 30);
        headerPanel.add(header);

        JButton closeBtn = new JButton("X");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(122, 72, 221));
        closeBtn.setBorder(null);
        closeBtn.setBounds(610, 20, 30, 30);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));
        headerPanel.add(closeBtn);

        add(headerPanel);
    }

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(250, 70, 650, 430);
        mainPanel.setLayout(null);

        JLabel fileLabel = new JLabel("Select Images:");
        fileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fileLabel.setBounds(30, 20, 200, 25);
        mainPanel.add(fileLabel);

        filePathsArea = new JTextArea();
        filePathsArea.setEditable(false);
        filePathsArea.setLineWrap(true);
        JScrollPane scrollPanePaths = new JScrollPane(filePathsArea);
        scrollPanePaths.setBounds(150, 20, 350, 60);
        mainPanel.add(scrollPanePaths);

        browseButton = new JButton("Browse");
        browseButton.setBounds(510, 20, 100, 30);
        browseButton.addActionListener(e -> chooseFiles());
        mainPanel.add(browseButton);

        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        algoLabel.setBounds(30, 100, 200, 25);
        mainPanel.add(algoLabel);

        algorithmComboBox = new JComboBox<>(new String[]{
                "AES-256", "RSA", "XOR", "DNA", "Fisher-Yates"
        });
        algorithmComboBox.setBounds(150, 100, 200, 30);
        mainPanel.add(algorithmComboBox);

        JLabel storageLabel = new JLabel("Storage System:");
        storageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        storageLabel.setBounds(30, 150, 200, 25);
        mainPanel.add(storageLabel);

        storageComboBox = new JComboBox<>(new String[]{
                "Hybrid (Cloud-Local)", "Blockchain", "Multidimensional"
        });
        storageComboBox.setBounds(150, 150, 200, 30);
        mainPanel.add(storageComboBox);

        encryptButton = new JButton("Encrypt & Analyze");
        encryptButton.setBackground(new Color(122, 72, 221));
        encryptButton.setForeground(Color.WHITE);
        encryptButton.setBounds(150, 200, 200, 40);
        encryptButton.setFocusPainted(false);
        encryptButton.addActionListener(e -> encryptAction());
        mainPanel.add(encryptButton);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        resultsPanel.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPaneResults = new JScrollPane(resultsPanel);
        scrollPaneResults.setBounds(20, 260, 600, 160);
        scrollPaneResults.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneResults.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneResults.setBorder(BorderFactory.createTitledBorder("Metrics Results"));

        mainPanel.add(scrollPaneResults);
        add(mainPanel);
    }

    private void chooseFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFiles = chooser.getSelectedFiles();
            StringBuilder paths = new StringBuilder();
            for (File file : selectedFiles) {
                paths.append(file.getAbsolutePath()).append("\n");
            }
            filePathsArea.setText(paths.toString());
        }
    }

    private ImageEncryption getEncryptionAlgorithm(String algoName) throws Exception {
        return switch (algoName) {
            case "AES-256" -> new AESEncryption();
            case "RSA" -> new RSAEncryption();
            case "XOR" -> new XOREncryption();
            case "DNA" -> new DNAEncryption();
            case "Fisher-Yates" -> new FisherYatesEncryption();
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algoName);
        };
    }

    private StorageSystem getStorageSystem(String storageName) {
        return switch (storageName) {
            case "Blockchain" -> new BlockchainStorage();
            case "Multidimensional" -> new MultidimensionalStorage(3);
            case "Hybrid (Cloud-Local)" -> new HybridStorage();
            default -> throw new IllegalArgumentException("Unknown storage system: " + storageName);
        };
    }

    private void encryptAction() {
        String algorithm = algorithmComboBox.getSelectedItem().toString();
        String storageSystemName = storageComboBox.getSelectedItem().toString();

        if (selectedFiles == null || selectedFiles.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one image.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resultsPanel.removeAll();

        try {
            ImageEncryption encryption = getEncryptionAlgorithm(algorithm);
            StorageSystem storageSystem = getStorageSystem(storageSystemName);

            for (File file : selectedFiles) {
                String path = file.getAbsolutePath();
                byte[] originalData = ImageConverter.imageToByteArray(path);

                long startEnc = System.currentTimeMillis();
                byte[] encryptedData = encryption.encrypt(originalData, "secure-key");
                long encryptionTime = System.currentTimeMillis() - startEnc;

                long startDec = System.currentTimeMillis();
                byte[] decryptedData = encryption.decrypt(encryptedData, "secure-key");
                long decryptionTime = System.currentTimeMillis() - startDec;

                String fileName = file.getName() + "-" + algorithm + ".bin";
                storageSystem.store(encryptedData, fileName);

                double entropy = SecurityMetrics.calculateEntropy(encryptedData);
                double correlation = SecurityMetrics.calculateCorrelation(encryptedData);

                JTextArea metricsBox = new JTextArea();
                metricsBox.setEditable(false);
                metricsBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                metricsBox.setBackground(Color.WHITE);
                metricsBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                metricsBox.setPreferredSize(new Dimension(270, 130));

                metricsBox.setText(String.format("""
                    Image: %s
                    -----------------------------
                    Algorithm: %s
                    Storage: %s
                    Enc Time: %d ms
                    Dec Time: %d ms
                    Entropy: %.4f
                    Correlation: %.4f
                    """,
                        file.getName(), algorithm, storageSystemName, encryptionTime,
                        decryptionTime, entropy, correlation));

                resultsPanel.add(metricsBox);
            }

            resultsPanel.revalidate();
            resultsPanel.repaint();

            JOptionPane.showMessageDialog(this, "Batch Encryption Completed!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Encryption Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EncryptionGUI().setVisible(true));
    }
}


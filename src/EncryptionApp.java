import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EncryptionApp {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Encryption App");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(900, 700);
			frame.setLayout(new BorderLayout());

			JPanel leftPanel = new JPanel();
			leftPanel.setBackground(new Color(64, 0, 128));
			leftPanel.setPreferredSize(new Dimension(150, frame.getHeight()));
			leftPanel.setLayout(new BorderLayout());
			JLabel appLabel = new JLabel("Encryption App", SwingConstants.CENTER);
			appLabel.setForeground(Color.WHITE);
			appLabel.setFont(new Font("Arial", Font.BOLD, 16));
			leftPanel.add(appLabel, BorderLayout.CENTER);

			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new GridBagLayout());
			rightPanel.setBackground(Color.WHITE);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(10, 10, 10, 10);
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.gridx = 0;
			gbc.gridy = 0;
			rightPanel.add(new JLabel("Select Image:"), gbc);

			gbc.gridx = 1;
			JTextField imageField = new JTextField(20);
			rightPanel.add(imageField, gbc);

			gbc.gridx = 2;
			JButton browseButton = new JButton("Browse");
			rightPanel.add(browseButton, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			rightPanel.add(new JLabel("Algorithm:"), gbc);

			gbc.gridx = 1;
			JComboBox<String> algorithmComboBox = new JComboBox<>(
					new String[] { "AES-256", "RSA", "DNA", "XOR", "Fisher-Yates" });
			rightPanel.add(algorithmComboBox, gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			rightPanel.add(new JLabel("Storage System:"), gbc);

			gbc.gridx = 1;
			JComboBox<String> storageComboBox = new JComboBox<>(
					new String[] { "Hybrid (Cloud-Local)", "Block-chain system", "Multidimensional" });
			rightPanel.add(storageComboBox, gbc);

			gbc.gridx = 1;
			gbc.gridy = 3;
			JButton encryptButton = new JButton("Encrypt & Analyze");
			rightPanel.add(encryptButton, gbc);

			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.gridwidth = 3;
			rightPanel.add(new JLabel("Metrics Results:"), gbc);

			gbc.gridy = 5;
			JTextArea metricsArea = new JTextArea(10, 50);
			metricsArea.setEditable(false);
			rightPanel.add(new JScrollPane(metricsArea), gbc);

			frame.add(leftPanel, BorderLayout.WEST);
			frame.add(rightPanel, BorderLayout.CENTER);

			frame.setVisible(true);

			final String[] imagePath = { "" };

			browseButton.addActionListener(e -> {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imagePath[0] = selectedFile.getAbsolutePath();
					imageField.setText(imagePath[0]);
				}
			});

			encryptButton.addActionListener(e -> {
				String key = "secure-password";
				try {
					byte[] originalImage = ImageConverter.imageToByteArray(imageField.getText() + "/test-image.jpg");
					ImageEncryption aesEncryption = new AESEncryption();

					java.util.List<Long> encryptionTimes = new ArrayList<>();
					List<Long> decryptionTimes = new ArrayList<>();

					int imagesProcessed = 3;

					byte[] encryptedData = null;

					for (int i = 0; i < imagesProcessed; i++) {
						long startEnc = System.currentTimeMillis();
						encryptedData = aesEncryption.encrypt(originalImage, key);
						long endEnc = System.currentTimeMillis();
						encryptionTimes.add(endEnc - startEnc);

						long startDec = System.currentTimeMillis();
						aesEncryption.decrypt(encryptedData, key);
						long endDec = System.currentTimeMillis();
						decryptionTimes.add(endDec - startDec);
					}

					long localAccessTime = 10;
					long cloudAccessTime = 100;
					double alpha = 0.6;

					String result = SecurityMetrics.printMetrics(encryptedData, encryptionTimes, decryptionTimes,
																 imagesProcessed, localAccessTime, cloudAccessTime, alpha);
					metricsArea.setText(result);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});
		});
	}
}
import java.util.*;
import java.io.*;


public class SecurityMetrics {

	public static double calculateEntropy(byte[] data) {
		int[] freq = new int[256];
		for (byte b : data) {
			freq[b & 0xFF]++;
		}
		double entropy = 0;
		int len = data.length;
		for (int f : freq) {
			if (f > 0) {
				double p = (double) f / len;
				entropy += p * (Math.log(p) / Math.log(2));
			}
		}
		return -entropy;
	}

	public static double calculateCorrelation(byte[] data) {
		double meanX = 0, meanY = 0;
		double stdX = 0, stdY = 0, covariance = 0;

		int n = data.length - 1;
		for (int i = 0; i < n; i++) {
			meanX += (data[i] & 0xFF);
			meanY += (data[i + 1] & 0xFF);
		}
		meanX /= n;
		meanY /= n;

		for (int i = 0; i < n; i++) {
			double dx = (data[i] & 0xFF) - meanX;
			double dy = (data[i + 1] & 0xFF) - meanY;

			covariance += dx * dy;
			stdX += dx * dx;
			stdY += dy * dy;
		}

		covariance /= n;
		stdX = Math.sqrt(stdX / n);
		stdY = Math.sqrt(stdY / n);

		return covariance / (stdX * stdY);
	}

	public static double averageTime(List<Long> times) {
		return times.stream().mapToLong(Long::longValue).average().orElse(0);
	}

	public static double processingSpeed(int N, double t_enc, double t_dec) {
		return N / (t_enc + t_dec);
	}

	public static double accessTime(long tLocal, long tCloud, double alpha) {
		return alpha * tLocal + (1 - alpha) * tCloud;
	}

	public static String printMetrics(byte[] encryptedData, List<Long> encTimes, List<Long> decTimes,
									  int imagesProcessed, long localAccessTime, long cloudAccessTime, double alpha) {
		StringBuilder sb = new StringBuilder();

		sb.append("Entropy (H): %.4f bits%n".formatted(calculateEntropy(encryptedData))).append(System.lineSeparator());
		sb.append("Correlation (R): %.4f%n".formatted(calculateCorrelation(encryptedData))).append(System.lineSeparator());

		double t_enc_avg = averageTime(encTimes);
		double t_dec_avg = averageTime(decTimes);
		sb.append("Avg Encryption Time (t_enc): %.2f ms%n".formatted(t_enc_avg)).append(System.lineSeparator());
		sb.append("Avg Decryption Time (t_dec): %.2f ms%n".formatted(t_dec_avg)).append(System.lineSeparator());

		sb.append("Processing Speed (S): %.2f images/sec%n".formatted(
				processingSpeed(imagesProcessed, t_enc_avg / 1000, t_dec_avg / 1000))).append(System.lineSeparator());

		sb.append("Access Time (T_access): %.2f ms%n".formatted(accessTime(localAccessTime, cloudAccessTime, alpha)))
		  .append(System.lineSeparator());
		return sb.toString();
	}
}

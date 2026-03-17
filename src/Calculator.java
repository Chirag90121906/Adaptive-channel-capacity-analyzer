
public class Calculator {

    // Nyquist Bit Rate: 2 * B * log2(L)
    public static double calculateNyquist(double bandwidth, int levels) {
        if (levels <= 0) return 0;
        return 2 * bandwidth * log2(levels);
    }

    // Shannon Capacity: B * log2(1 + S/N)
    public static double calculateShannon(double bandwidth, double signalPower, double noisePower) {
        if (noisePower == 0) return 0;
        double snr = signalPower / noisePower;
        return bandwidth * log2(1 + snr);
    }

    // SNR (linear)
    public static double calculateSNR(double signalPower, double noisePower) {
        if (noisePower == 0) return 0;
        return signalPower / noisePower;
    }

    // SNR in dB
    public static double calculateSNRdB(double signalPower, double noisePower) {
        double snr = calculateSNR(signalPower, noisePower);
        if (snr == 0) return 0;
        return 10 * log10(snr);
    }

    // log base 2
    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    // log base 10
    private static double log10(double x) {
        return Math.log(x) / Math.log(10);
    }
}
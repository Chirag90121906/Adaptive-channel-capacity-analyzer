public class ModulationSelector {

    // Returns recommended modulation based on SNR (in dB)
    public static String selectModulation(double snrDb) {

        if (snrDb < 10) {
            return "BPSK (Low SNR - High Reliability)";
        } 
        else if (snrDb < 20) {
            return "QPSK (Moderate SNR - Balanced Performance)";
        } 
        else {
            return "16-QAM (High SNR - High Data Rate)";
        }
    }
}

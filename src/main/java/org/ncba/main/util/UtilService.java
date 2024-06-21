package org.ncba.main.util;

/**
 * @ Author Nephat Muchiri
 * Date 21/06/2024
 */
public class UtilService {

    private UtilService(){}

    /**
     * Normalizes a given MSISDN (mobile number) to a standard format.
     *
     * @param msisdn The MSISDN to normalize.
     * @return The normalized MSISDN or null if the input is null.
     * <p>
     * This method handles the following cases:
     * 1. If the MSISDN is null, it returns null.
     * 2. If the MSISDN starts with "+254", it removes the "+".
     * 3. If the MSISDN starts with "0", it replaces the "0" with "254".
     * 4. If the MSISDN starts with "254", it returns the MSISDN unchanged.
     * 5. For all other cases, it returns the MSISDN unchanged.
     */
    public static String normalizeMSISDN(String msisdn) {
        if (msisdn == null) {
            return null;
        }
        if (msisdn.startsWith("+254")) {
            return msisdn.substring(1);
        } else if (msisdn.startsWith("0")) {
            return "254" + msisdn.substring(1);
        } else if (msisdn.startsWith("254")) {
            return msisdn;
        } else {
            return msisdn;
        }
    }

}

package org.ncba.main.util;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @ Author Nephat Muchiri
 * Date 21/06/2024
 */
public class UtilService {
    public static String generateUUID(String documentType, String documentNumber, String msisdn) {
        String combined = documentType + ":" + documentNumber + ":" + msisdn;
        UUID uuid = UUID.nameUUIDFromBytes(combined.getBytes(StandardCharsets.UTF_8));
        return uuid.toString();
    }

    private UtilService(){}
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

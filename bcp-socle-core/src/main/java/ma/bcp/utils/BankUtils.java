package ma.bcp.utils;

import ma.bcp.utils.dto.Bank;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class BankUtils {

    public static String extractBankCodeFromRib(String rib) {
        return rib.substring(0, 3);
    }

    public static String extractCityCodeFromRib(String rib) {
        return rib.substring(3, 6);
    }

    public static String extractAccountNumberFromRib(String rib) {
        return rib.substring(6, 22);
    }

    public static String extractKeyFromRib(String rib) {
        return rib.substring(22, 24);
    }

    public static Bank extractBankFromRib(String rib) {
        switch (extractBankCodeFromRib(rib)){
            case "007":
                return new Bank("007","Attijariwafa Bank","AWB");
            default:
                return new Bank("190","Banque Populaire","BCP");
        }
    }
    

    public static boolean isValidMoroccanAccountNumber(String accountNumber) {
        return accountNumber.length() == 16;
    }

    public static String formatAmountInMoroccanCurrency(double amount) {
        return "MAD " + String.format("%.2f", amount);
    }

    public static String generateTransactionReference() {
        return UUID.randomUUID().toString();
    }

    public static String generateTransactionId() {
        // Generate a unique transaction ID for bank transactions (e.g., combining a timestamp and a random number).
        return Instant.now().toString() + "-" + new Random().nextInt(100000);
    }

}
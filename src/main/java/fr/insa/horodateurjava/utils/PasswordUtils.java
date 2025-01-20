package fr.insa.horodateurjava.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class PasswordUtils {

    // Hacher un mot de passe
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Vérifier un mot de passe
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
        //return Objects.equals(plainPassword, hashedPassword);
    }
}

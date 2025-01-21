package fr.insa.horodateurjava.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitaire pour la gestion des mots de passe.
 * Fournit des fonctionnalités pour hacher et vérifier les mots de passe.
 */
public class PasswordUtils {

    /**
     * Hache un mot de passe en utilisant l'algorithme BCrypt.
     *
     * @param plainPassword Le mot de passe en texte brut à hacher.
     * @return Le mot de passe haché.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Vérifie si un mot de passe en texte brut correspond à un mot de passe haché.
     *
     * @param plainPassword  Le mot de passe en texte brut.
     * @param hashedPassword Le mot de passe haché à vérifier.
     * @return {@code true} si le mot de passe correspond, {@code false} sinon.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

package fr.insa.horodateurjava.utils;

public class Session {

    private static Session instance;

    private String userEmail;
    private String fname;
    private String lname;

    private Session() {
    }

    // Méthode pour obtenir l'instance unique
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

     public String getFname() {
        return fname;
     }

     public void setFname(String fname) {
        this.fname = fname;
     }

     public String getLname() {
        return lname;
     }

     public void setLname(String lname) {
        this.lname = lname;
     }

    public void clearSession() {
        userEmail = null;
        fname = null;
        lname = null;
    }

}

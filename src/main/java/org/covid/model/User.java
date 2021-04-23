package org.covid.model;

/**
 *Хэрэглчийн model
 */
public class User {
    private String mail;
    private String password;

    /**
     *Байгуулагч
     * @param mail String төрөлтэй майл авна
     * @param password нууц үг авна
     */
    public User(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    /**
     *Майлыг буцаах
     * @return string mail
     */
    public String getMail() {
        return mail;
    }

    /**
     *Нууц үгийг буцаах
     * @return string password
     */
    public String getPassword() {
        return password;
    }

}
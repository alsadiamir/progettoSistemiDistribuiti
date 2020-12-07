package it.unibo.canteen.authentication;

public class AuthUserData {
    public static final String ATTR_NAME = "authUserData";
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

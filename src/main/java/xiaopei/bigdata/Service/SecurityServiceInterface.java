package xiaopei.bigdata.Service;

public interface SecurityServiceInterface {
    String findLoggedInUsername();

    void autoLogin(String username, String password);

}

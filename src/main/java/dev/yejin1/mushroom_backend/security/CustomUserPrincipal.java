package dev.yejin1.mushroom_backend.security;

public class CustomUserPrincipal {
    private final String username;
    private final Long usrId;

    public CustomUserPrincipal(String username, Long usrId) {
        this.username = username;
        this.usrId = usrId;
    }

    public String getUsername() {
        return username;
    }

    public Long getUsrId() {
        return usrId;
    }
}

package eu.trentorise.game.platform;

import java.util.List;

public interface PlatformRolesClient {
    List<String> getRolesByToken(String token);
}

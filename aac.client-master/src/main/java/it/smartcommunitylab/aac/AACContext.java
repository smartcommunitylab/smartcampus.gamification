package it.smartcommunitylab.aac;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import it.smartcommunitylab.aac.AACProfileService;
import it.smartcommunitylab.aac.AACRoleService;
import it.smartcommunitylab.aac.AACService;
import it.smartcommunitylab.aac.model.TokenData;

public class AACContext {

    private String aacUri;
    private String clientId;
    private String clientSecret;

    private AACService aacService;
    private ConcurrentHashMap<String, TokenData> tokens;

    public AACContext(String uri, String clientId, String clientSecret) {
        if (StringUtils.isEmpty(uri)) {
            throw new IllegalArgumentException("AAC uri is required");
        }
        if (StringUtils.isEmpty(clientId)) {
            throw new IllegalArgumentException("AAC clientId is required");
        }

        this.aacUri = uri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.tokens = new ConcurrentHashMap<>();

        // Generates the service to obtain the proper client tokens needed for certain
        // calls to the identity provider's APIs
        this.aacService = new AACService(aacUri, clientId, clientSecret);

    }

    public String getClientId() {
        return clientId;
    }

    protected String getClientSecret() {
        return clientSecret;
    }

    public String getToken(String scope) throws AACException {
        TokenData data = tokens.get(scope);
        
        //renew if expires in less than 30 seconds
        if (data == null || (data.getExpires_on()  < (System.currentTimeMillis() + 1000 * 30))) {
            synchronized (tokens) {
                    try {
                        TokenData dt = aacService.generateClientToken(scope);
                        tokens.put(scope, dt);
                    } catch (Exception e) {
                        throw new AACException(e.getMessage(), e.getCause());
                    }
            }

        }
        return tokens.get(scope).getAccess_token();
    }

    /*
     * Services TODO implement singleton or factory
     */

    public AACAuthorizationService getAACAuthorizationService() {
        return new AACAuthorizationService(aacUri);
    }

    public AACProfileService getAACProfileService() {
        return new AACProfileService(aacUri); // service for various operations on AAC Profiles
    }

    public AACRoleService getAACRoleService() {
        return new AACRoleService(aacUri); // service for various operations on AAC roles
    }

    public AACServiceDefinitionService getAACServiceDefinitionService() {
        return new AACServiceDefinitionService(aacUri);
    }

    public AACUserClaimService getAACUserClaimService() {
        return new AACUserClaimService(aacUri);
    }

}

package eu.trentorise.game.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.aac.AACException;
import it.smartcommunitylab.aac.AACRoleService;
import it.smartcommunitylab.aac.model.Role;

@Component
@PropertySource("classpath:engine.web.properties")
public class AacRolesClient implements PlatformRolesClient {

    @Resource
    private Environment env;

    private static String aacURL;
    
    // tuple
    private static String context;
    private static String rolePrefix;
    private AACRoleService aacRoleService;
    

    @PostConstruct
    private void init() {
       
    	context = env.getProperty("aac.roles.context");
        rolePrefix = env.getProperty("aac.roles.role");
//        rolePrefixLength = rolePrefix.length();
        
        aacURL = env.getProperty("aac.url");
        aacRoleService = new AACRoleService(aacURL); 

    }

    @Override
    public List<String> getRolesByToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new TokenRequest(token));
        restTemplate.getMessageConverters().add(new RolesConverter());
        List<String> domains = new ArrayList<String>();
        try{
//      Set<Role> roles =  aacRoleService.getRoles(token);
        Set<Role> roles = aacRoleService.getClientRoles(token);
        for (Role role : roles) {
            if (context.equalsIgnoreCase(role.getContext())
                    && role.getRole().startsWith(rolePrefix)) {
                domains.add(role.getSpace());
            }
        }
        }catch (RestClientException e) {
            throw new IllegalArgumentException("the token seems invalid");
        } catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AACException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return domains;
    }


    private class TokenRequest implements ClientHttpRequestInterceptor {
        private String token;

        public TokenRequest(String token) {
            this.token = token;
        };

        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().add("Authorization", "Bearer " + token);
            return execution.execute(request, body);
        }
    }


    private class RolesConverter extends AbstractHttpMessageConverter<AacRole[]> {

        @Override
        protected boolean supports(Class<?> clazz) {
            return clazz.isInstance(List.class);
        }

        @Override
        protected AacRole[] readInternal(Class<? extends AacRole[]> clazz,
                HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.getTypeFactory().constructArrayType(AacRole.class);
            return mapper.readValue(inputMessage.getBody(), type);

        }

        @Override
        protected void writeInternal(AacRole[] t, HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {
            throw new UnsupportedOperationException();

        }

    }

    @SuppressWarnings("unused")
    private static class AacRole {
        public String id;
        public String scope;
        public String role;
        public String context;
        public String authority;
    }
}

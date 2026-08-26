package it.smartcommunitylab.aac;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import it.smartcommunitylab.aac.model.Role;
import it.smartcommunitylab.aac.model.User;

public class AACRoleService {

	private String aacURL;
	
	private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param aacURL URL of the AAC service
     */
	public AACRoleService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) this.aacURL += '/';
	}	
	
	public Collection<Role> getRoles(String token) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/me";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	List<?> data = mapper.readValue(response, List.class);
	            	Set<Role> result = (Set<Role>) data.stream().map(x -> mapper.convertValue(x, Role.class)).collect(Collectors.toSet());
	                return result;
	            }
	            throw new AACException("Error in userroles/me " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public void addRoles(String token, String userId, List<String> roles) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/user/" + userId + "?roles=" + Joiner.on(",").join(roles);
	        final HttpPut put = new HttpPut(url);
	        put.setHeader("Accept", "application/json");
	        put.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(put);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in put userroles/user " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	
	}
	
	public void deleteRoles(String token, String userId, List<String> roles) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/user/" + userId + "?roles=" + Joiner.on(",").join(roles);
	        final HttpDelete delete = new HttpDelete(url);
	        delete.setHeader("Accept", "application/json");
	        delete.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(delete);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in delete userroles/user " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	
	}	
	
	public Collection<Role> getRolesByUserId(String token, String userId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/user/" + userId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	List<?> data = mapper.readValue(response, List.class);
	            	Set<Role> result = (Set<Role>) data.stream().map(x -> mapper.convertValue(x, Role.class)).collect(Collectors.toSet());
	                return result;
	            }
	            throw new AACException("Error in userroles/tenant/user " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public Collection<Role> getRolesByClientId(String token, String clientId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/client/" + clientId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	List<?> data = mapper.readValue(response, List.class);
	            	Set<Role> result = (Set<Role>) data.stream().map(x -> mapper.convertValue(x, Role.class)).collect(Collectors.toSet());
	                return result;
	            }
	            throw new AACException("Error in userroles/tenant/user " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}
	
	public Collection<Role> getRolesByToken(String token, String clientToken) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/token/" + clientToken;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	List<?> data = mapper.readValue(response, List.class);
	            	Set<Role> result = (Set<Role>) data.stream().map(x -> mapper.convertValue(x, Role.class)).collect(Collectors.toSet());
	                return result;
	            }
	            throw new AACException("Error in userroles/token/{token} " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}

	public Collection<User> getSpaceUsers(String context, String role, boolean nested, Integer offset, Integer limit, String clientToken) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/role?context="+context;
	        if (role != null) url += "&role="+role;
	        if (offset != null) url += "&offset=" +offset;
	        if (limit != null) url += "&limit=" + limit;
	        if (nested) url += "&nested=true";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + clientToken);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	List<?> data = mapper.readValue(response, List.class);
	            	Set<User> result = (Set<User>) data.stream().map(x -> mapper.convertValue(x, User.class)).collect(Collectors.toSet());
	                return result;
	            }
	            throw new AACException("Error in userroles/tenant/user " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}
	
	public Collection<Role> getClientRoles(String token) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "userroles/client";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	List<?> data = mapper.readValue(response, List.class);
	            	Set<Role> result = (Set<Role>) data.stream().map(x -> mapper.convertValue(x, Role.class)).collect(Collectors.toSet());
	                return result;
	            }
	            throw new AACException("Error in userroles/client " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	
    private HttpClient getHttpClient() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient;
    }	
	
}

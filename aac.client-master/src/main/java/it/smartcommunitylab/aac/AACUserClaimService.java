package it.smartcommunitylab.aac;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.aac.model.Page;
import it.smartcommunitylab.aac.model.UserClaimProfile;

public class AACUserClaimService {

	private String aacURL;
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * @param aacURL URL of the AAC service
     */
	public AACUserClaimService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) this.aacURL += '/';
	}	
	
	@SuppressWarnings("unchecked")
	public Page<UserClaimProfile> getServiceClaims(String token, String serviceId, Integer page, Integer size, String sort) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId;
	        String query = "?"; 
    		if (page != null) query += "page=" + page;
    		if (size != null) query += "&size=" + size;
    		if (sort != null) query += "&sort=" + sort;
    		
	        
	        final HttpGet get = new HttpGet(url + query);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                Page<Object> p = mapper.readValue(response, Page.class);
	                List<UserClaimProfile> list = p.getContent().stream().map(c -> mapper.convertValue(c, UserClaimProfile.class)).collect(Collectors.toList());
	                Page<UserClaimProfile> res = new Page<>();
	                res.setContent(list);
	                res.setFirst(p.getFirst()); res.setLast(p.getLast()); res.setNumber(p.getNumber()); res.setNumberOfElements(p.getNumberOfElements());
	                res.setSize(p.getSize()); res.setTotalElements(p.getTotalElements()); res.setTotalPages(p.getTotalPages());
	                return res;
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public UserClaimProfile getServiceClaimsForUserId(String token, String serviceId, String userId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/" + userId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, UserClaimProfile.class);
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public UserClaimProfile getServiceClaimsForUsername(String token, String serviceId, String username) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/username?username=" + username;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, UserClaimProfile.class);
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	public UserClaimProfile saveClaimsForUserId(String token, String serviceId, String userId, Map<String, Object> claims) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/" + userId;
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        UserClaimProfile profile = new UserClaimProfile(userId, null);
	        profile.setClaims(claims);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(profile), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, UserClaimProfile.class);
	            }
	            throw new AACException("Error in save service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public void deleteClaimsForUserId(String token, String serviceId, String userId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/" + userId;
	        final HttpDelete delete = new HttpDelete(url);
	        delete.setHeader("Accept", "application/json");
	        delete.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(delete);
	            throw new AACException("Error in save service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	public void deleteClaimsForUsername(String token, String serviceId, String username) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/username?username=" + username;
	        final HttpDelete delete = new HttpDelete(url);
	        delete.setHeader("Accept", "application/json");
	        delete.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(delete);
	            throw new AACException("Error in save service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	

	public UserClaimProfile saveClaimsForUsername(String token, String serviceId, String username, Map<String, Object> claims) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/claims/" + serviceId +"/username?username=" + username;
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        UserClaimProfile profile = new UserClaimProfile(null, username);
	        profile.setClaims(claims);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(profile), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, UserClaimProfile.class);
	            }
	            throw new AACException("Error in save service " + resp.getStatusLine());
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

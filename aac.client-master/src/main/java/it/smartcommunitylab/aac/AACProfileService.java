package it.smartcommunitylab.aac;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import it.smartcommunitylab.aac.model.AccountProfile;
import it.smartcommunitylab.aac.model.BasicProfile;

public class AACProfileService {

	private String aacURL;
	
	private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param aacURL URL of the AAC service
     */
	public AACProfileService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) {
			this.aacURL += '/';
		}
	}	
	
	public Collection<BasicProfile> searchUsersByUsername(String token, String username) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/all?username="+username;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                List<?> data = mapper.readValue(response, List.class);
                    Set<BasicProfile> result = (Set<BasicProfile>) data.stream().map(x -> mapper.convertValue(x, BasicProfile.class)).collect(Collectors.toSet());
                    return result;	                
	            }
	            throw new AACException("Error in basicprofile/all " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	public Collection<BasicProfile> searchUsersByFullname(String token, String name) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/all?filter="+name;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    List<?> data = mapper.readValue(response, List.class);
                    Set<BasicProfile> result = (Set<BasicProfile>) data.stream().map(x -> mapper.convertValue(x, BasicProfile.class)).collect(Collectors.toSet());
                    return result;    
	            }
	            throw new AACException("Error in basicprofile/all " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}
	
	public Collection<BasicProfile> findProfiles(String token, List<String> userIds) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/profiles?userIds=" + Joiner.on(",").join(userIds);
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    List<?> data = mapper.readValue(response, List.class);
                    Set<BasicProfile> result = (Set<BasicProfile>) data.stream().map(x -> mapper.convertValue(x, BasicProfile.class)).collect(Collectors.toSet());
                    return result;    
	            }
	            throw new AACException("Error in basicprofile/profiles " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public BasicProfile findProfile(String token) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/me";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	BasicProfile data = mapper.readValue(response, BasicProfile.class);
	                return data;
	            }
                throw new AACException("Error in basicprofile/me " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public BasicProfile getUser(String token, String userId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "basicprofile/all/" + userId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	BasicProfile data = mapper.readValue(response, BasicProfile.class);
	                return data;
	            }
	            throw new AACException("Error in basicprofile/all " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public Collection<AccountProfile> findAccountProfiles(String token, List<String> userIds) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "accountprofile/profiles?userIds=" + Joiner.on(",").join(userIds);
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    List<?> data = mapper.readValue(response, List.class);
                    Set<AccountProfile> result = (Set<AccountProfile>) data.stream().map(x -> mapper.convertValue(x, AccountProfile.class)).collect(Collectors.toSet());
                    return result;    
	            }
	            throw new AACException("Error in accountprofile/profiles " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}	
	
	public AccountProfile findAccountProfile(String token)  throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "accountprofile/me";
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	AccountProfile data = mapper.readValue(response, AccountProfile.class);
	                return data;
	            }
	            throw new AACException("Error in accountprofile/me " + resp.getStatusLine());
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

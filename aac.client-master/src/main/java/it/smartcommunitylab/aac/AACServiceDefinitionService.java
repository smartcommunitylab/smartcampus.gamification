package it.smartcommunitylab.aac;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.aac.model.Service;
import it.smartcommunitylab.aac.model.Service.ServiceClaim;
import it.smartcommunitylab.aac.model.Service.ServiceScope;

public class AACServiceDefinitionService {

	private String aacURL;
	
	private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param aacURL URL of the AAC service
     */
	public AACServiceDefinitionService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) this.aacURL += '/';
	}	
	
	public Service getService(String token, String serviceId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services/" + serviceId;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, Service.class);
	            }
	            throw new AACException("Error in read service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public Service saveService(String token, Service service) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services";
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(service), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, Service.class);
	            }
	            throw new AACException("Error in save service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		


	public void deleteService(String token, String serviceId) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services/" + serviceId;
	        final HttpDelete del = new HttpDelete(url);
	        del.setHeader("Accept", "application/json");
	        del.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(del);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in delete service " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	
	public ServiceScope saveScope(String token, ServiceScope scope) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services/"+ scope.getServiceId() +"/scope";
	        final HttpPut m = new HttpPut(url);
	        m.setHeader("Accept", "application/json");
	        m.setHeader("Content-Type", "application/json");
	        m.setHeader("Authorization", "Bearer " + token);
	        m.setEntity(new StringEntity(mapper.writeValueAsString(scope), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(m);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, ServiceScope.class);
	            }
	            throw new AACException("Error in save scope " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		


	public void deleteScope(String token, String serviceId, String scope) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services/" + serviceId +"/scope/" + scope;
	        final HttpDelete del = new HttpDelete(url);
	        del.setHeader("Accept", "application/json");
	        del.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(del);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in delete scope " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		
	
	public ServiceClaim saveClaim(String token, ServiceClaim claim) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services/"+ claim.getServiceId() +"/claim";
	        final HttpPut m = new HttpPut(url);
	        m.setHeader("Accept", "application/json");
	        m.setHeader("Content-Type", "application/json");
	        m.setHeader("Authorization", "Bearer " + token);
	        m.setEntity(new StringEntity(mapper.writeValueAsString(claim), "UTF-8"));

	        try {
	            resp = getHttpClient().execute(m);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return mapper.readValue(response, ServiceClaim.class);
	            }
	            throw new AACException("Error in save claim " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}		


	public void deleteClaim(String token, String serviceId, String claim) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "api/services/" + serviceId +"/claim/" + claim;
	        final HttpDelete del = new HttpDelete(url);
	        del.setHeader("Accept", "application/json");
	        del.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(del);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in delete claim " + resp.getStatusLine());
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

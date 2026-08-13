package it.smartcommunitylab.aac;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.aac.model.authorization.AuthorizationDTO;
import it.smartcommunitylab.aac.model.authorization.AuthorizationNodeDTO;
import it.smartcommunitylab.aac.model.authorization.AuthorizationResourceDTO;
import it.smartcommunitylab.aac.model.authorization.RequestedAuthorizationDTO;

public class AACAuthorizationService {

	private String aacURL;

	private ObjectMapper mapper = new ObjectMapper();
	
    /**
     * @param aacURL URL of the AAC service
     */
	public AACAuthorizationService(String aacURL) {
		super();
		this.aacURL = aacURL;
		if (!aacURL.endsWith("/")) this.aacURL += '/';
	}	

	//@RequestMapping(value = "/authorization/{domain}/{id}", method = RequestMethod.DELETE)
	public void removeAuthorization(String token, String domain, String id) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/" + id;
	        final HttpDelete delete = new HttpDelete(url);
	        delete.setHeader("Accept", "application/json");
	        delete.setHeader("Content-Type", "application/json");
	        delete.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(delete);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in delete authorization " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}

	//@RequestMapping(value = "/authorization/{domain}", method = RequestMethod.POST)
	public AuthorizationDTO insertAuthorization(String token, String domain, AuthorizationDTO authorization) throws SecurityException, AACException {
			try {
		        final HttpResponse resp;
		        String url = aacURL + "authorization/" + domain;
		        final HttpPost post = new HttpPost(url);
		        post.setHeader("Accept", "application/json");
		        post.setHeader("Content-Type", "application/json");
		        post.setHeader("Authorization", "Bearer " + token);
		        post.setEntity(new StringEntity(mapper.writeValueAsString(authorization), "UTF-8"));
		        try {
		            resp = getHttpClient().execute(post);
		            final String response = EntityUtils.toString(resp.getEntity());
		            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		            	AuthorizationDTO data = mapper.readValue(response, AuthorizationDTO.class);
		                return data;
		            }
		            throw new AACException("Error in authorization " + resp.getStatusLine());
		        } catch (final Exception e) {
		            throw new AACException(e);
		        }
			} catch (Exception e) {
				throw new AACException(e);
			}
		
		}


	//@RequestMapping(value = "/authorization/{domain}/validate", method = RequestMethod.POST)
	public boolean validateAuthorization(String token, String domain, RequestedAuthorizationDTO authorization) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/validate";
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(authorization), "UTF-8"));
	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return Boolean.parseBoolean(response);
	            }
	            throw new AACException("Error in authorization/validate " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	
	}

	//@RequestMapping(value = "/authorization/{domain}/schema", method = RequestMethod.POST)
	public void addRootChildToSchema(String token, String domain, AuthorizationNodeDTO node) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/schema";
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(node), "UTF-8"));
	        try {
	            resp = getHttpClient().execute(post);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in authorization/schema " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	
	}

	//@RequestMapping(value = "/authorization/{domain}/schema/{parentQname}", method = RequestMethod.POST)
	public void addChildToSchema(String token, String domain, AuthorizationNodeDTO childNode, String parentQname) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/schema/" + parentQname ;
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(childNode), "UTF-8"));
	        try {
	            resp = getHttpClient().execute(post);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in authorization/schema " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	
	}

	//@RequestMapping(value = "/authorization/{domain}/schema/{qname}", method = RequestMethod.GET)
	public AuthorizationNodeDTO getNode(String token, String domain, String qname) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/schema/" + qname ;
	        final HttpGet get = new HttpGet(url);
	        get.setHeader("Accept", "application/json");
	        get.setHeader("Content-Type", "application/json");
	        get.setHeader("Authorization", "Bearer " + token);
	        try {
	            resp = getHttpClient().execute(get);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	AuthorizationNodeDTO data = mapper.readValue(response, AuthorizationNodeDTO.class);
	                return data;
	            }
	            throw new AACException("Error in authorization/schema " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	
	}

	//@RequestMapping(value = "/authorization/{domain}/schema/validate", method = RequestMethod.POST)
	public boolean validateResource(String token, String domain, AuthorizationResourceDTO resource) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/validate";
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        post.setEntity(new StringEntity(mapper.writeValueAsString(resource), "UTF-8"));
	        try {
	            resp = getHttpClient().execute(post);
	            final String response = EntityUtils.toString(resp.getEntity());
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return Boolean.parseBoolean(response);
	            }
	            throw new AACException("Error in authorization/schema/validate " + resp.getStatusLine());
	        } catch (final Exception e) {
	            throw new AACException(e);
	        }
		} catch (Exception e) {
			throw new AACException(e);
		}
	}
	
	//@RequestMapping(value = "/authorization/{domain}/schema/load", method = RequestMethod.POST)
	public void loadSchema(String token, String domain, String content) throws SecurityException, AACException {
		try {
	        final HttpResponse resp;
	        String url = aacURL + "authorization/" + domain + "/schema/load";
	        final HttpPost post = new HttpPost(url);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-Type", "application/json");
	        post.setHeader("Authorization", "Bearer " + token);
	        post.setEntity(new StringEntity(content, "UTF-8"));
	        try {
	            resp = getHttpClient().execute(post);
	            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	                return;
	            }
	            throw new AACException("Error in authorization/schema/load " + resp.getStatusLine());
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

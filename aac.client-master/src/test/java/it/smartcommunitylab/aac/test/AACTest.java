package it.smartcommunitylab.aac.test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import it.smartcommunitylab.aac.AACAuthorizationService;
import it.smartcommunitylab.aac.AACProfileService;
import it.smartcommunitylab.aac.AACRoleService;
import it.smartcommunitylab.aac.AACService;
import it.smartcommunitylab.aac.AACServiceDefinitionService;
import it.smartcommunitylab.aac.AACUserClaimService;
import it.smartcommunitylab.aac.model.AACTokenIntrospection;
import it.smartcommunitylab.aac.model.AccountProfile;
import it.smartcommunitylab.aac.model.BasicProfile;
import it.smartcommunitylab.aac.model.Role;
import it.smartcommunitylab.aac.model.Service;
import it.smartcommunitylab.aac.model.Service.ServiceClaim;
import it.smartcommunitylab.aac.model.Service.ServiceScope;
import it.smartcommunitylab.aac.model.TokenData;
import it.smartcommunitylab.aac.model.User;
import it.smartcommunitylab.aac.model.authorization.AuthorizationDTO;
import it.smartcommunitylab.aac.model.authorization.RequestedAuthorizationDTO;

@Ignore
public class AACTest {

	/**
	 * 
	 */
	private static final String TESTCONTEXT = "testservice";
	private static final String USERNAME = "admin";
	private static final String PWD = "admin";
	private static final String TEST = "cartellastudente"; 
	private static final String AUTHORIZATION_TEST = "authorization:" + TEST;
	private String aacURL = "http://localhost:8080/aac";
	private String clientId = "API_MGT_CLIENT_ID";
	private String clientSecret = "";
	
	AACService aacService;
	AACProfileService profileService;
	AACRoleService roleService;
	AACAuthorizationService authService;
	AACServiceDefinitionService serviceService;
	AACUserClaimService claimService;
	
	private ObjectMapper mapper = new ObjectMapper();	
	
	@Before
	public void setup() {
		aacService = new AACService(aacURL, clientId, clientSecret);
		profileService = new AACProfileService(aacURL);
		roleService = new AACRoleService(aacURL);
		authService = new AACAuthorizationService(aacURL);
		serviceService = new AACServiceDefinitionService(aacURL);
		claimService = new AACUserClaimService(aacURL);
	}

	@Test
	public void test() throws Exception {
		TokenData data = aacService.generateUserToken(USERNAME, PWD, "openid");
		Assert.assertNotNull(data);
		
		AACTokenIntrospection intro = aacService.tokenInfo(data.getAccess_token());
		Assert.assertNotNull(intro);
		Assert.assertEquals(clientId, intro.getAud());
		Assert.assertEquals(USERNAME, intro.getUsername());
		
		Map<String, Object> userInfo = aacService.userInfo(data.getAccess_token());
		Assert.assertNotNull(userInfo);
		Assert.assertEquals(USERNAME, userInfo.get("username"));
		
	}
	
	@Test
	public void testProfileClient() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all profile.accountprofile.all").getAccess_token();
		
		Collection<BasicProfile> basicProfiles = profileService.searchUsersByUsername(clientToken, USERNAME);
		Assert.assertNotEquals(0, basicProfiles.size());
		BasicProfile profile = basicProfiles.iterator().next();
		
		basicProfiles = profileService.searchUsersByFullname(clientToken, USERNAME);
		Assert.assertNotEquals(0, basicProfiles.size());
		
		basicProfiles = profileService.findProfiles(clientToken, Lists.newArrayList(profile.getUserId()));
		Assert.assertEquals(1, basicProfiles.size());
		
		Collection<AccountProfile> accountProfiles = profileService.findAccountProfiles(clientToken, Lists.newArrayList(Lists.newArrayList(profile.getUserId())));
		Assert.assertEquals(1, accountProfiles.size());		
	}
	
	@Test
	public void testProfileUser() throws Exception {
		String userToken = aacService.generateUserToken(USERNAME, PWD, "profile.basicprofile.all profile.basicprofile.me profile.accountprofile.me").getAccess_token();
		String clientToken = aacService.generateClientToken("profile.basicprofile.all").getAccess_token();
		
		Collection<BasicProfile> basicProfiles = profileService.searchUsersByUsername(clientToken, USERNAME);
		BasicProfile profile = basicProfiles.iterator().next();
		
		BasicProfile basicProfile = profileService.getUser(userToken, profile.getUserId());
		Assert.assertNotNull(basicProfile);
		
		basicProfile = profileService.findProfile(userToken);
		Assert.assertNotNull(basicProfile);		
		
		AccountProfile accountProfile = profileService.findAccountProfile(userToken);
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountProfile));
		Assert.assertNotNull(accountProfile);				
	}	
	
	@Test
	public void testRoleUser() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all profile.accountprofile.all user.roles.write user.roles.read.all user.roles.read client.roles.read.all").getAccess_token();
		
		Collection<BasicProfile> basicProfiles = profileService.searchUsersByUsername(clientToken, USERNAME);
		Assert.assertNotEquals(0, basicProfiles.size());
		BasicProfile profile = basicProfiles.iterator().next();
		
		String userToken = aacService.generateUserToken(USERNAME, PWD, "user.roles.me").getAccess_token();
		
		Collection<Role> roles = roleService.getRoles(userToken);
		int rolesN = roles.size();
		
		roleService.addRoles(clientToken, profile.getUserId(), Lists.newArrayList("apimanager/carbon.super:testrole"));
		roles = roleService.getRoles(userToken);
		Assert.assertEquals(rolesN + 1, roles.size());		
		
		roleService.deleteRoles(clientToken, profile.getUserId(), Lists.newArrayList("apimanager/carbon.super:testrole"));
		roles = roleService.getRoles(userToken);
		Assert.assertEquals(rolesN, roles.size());		
		Set<String> ownedSpaces = roles.stream().filter(r -> r.getRole().equals("ROLE_PROVIDER")).map(r -> r.canonicalSpace()).collect(Collectors.toSet());
		long extraRoles = roles.stream().filter(r -> !ownedSpaces.contains(r.canonicalSpace())).count();
		
		// should return roles minus roles in not owned spaces
		roles = roleService.getRolesByUserId(clientToken, profile.getUserId());
		Assert.assertEquals(rolesN - extraRoles, roles.size());			
		
		roles = roleService.getClientRoles(clientToken);
		Assert.assertEquals(rolesN, roles.size());		
		
		Collection<User> users = roleService.getSpaceUsers("components/apimanager/carbon.super", null, false, 0, 10, clientToken);
		Assert.assertEquals(1, users.size());
		Assert.assertEquals(4, users.iterator().next().getRoles().size());
		
		users = roleService.getSpaceUsers("components/apimanager", null, true, 0, 10, clientToken);
		Assert.assertNotEquals(0, users.size());
	}	
	
	@Test
	public void testAuthorization() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all user.roles.write authorization.manage authorization.schema.manage").getAccess_token();
		
		Collection<BasicProfile> basicProfiles = profileService.searchUsersByUsername(clientToken, USERNAME);
		Assert.assertNotEquals(0, basicProfiles.size());
		BasicProfile profile = basicProfiles.iterator().next();
		
		roleService.addRoles(clientToken, profile.getUserId(), Lists.newArrayList(AUTHORIZATION_TEST));
		
		String json = Resources.toString(Resources.getResource("load.json"), Charsets.UTF_8);
		authService.loadSchema(clientToken, TEST, json);
		
		json = Resources.toString(Resources.getResource("auth.json"), Charsets.UTF_8);
		AuthorizationDTO auth = mapper.readValue(json, AuthorizationDTO.class);
		authService.insertAuthorization(clientToken, TEST, auth);
		
		json = Resources.toString(Resources.getResource("validate.json"), Charsets.UTF_8);
		RequestedAuthorizationDTO reqAuth = mapper.readValue(json, RequestedAuthorizationDTO.class);		
		boolean res = authService.validateAuthorization(clientToken, TEST, reqAuth);
		System.err.println(res);
		
		roleService.deleteRoles(clientToken, profile.getUserId(), Lists.newArrayList(AUTHORIZATION_TEST));
	}

	@Test
	public void testServicesAndClaims() throws Exception {
		String clientToken = aacService.generateClientToken("profile.basicprofile.all user.roles.write authorization.manage authorization.schema.manage servicemanagement claimmanagement").getAccess_token();

		Service svc = new Service();
		svc.setContext(TESTCONTEXT);
		svc.setName("name");
		svc.setNamespace("namespace");
		svc.setDescription("description");
		svc.setServiceId("mytestservice");
		
		// create service
		Service res = serviceService.saveService(clientToken, svc);
		Assert.assertEquals(svc.getServiceId(), res.getServiceId());

		// create scope
		ServiceScope scope = new ServiceScope();
		scope.setScope("scope1");
		scope.setName("name");
		scope.setDescription("description");
		scope.setServiceId(svc.getServiceId());
		serviceService.saveScope(clientToken, scope);
		res = serviceService.getService(clientToken, svc.getServiceId());
		Assert.assertEquals(1, res.getScopes().size());

		// create claim
		ServiceClaim claim = new ServiceClaim();
		claim.setClaim("claim1");
		claim.setName("claim name");
		claim.setServiceId(svc.getServiceId());
		claim.setType("object");
		serviceService.saveClaim(clientToken, claim);
		res = serviceService.getService(clientToken, svc.getServiceId());
		Assert.assertEquals(1, res.getClaims().size());

		
		// delete scope
		serviceService.deleteScope(clientToken, svc.getServiceId(), scope.getScope());
		res = serviceService.getService(clientToken, svc.getServiceId());
		Assert.assertEquals(0, res.getScopes().size());
		
		// delete claim
		serviceService.deleteClaim(clientToken, svc.getServiceId(), claim.getClaim());
		res = serviceService.getService(clientToken, svc.getServiceId());
		Assert.assertEquals(0, res.getClaims().size());

		// delete service
		serviceService.deleteService(clientToken, svc.getServiceId());
		
	}
	

}

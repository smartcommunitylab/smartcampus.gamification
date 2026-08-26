package it.smartcommunitylab.aac.model;

import java.io.Serializable;

public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5989316043413301379L;

	/** Predefined system role USER */
	public static final String R_USER = "ROLE_USER";
	/** Predefined system role ADMIN */
	public static final String R_ADMIN = "ROLE_ADMIN";	

	private Long id;
	
	private String context;
	private String space;
	private String role;
	
	private String authority;

	public Role() {
		super();
	}

	public Role(String context, String space, String role) {
		super();
		this.context = context;
		this.space = space;
		this.role = role;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", context=" + context + ", space=" + space + ", role=" + role + "]";
	}
	
	public String getAuthority() {
		if (authority == null) {
			StringBuilder sb = new StringBuilder();
			if (!isEmpty(context)) {
				sb.append(context);
				sb.append('/');
			}
			if (!isEmpty(space)) {
				sb.append(space);
				sb.append(':');
			}
			sb.append(role);
			authority = sb.toString();
		}
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((space == null) ? 0 : space.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Role))
			return false;
		Role other = (Role) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (space == null) {
			if (other.space != null)
				return false;
		} else if (!space.equals(other.space))
			return false;
		return true;
	}
	


	public String canonicalSpace() {
		StringBuilder sb = new StringBuilder();
		if (!isEmpty(context)) {
			sb.append(context);
			sb.append('/');
		}
		if (!isEmpty(space)) {
			sb.append(space);
		}
		return sb.toString();
	}
	
	public String asSlug() {
		return canonicalSpace().replace('/', '-');
	}
	
	/**
	 * @param ctxStr
	 * @return
	 */
	public static Role memberOf(String ctxStr, String role) {
		int idx = ctxStr.lastIndexOf('/');
		String ctx = idx > 0 ? ctxStr.substring(0,  idx) : null;
		String space = idx > 0 ? ctxStr.substring(idx + 1) : ctxStr;
		Role r = new Role(ctx, space, role);
		validate(r);
		return r;
	}

	/**
	 * @param x
	 * @return
	 */
	public static Role parse(String s) throws IllegalArgumentException {
		s = s.trim();
		int idx = s.lastIndexOf(':');
		if (isEmpty(s) || idx == s.length() - 1) throw new IllegalArgumentException("Invalid Role format " + s);
		if (idx <= 0) return new Role(null, null, s.substring(idx + 1));
		return memberOf(s.substring(0, idx), s.substring(idx + 1));
	}

	public static void validate(Role r) throws IllegalArgumentException {
		// context may be empty
		if (r.context != null && !r.context.matches("[\\w\\./]+")) {
			throw new IllegalArgumentException("Invalid role context value: only alpha-numeric characters and '_./' allowed");
		};
		// space empty only if context is empty
		if (r.space == null && r.context != null || r.space != null && !r.space.matches("[\\w\\.]+")) {
			throw new IllegalArgumentException("Invalid role space value: only alpha-numeric characters and '_.' allowed");
		};
		// role should never be empty
		if (r.role == null || !r.role.matches("[\\w\\.]+")) {
			throw new IllegalArgumentException("Invalid role value: only alpha-numeric characters and '_.' allowed");
		};
	}
	
	private static boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}
	
}

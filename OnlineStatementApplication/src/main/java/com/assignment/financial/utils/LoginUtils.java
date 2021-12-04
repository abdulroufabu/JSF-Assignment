package com.assignment.financial.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * @author abdul.abu
 *
 */
public class LoginUtils {
	

	public static final String USER = "user";
	public static final String ADMIN = "admin";
	
	public enum Role {
		ADMIN,
		USER;
	}
	
	public enum Feature {
		NONE,
		SEARCH_BY_DATE,
		SEARCH_BY_AMOUNT,
		SEARCH_BY_ACCOUNT_ID;
	}
	
	private static Map<Role , List<Feature>> rolefeatures ;
	private static Map<Role,List<String>> roleUsers;
	static {
		mapRolesByUser();
		mapFeaturesByRole();
	}
	
	private static void mapRolesByUser() {
		
		roleUsers = new HashMap<Role, List<String>>();
		
		List<String> adminNames = new ArrayList<String>();
		adminNames.add(ADMIN);
		roleUsers.put(Role.ADMIN, adminNames);
		
		List<String> userNames = new ArrayList<String>();
		userNames.add(USER);
		roleUsers.put(Role.USER, userNames);
	}
	
	private static void mapFeaturesByRole() {
		
		rolefeatures = new HashMap<Role, List<Feature>>();
		
		List<Feature> adminFeatures = new ArrayList<Feature>();
		adminFeatures.add(Feature.SEARCH_BY_ACCOUNT_ID);
		adminFeatures.add(Feature.SEARCH_BY_AMOUNT);
		adminFeatures.add(Feature.SEARCH_BY_DATE);
		rolefeatures.put(Role.ADMIN, adminFeatures);
		
		List<Feature> userFeatures = new ArrayList<Feature>();
		userFeatures.add(Feature.SEARCH_BY_ACCOUNT_ID);
		rolefeatures.put(Role.USER, userFeatures);
	}

	public static Map<Role, List<Feature>> getRolefeatures() {
		return rolefeatures;
	}

	public static Map<Role, List<String>> getRoleUsers() {
		return roleUsers;
	}
	
	public static HashSet<Feature> getFeatures(List<Role> roles){
		
		HashSet<Feature> features = new HashSet<Feature>();
		
		for(Role role : roles) {
			if (!rolefeatures.containsKey(role)) {
				continue;
			}
			features.addAll(rolefeatures.get(role));
		}
		return features;
	}
	
	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean login(String username, String password) {

		if (username.equals(LoginUtils.ADMIN) && password.equals(LoginUtils.ADMIN)) {
			return true;
		} else if (username.equals(LoginUtils.USER) && password.equals(LoginUtils.USER)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param userName
	 * @return
	 */
	public static boolean isCallerAlreadyLoggedIn(String userName) {
		
		HttpSession session = SessionUtils.getSession();
		if (session.getAttribute("username") != null) {
			String username = session.getAttribute("username").toString();
			if (!Utils.isVoid(username) && username.equals(userName)) {
				return true;
			}
		}
		return false;
	}

}

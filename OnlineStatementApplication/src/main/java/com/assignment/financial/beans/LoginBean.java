package com.assignment.financial.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.assignment.financial.utils.LoginUtils;
import com.assignment.financial.utils.LoginUtils.Role;
import com.assignment.financial.utils.SessionUtils;

@ManagedBean(name= "login")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	 private static Logger smLogger = Logger.getLogger(LoginBean.class);
	
	private String pwd;
	private String msg;
	private String userName;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Produces
    public FacesContext produceFacesContext() {
           return FacesContext.getCurrentInstance();
    }

	//validate login
	public String login() {
		
		boolean valid = LoginUtils.login(userName, pwd);
		if (valid) {
			
			if (LoginUtils.isCallerAlreadyLoggedIn(userName)) {
				
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"User already logged in,",
								"Please logout."));
				return "login";
			}
			
			smLogger.info(String.format("User [%s] logged in successfully.", userName));
			
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", userName);
			List<Role> roles = LoginUtils.getRoleUsers().entrySet().stream()
					  .filter(e -> e.getValue().contains(userName))
					  .map(Map.Entry::getKey)
					  .collect(Collectors.toList());
			session.setAttribute("ROLE",roles);
			return "search";
			
		} else {
			smLogger.warn(String.format("Failed login with user [%s]", userName));
			smLogger.warn("Invalid credentials, Please try again !");
			
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Invalid Username and Passowrd",
							"Please enter valid username and Password"));
			return "login";
		}
	}

	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}

}

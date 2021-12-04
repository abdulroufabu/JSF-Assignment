package com.assignment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.assignment.financial.beans.LoginBean;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FacesContext.class, HttpSession.class})
@PowerMockIgnore("jdk.internal.reflect.*")
public class LoginBeanTest {

  private LoginBean loginBean;

  @Mock
  private FacesContext facesContext;
  @Mock
  private ExternalContext externalContext;
  @Mock
  private HttpSession session;
  

  @Before
  public void setUp() throws Exception {
    loginBean = new LoginBean();

    // mock all static methods of FacesContext using PowerMockito
    PowerMockito.mockStatic(FacesContext.class);

    when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
    when(facesContext.getExternalContext()).thenReturn(externalContext);
    when(FacesContext.getCurrentInstance()
			.getExternalContext().getSession(false)).thenReturn(session);
  }

  @Test
  public void testFailedLogin() {
    // create Captor instances for the clientId and FacesMessage parameters
    // that will be added to the FacesContext
    ArgumentCaptor<String> clientIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<FacesMessage> facesMessageCaptor = ArgumentCaptor.forClass(FacesMessage.class);

    // run the login() method to be tested
    loginBean.setUserName("misk");
    loginBean.setPwd("1234");
    loginBean.login();

    // verify if the call to addMessage() was made and capture the arguments
    verify(facesContext).addMessage(clientIdCaptor.capture(),
        facesMessageCaptor.capture());

    // check the value of the clientId that was passed
    assertNull(clientIdCaptor.getValue());

    // retrieve the captured FacesMessage
    FacesMessage captured = facesMessageCaptor.getValue();
    // check if the captured FacesMessage contains the expected values
    assertEquals(FacesMessage.SEVERITY_WARN, captured.getSeverity());
    assertEquals("Invalid Username and Passowrd", captured.getSummary());
    assertEquals("Please enter valid username and Password", captured.getDetail());
  }

  @Test
  public void testSuccessfullLogin() {

    // run the login() method to be tested
    loginBean.setUserName("admin");
    loginBean.setPwd("admin");
    assertEquals("search", loginBean.login());
  }
}
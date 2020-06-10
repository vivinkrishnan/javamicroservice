/**
* Copyright 2020 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package dev.appsody.microservice;

import java.io.IOException;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.mfp.java.token.validator.AuthenticationError;
import com.ibm.mfp.java.token.validator.TokenValidationException;
import com.ibm.mfp.java.token.validator.TokenValidationManager;
import com.ibm.mfp.java.token.validator.TokenValidationResult;
//import com.ibm.mfp.java.token.validator.data.MobileDeviceData;

@WebFilter(urlPatterns = {"/microservice/hello"} ,
initParams= {@WebInitParam(name="scope",value="accessRestricted")}
)
public class JavaMicroServiceFilter implements Filter {

    public static final String AUTH_HEADER = "Authorization";
	private static final String AUTHSERVER_URI = "http://<serverhost>:<port>/mfp/api"; //Set here your authorization server URI
	private static final String CLIENT_ID = "testclient"; //Set here your confidential client ID
	private static final String CLIENT_SECRET = "testclient"; //Set here your confidential client SECRET

	private TokenValidationManager validator;
	private FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		URI uri = null;
		try {
			uri = new URI(AUTHSERVER_URI);
			validator = new TokenValidationManager(uri, CLIENT_ID, CLIENT_SECRET);
			this.filterConfig = filterConfig;
		} catch (Exception e1) {
			System.out.println("Error reading introspection URI");
		}
	}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // TODO Auto-generated method stub
        String expectedScope = filterConfig.getInitParameter("scope");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String authCredentials = httpServletRequest.getHeader(AUTH_HEADER);
		
		try {
            TokenValidationResult tokenValidationRes = validator.validate(authCredentials, expectedScope);
			if (tokenValidationRes.getAuthenticationError() != null) {

        		// Error
                AuthenticationError error = tokenValidationRes.getAuthenticationError();
                
        		httpServletResponse.setStatus(error.getStatus());
				httpServletResponse.setHeader("WWW-Authenticate", error.getAuthenticateHeader());
			} else if (tokenValidationRes.getIntrospectionData() != null) {
                // Success
        		httpServletRequest.setAttribute("introspection-data", tokenValidationRes.getIntrospectionData());
				filterChain.doFilter(request, response);
            }
		} catch (TokenValidationException e) {
            e.printStackTrace();
			httpServletResponse.setStatus(500);
        }
        catch (Exception e) {
            e.printStackTrace();
			httpServletResponse.setStatus(500);
		}

    }
    
    
}
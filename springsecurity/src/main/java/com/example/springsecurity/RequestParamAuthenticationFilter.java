/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.springsecurity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.util.Assert;

/**
 * A simple pre-authenticated filter which obtains the username from a request header, for
 * use with systems such as CA Siteminder.
 * <p>
 * As with most pre-authenticated scenarios, it is essential that the external
 * authentication system is set up correctly as this filter does no authentication
 * whatsoever. All the protection is assumed to be provided externally and if this filter
 * is included inappropriately in a configuration, it would be possible to assume the
 * identity of a user merely by setting the correct header name. This also means it should
 * not generally be used in combination with other Spring Security authentication
 * mechanisms such as form login, as this would imply there was a means of bypassing the
 * external system which would be risky.
 * <p>
 * The property {@code principalRequestHeader} is the name of the request header that
 * contains the username. It defaults to "SM_USER" for compatibility with Siteminder.
 * <p>
 * If the header is missing from the request, {@code getPreAuthenticatedPrincipal} will
 * throw an exception. You can override this behaviour by setting the
 * {@code exceptionIfHeaderMissing} property.
 *
 *
 * @author Luke Taylor
 * @since 2.0
 */
public class RequestParamAuthenticationFilter extends
		AbstractPreAuthenticatedProcessingFilter {
	private String principalRequestParam = "SM_USER";
	private String credentialsRequestParam;
	private boolean exceptionIfHeaderMissing = true;

	/**
	 * Read and returns the header named by {@code principalRequestHeader} from the
	 * request.
	 *
	 * @throws PreAuthenticatedCredentialsNotFoundException if the header is missing and
	 * {@code exceptionIfHeaderMissing} is set to {@code true}.
	 */
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String principal = request.getParameter(principalRequestParam);

		if (principal == null && exceptionIfHeaderMissing) {
			throw new PreAuthenticatedCredentialsNotFoundException(principalRequestParam
					+ " param not found in request.");
		}

		return principal;
	}

	/**
	 * Credentials aren't usually applicable, but if a {@code credentialsRequestHeader} is
	 * set, this will be read and used as the credentials value. Otherwise a dummy value
	 * will be used.
	 */
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		if (credentialsRequestParam != null) {
			return request.getHeader(credentialsRequestParam);
		}

		return "N/A";
	}

	public void setPrincipalRequestParam(String principalRequestParam) {
		Assert.hasText(principalRequestParam,
				"principalRequestParam must not be empty or null");
		this.principalRequestParam = principalRequestParam;
	}

	public void setCredentialsRequestHeader(String credentialsRequestParam) {
		Assert.hasText(credentialsRequestParam,
				"credentialsRequestParam must not be empty or null");
		this.credentialsRequestParam = credentialsRequestParam;
	}

	/**
	 * Defines whether an exception should be raised if the principal header is missing.
	 * Defaults to {@code true}.
	 *
	 * @param exceptionIfHeaderMissing set to {@code false} to override the default
	 * behaviour and allow the request to proceed if no header is found.
	 */
	public void setExceptionIfHeaderMissing(boolean exceptionIfHeaderMissing) {
		this.exceptionIfHeaderMissing = exceptionIfHeaderMissing;
	}
}

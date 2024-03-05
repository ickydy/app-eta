package org.edupoll.app.config.support;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.edupoll.app.common.CustomUserDetails;
import org.edupoll.app.entity.Account;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		String redirectUri = request.getParameter("redirectUri");

		if (redirectUri != null) {
			response.sendRedirect(request.getContextPath() + redirectUri);
			return;
		}
		super.onAuthenticationSuccess(request, response, authentication);

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Account account = customUserDetails.getAccount();

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("type", "alreadyLogin");
		payload.put("account", account);
		simpMessagingTemplate.convertAndSend("/push/" + account.getId(), payload);
	}

}
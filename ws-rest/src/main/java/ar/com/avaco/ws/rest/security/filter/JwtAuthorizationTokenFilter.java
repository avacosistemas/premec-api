package ar.com.avaco.ws.rest.security.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.com.avaco.ws.rest.security.service.impl.JwtUserDetailsService;
import ar.com.avaco.ws.rest.security.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Resource(name = "jwtUserDetailsService")
	private JwtUserDetailsService jwtUserDetailsService;

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			final String requestHeader = request.getHeader(this.tokenHeader);

			String username = null;
			String authToken = null;

			if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
				authToken = requestHeader.substring(7);
				try {
					username = jwtTokenUtil.getUsernameFromToken(authToken);
				} catch (ExpiredJwtException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

				if (jwtTokenUtil.validateToken(authToken, userDetails)) {

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

		} finally {
			chain.doFilter(request, response);
		}
	}
}

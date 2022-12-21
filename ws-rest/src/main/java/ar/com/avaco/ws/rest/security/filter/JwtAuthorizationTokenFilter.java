package ar.com.avaco.ws.rest.security.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.com.avaco.ws.rest.security.service.impl.JwtUserDetailsService;
import ar.com.avaco.ws.rest.security.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired(required=false)
    @Value("${jwt.header}")
    private String tokenHeader;
    private JwtUserDetailsService jwtUserDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    
    

    public JwtAuthorizationTokenFilter() {

	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
		        logger.debug("processing authentication for '{}'", request.getRequestURL());
		
		        final String requestHeader = request.getHeader(this.tokenHeader);
		
		        String username = null;
		        String authToken = null;
		        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
		            authToken = requestHeader.substring(7);
		            try {
		                username = jwtTokenUtil.getUsernameFromToken(authToken);
		            } catch (IllegalArgumentException e) {
		                logger.error("an error occured during getting username from token", e);
		            } catch (ExpiredJwtException e) {
		                logger.warn("the token is expired and not valid anymore", e);
		            }
		        } else {
		            logger.warn("couldn't find bearer string, will ignore the header");
		        }
		
		        logger.debug("checking authentication for user '{}'", username);
		        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		            logger.debug("security context was null, so authorizating user");
		
		            // It is not compelling necessary to load the use details from the database. You could also store the information
		            // in the token and read it from it. It's up to you ;)				
		            	UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
					
		
			            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
			            // the database compellingly. Again it's up to you ;)
			            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
			                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			                logger.info("authorizated user '{}', setting security context", username);
			                SecurityContextHolder.getContext().setAuthentication(authentication);
			            }
        }

		} catch (UsernameNotFoundException e) {
			logger.error("unauthorizated request");
		}finally {			
			chain.doFilter(request, response);
		}
		
    }
    
	@Resource(name = "jwtUserDetailsService")
	public void setJwtUserDetailsService(JwtUserDetailsService jwtUserDetailsService) {
		this.jwtUserDetailsService = jwtUserDetailsService;
	}
}
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.com.avaco.ws.rest.security.dto.UserAuthorisedDTO;
import ar.com.avaco.ws.rest.security.service.impl.CacheUserDetailsService;

public class AuthorizationTokenCheck extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired(required=false)
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${security.endpoint}")
    private String securityEndpoint;

    @Resource(name="cacheUserDetailsService")
    private CacheUserDetailsService cacheUserDetailsService;  
    
    public AuthorizationTokenCheck() {
	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
		
	        logger.debug("processing authentication for '{}'", request.getRequestURL());
	        final String requestHeader = request.getHeader(this.tokenHeader);
	
	        String authToken = null;
	        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
	            authToken = requestHeader.substring(7);
	            
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            headers.set("Authorization","Bearer "  + authToken);
	            HttpEntity<String> entity = new HttpEntity<String>(headers);
	            RestTemplate restTemplate = new RestTemplate();
	            ResponseEntity<UserAuthorisedDTO> wsSecResp = restTemplate.exchange(securityEndpoint,HttpMethod.GET,entity,UserAuthorisedDTO.class);
	            
	            if(HttpStatus.OK.equals(wsSecResp.getStatusCode())) {            	
	            	UserAuthorisedDTO userDto = wsSecResp.getBody();
	            	logger.debug("checking authentication for user '{}'", userDto.getUsername());
	                if (SecurityContextHolder.getContext().getAuthentication() == null) {
	                    logger.debug("security context was null, so authorizating user");
	                    // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
	                    // the database compellingly. Again it's up to you ;)
//	                    UserAuthorised userAuth = new UserAuthorised();
//	                    userAuth.setAccountNoExpired(userDto.isAccountNoExpired());
//	                    userAuth.setAccountNonLocked(userDto.isAccountNonLocked());
//	                    userAuth.setCredentialsNonExpired(userDto.isCredentialsNonExpired());
//	                    userAuth.setEnabled(userDto.isEnabled());
//	                    userAuth.setUsername(userDto.getUsername());
//	                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(); 
////	                    for (Grande authority : userDto.getAuthorities()) {
////	                    	authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
////	                    }
//	                    userAuth.setAuthorities(authorities);
	                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto, null, userDto.getAuthorities());
	                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    logger.info("authorizated user '{}', setting security context", userDto.getUsername());
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	
	            }else {                
	            	logger.warn(" the authentication web service has responded with the error code " + wsSecResp.getStatusCodeValue());
	            }
	            
	        } else {
	            logger.warn("couldn't find bearer string, will ignore the header");
	        }
		} catch (HttpClientErrorException e) {
			logger.error(" the authentication web service has responded with the error code " + e.getRawStatusCode());
		}finally {			
			chain.doFilter(request, response);
		}
	
    }
}
package ar.com.avaco.commons.service.captcha;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import ar.com.avaco.commons.exception.ReCaptchaInvalidException;

public abstract class AbstractCaptchaService implements ReCaptchaService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractCaptchaService.class);
    
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected ReCaptchaSettings captchaSettings;

    @Autowired
    protected ReCaptchaAttempt reCaptchaAttemptService;

    @Autowired
    protected RestTemplate restTemplate;

    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    
    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
    
   
    @Override
    public String getReCaptchaSite() {
        return captchaSettings.getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }
  
    protected void securityCheck(final String response) {
        LOGGER.debug("Intentando validar el response {}", response);

        if (reCaptchaAttemptService.isBlocked(getClientIP())) {
            throw new ReCaptchaInvalidException("El cliente superó el número máximo de intentos fallidos");
        }

        if (!responseSanityCheck(response)) {
            throw new ReCaptchaInvalidException("El response contiene caracteres no válidos");
        }
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    protected String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
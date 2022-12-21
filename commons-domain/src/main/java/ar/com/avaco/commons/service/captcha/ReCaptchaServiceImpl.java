package ar.com.avaco.commons.service.captcha;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service("captchaService")
public class ReCaptchaServiceImpl extends AbstractCaptchaService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReCaptchaServiceImpl.class);
    
    @Override
    public boolean verifyRecaptcha(String response) {
        securityCheck(response);
        boolean reCaptchaOk = true;
        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            LOGGER.debug("Google's response: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                reCaptchaOk = false;
                //throw new ReCaptchaInvalidException("reCaptcha no se validó con éxito");
            }
        } catch (RestClientException rce) {
        	reCaptchaOk = false;
        	//throw new ReCaptchaUnavailableException("Registracion no disponible en este momento. Por favor, inténtelo de nuevo más tarde.", rce);
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
        
        return reCaptchaOk;
    }
   
    
}
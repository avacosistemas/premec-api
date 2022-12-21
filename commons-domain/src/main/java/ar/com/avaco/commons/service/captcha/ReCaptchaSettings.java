package ar.com.avaco.commons.service.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReCaptchaSettings {

	@Value("${google.recaptcha.key.site}")
    private String site;
    
    @Value("${google.recaptcha.secret}")
    private String secret;

    public ReCaptchaSettings() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    
}
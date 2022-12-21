package ar.com.avaco.commons.service.captcha;

public interface ReCaptchaService {
    
	boolean verifyRecaptcha(String response);

    String getReCaptchaSite();

    String getReCaptchaSecret();

}
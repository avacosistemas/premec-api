package ar.com.avaco.ws.rest.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.avaco.commons.service.captcha.ReCaptchaService;

@RestController
public class ReCaptchaController {
	
    @Autowired
    private ReCaptchaService captchaService;

    public ReCaptchaController() {
        super();
    }

    @RequestMapping(value = "/recaptcha", method = RequestMethod.POST)
    public boolean captchaRegister(@RequestParam(name = "g-recaptcha-response") String captchaResponse, HttpServletRequest request) {
    	
        return captchaService.verifyRecaptcha(captchaResponse);

    }

}
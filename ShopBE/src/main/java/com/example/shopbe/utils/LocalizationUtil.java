package com.example.shopbe.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
public class LocalizationUtil {

    private MessageSource messageSource;

    private LocaleResolver localeResolver;

    public LocalizationUtil(MessageSource messageSource, LocaleResolver localeResolver){
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    public String getLocalizationMessage(String messageKey, Object ... param) {

        // get current request
        HttpServletRequest request = WebUtil.getCurrentRequest();

        // get locale from request; vn, en, jp, cn, ru.....
        Locale locale = localeResolver.resolveLocale(request);

        return messageSource.getMessage(messageKey, param, locale);
    }
}

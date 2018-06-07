package eu.trentorise.game.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import eu.trentorise.game.api.rest.AACAuthenticationInterceptor;
import it.smartcommunitylab.aac.AACProfileService;
import it.smartcommunitylab.aac.AACService;

@Configuration
@Profile("platform")
public class PlatformSecConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Value("${aac.url}")
    private String aacServiceUrl;

    @Value("${aac.clientId}")
    private String aacClientId;

    @Value("${aac.clientSecret}")
    private String aacClientSecret;

    @Bean
    public AACService aacService() {
        return new AACService(aacServiceUrl, aacClientId, aacClientSecret);
    }

    @Bean
    public AACProfileService aacProfileService() {
        return new AACProfileService(aacServiceUrl);
    }

    @Bean
    public HandlerInterceptor authInterceptor() {
        return new AACAuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/api/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").anonymous().and().csrf().disable();
    }

    public String getAacServiceUrl() {
        return aacServiceUrl;
    }

    public void setAacServiceUrl(String aacServiceUrl) {
        this.aacServiceUrl = aacServiceUrl;
    }

    public String getAacClientId() {
        return aacClientId;
    }

    public void setAacClientId(String aacClientId) {
        this.aacClientId = aacClientId;
    }

    public String getAacClientSecret() {
        return aacClientSecret;
    }

    public void setAacClientSecret(String aacClientSecret) {
        this.aacClientSecret = aacClientSecret;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // TODO Auto-generated method stub

    }

    @Override
    public Validator getValidator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configureHandlerExceptionResolvers(
            List<HandlerExceptionResolver> exceptionResolvers) {
        // TODO Auto-generated method stub

    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // TODO Auto-generated method stub

    }
}

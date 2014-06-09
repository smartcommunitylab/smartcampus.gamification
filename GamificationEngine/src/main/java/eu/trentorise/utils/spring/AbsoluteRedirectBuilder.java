package eu.trentorise.utils.spring;

public class AbsoluteRedirectBuilder implements IRedirectBuilder {

    public static final String REDIRECT_COMMAND = "redirect:";
           
    private String webappDomainAbsolute;
    
    @Override
    public String buildRedirect(String absoluteView) {
        StringBuilder sb = new StringBuilder(REDIRECT_COMMAND);
        sb.append(webappDomainAbsolute);
        sb.append(absoluteView);

        return sb.toString();
    }

    public void setWebappDomainAbsolute(String webappDomainAbsolute) {
        this.webappDomainAbsolute = webappDomainAbsolute;
    }
}
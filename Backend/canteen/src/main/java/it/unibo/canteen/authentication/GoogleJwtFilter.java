package it.unibo.canteen.authentication;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GoogleJwtFilter extends GenericFilterBean {
    private static String bearerPrefix = "Bearer ";
    private GoogleJwtVerifier verifier = new GoogleJwtVerifier();

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String authorizationHeader = httpRequest.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith(bearerPrefix)) {
                throw new IOException("Only Bearer tokens are supported");
            }
            try {
                AuthUserData authUserData = verifier.verify(authorizationHeader.substring(bearerPrefix.length()));
                httpRequest.setAttribute(AuthUserData.ATTR_NAME, authUserData);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        chain.doFilter(request, response);
    }
}

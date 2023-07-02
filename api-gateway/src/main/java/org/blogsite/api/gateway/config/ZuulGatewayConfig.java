package org.blogsite.api.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.blogsite.api.gateway.service.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class ZuulGatewayConfig extends ZuulFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/auth-api") || requestUri.contentEquals("/blog-api/api/v1/blogsite/user/getall") || requestUri.contentEquals("/blog-api/swagger-ui.html")){
            return null;
        }
        if(requestUri.startsWith("/blog-api")){
            String jwtToken = request.getHeader("Authorization");
            if(jwtToken!=null && jwtToken.startsWith("Bearer ")){
                String token = jwtToken.substring(7);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", jwtToken);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                try {
                    Boolean val = jwtTokenUtil.validateToken(token);
                    return null;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setResponseBody("Unauthorized access for "+requestUri);
        ctx.setSendZuulResponse(false);
        return null;
    }
}

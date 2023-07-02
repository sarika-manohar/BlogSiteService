package org.blogsite.api.gateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class ZuulGatewayConfig extends ZuulFilter {

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
        if(requestUri.startsWith("/auth-api")){
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
                ResponseEntity<Void> response = restTemplate.exchange("http://zuul-gateway-service/auth-api/api/v1/blogsite/user/validate", HttpMethod.GET,entity, Void.class);
                if(response.getStatusCode()== HttpStatus.OK){
                    return null;
                }
            }
        }
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setResponseBody("Unauthorized access for "+requestUri);
        ctx.setSendZuulResponse(false);
        return null;
    }
}

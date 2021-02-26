package kr.co.divus.home.config;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.ictcenter.govngolf.interceptor.AuthenticationInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /*
     * @Autowired private AuthenticationInterceptor authenticationInterceptor;
     * 
     * @Override public void addInterceptors(InterceptorRegistry registry) { // 토큰
     * 확인할 url 인걸 추가 //
     * registry.addInterceptor(authenticationInterceptor).addPathPatterns(
     * "/api/users/me"); //
     * registry.addInterceptor(authenticationInterceptor).addPathPatterns(
     * "/api/adminUsers/*"); }
     */

    @Value("${api.photo.path}")
    public String LOCAL_UPLOAD;

    @Value("${api.excel.path}")
    public String LOCAL_EXCEL_UPLOAD;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photo/**").addResourceLocations("file:///" + LOCAL_UPLOAD)
                .setCachePeriod(0);

        registry.addResourceHandler("/excel/**").addResourceLocations("file:///" + LOCAL_EXCEL_UPLOAD)
        .setCachePeriod(0);
    }
}

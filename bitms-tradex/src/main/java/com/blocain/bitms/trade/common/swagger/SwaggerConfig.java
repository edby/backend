package com.blocain.bitms.trade.common.swagger;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig Introduce
 * <p>File：SwaggerConfig.java</p>
 * <p>Title: SwaggerConfig</p>
 * <p>Description: SwaggerConfig</p>
 * <p>Copyright: Copyright (c) 2017/7/8</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@EnableSwagger2
public class SwaggerConfig
{
    @Bean
    public Docket buildDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInf(){
        Contact contact = new Contact("BitMS", "http://www.blocain.com", "service@blocain.com");
        return new ApiInfoBuilder()
                .title("BitMS交易平台在线API文档")
                .termsOfServiceUrl("http://www.blocain.com")
                .description("API documents generated based on code annotations")
                .contact(contact)
                .build();

    }
}

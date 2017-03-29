package eu.trentorise.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("eu.trentorise.game.api.rest"))
				.paths(PathSelectors.ant("/**")).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Gamification Engine API", null, "v1.0", null,
				new Contact("FBK Smartcommunitylab HII", "http://fbk.eu", null), "APACHE LICENSE 2.0",
				"http://www.apache.org/licenses/LICENSE-2.0");
		return apiInfo;
	}
}

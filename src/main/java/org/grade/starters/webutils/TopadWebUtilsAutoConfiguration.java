package org.grade.starters.webutils;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import org.grade.starters.webutils.bean.SwaggerUIRedirectController;
import org.grade.starters.webutils.bean.TopadWebExceptionHandler;
import org.grade.starters.webutils.bean.TopadWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class TopadWebUtilsAutoConfiguration {

	@Autowired
	private TypeResolver typeResolver;

	@Value("${topad.webutils.swagger.basePackage}")
	private String swaggerBasePackage;

	@Value("${app.name}")
	private String appName;

	@Value("${app.description}")
	private String appDescription;

	@Value("${app.version}")
	private String appVersion;

	@Value("${app.termsOfService}")
	private String termsOfService;

	@Value("${app.contact.name}")
	private String contactName;

	@Value("${app.contact.url}")
	private String contactUrl;

	@Value("${app.contact.email}")
	private String contactEmail;

	@Value("${app.license.name}")
	private String licenseName;

	@Value("${app.license.url}")
	private String licenseUrl;

	@Bean
	@ConditionalOnProperty("topad.webutils.swagger.enabled")
	public Docket petApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors
						.basePackage(swaggerBasePackage != null ? swaggerBasePackage : "fr.gouv.impots.appli.topad"))
				.paths(PathSelectors.any()).build().pathMapping("/").genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(newRule(
						typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, newArrayList(new ResponseMessageBuilder().code(500)
						.message("500 message").responseModel(new ModelRef("Error")).build()))
				.apiInfo(apiInfo());
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfo(appName, appDescription, appVersion, termsOfService, contactName, licenseName, licenseUrl);
	}

	@Bean
	@ConditionalOnProperty("topad.webutils.exceptionHandler.enabled")
	public TopadWebExceptionHandler webExceptionHandler() {
		return new TopadWebExceptionHandler();
	}

	@Bean
	@ConditionalOnProperty("topad.webutils.swagger.redirectUrl")
	@ConditionalOnBean(name = "topadSwaggerConfiguration")
	public SwaggerUIRedirectController swaggerUIRedirect() {
		return new SwaggerUIRedirectController();
	}

	@Bean
	public TopadWebProperties topadWebProperties() {
		return new TopadWebProperties();
	}

}

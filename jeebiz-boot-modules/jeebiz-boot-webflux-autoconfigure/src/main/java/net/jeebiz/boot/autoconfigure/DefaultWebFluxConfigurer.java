/** 
 * Copyright (C) 2018 Jeebiz (http://jeebiz.net).
 * All Rights Reserved. 
 */
package net.jeebiz.boot.autoconfigure;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.WebJarsResourceResolver;

import net.jeebiz.boot.autoconfigure.config.LocalResourceProperteis;

public class DefaultWebFluxConfigurer implements WebFluxConfigurer  {
	
	private final String META_INF_RESOURCES = "classpath:/META-INF/resources/"; 
	private final String META_INF_WEBJAR_RESOURCES = META_INF_RESOURCES + "webjars/"; 
	
    private LocalResourceProperteis localResourceProperteis;
    
    public DefaultWebFluxConfigurer(LocalResourceProperteis localResourceProperteis) {
    	this.localResourceProperteis = localResourceProperteis;
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// 本地资源映射
    	if(!CollectionUtils.isEmpty(localResourceProperteis.getLocalLocations())){
    		Iterator<Entry<String, String>> ite = localResourceProperteis.getLocalLocations().entrySet().iterator();
    		while (ite.hasNext()) {
				Entry<String, String> entry = ite.next();
				if (localResourceProperteis.isLocalRelative()) {
					registry.addResourceHandler(entry.getKey()).addResourceLocations(ResourceUtils.FILE_URL_PREFIX
							+ localResourceProperteis.getLocalStorage() + File.separator + entry.getValue());
				} else {
					registry.addResourceHandler(entry.getKey()).addResourceLocations(entry.getValue());
				}
			}
		}
    	// 指定个性化资源映射
    	registry.addResourceHandler("/assets/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/assets/");
    	registry.addResourceHandler("/js/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/js/");
    	registry.addResourceHandler("/css/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/css/");
		registry.addResourceHandler("/images/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/images/");
		if(!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations(META_INF_WEBJAR_RESOURCES)
				.resourceChain(false).addResolver(new WebJarsResourceResolver());
		}
		
	}
	
}

package br.com.geocab.application;

import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author rodrigo
 *
 */
public class ResourceBundleMessageSource extends ReloadableResourceBundleMessageSource
{
	/**
	 * 
	 * @param locale
	 * @return
	 */
	public Properties getProperties( Locale locale ) 
	{
        super.clearCacheIncludingAncestors();
        
        final PropertiesHolder propertiesHolder = super.getMergedProperties(locale);
        return propertiesHolder.getProperties();
    }
}


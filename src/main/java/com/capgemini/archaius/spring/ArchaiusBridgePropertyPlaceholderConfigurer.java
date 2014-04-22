package com.capgemini.archaius.spring;

import java.util.Properties;

import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 *
 * @author Andrew Harmel-Law
 */
public class ArchaiusBridgePropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchaiusBridgePropertyPlaceholderConfigurer.class);

    private final ArchaiusSpringPropertyPlaceholderSupport propertyPlaceholderSupport
            = new ArchaiusSpringPropertyPlaceholderSupport();
    private boolean ignoreResourceNotFound;

    // settings for dynamic property configuration
    private int initialDelayMillis = 1000;
    private int delayMillis = 1000;
    private boolean ignoreDeletesFromSource = true;


    @Override
    public void setIgnoreResourceNotFound(boolean setting) {
        ignoreResourceNotFound = setting;
        super.setIgnoreResourceNotFound(setting);
    }

    /**
     * The initial delay before the property values are re-read from the location, in milliseconds
     *
     * @param initialDelayMillis
     */
    public void setInitialDelayMillis(int initialDelayMillis) {
        this.initialDelayMillis = initialDelayMillis;
    }

    /**
     * Set the delay for the property values to re-read from the location, in milliseconds
     *
     * @param delayMillis
     */
    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * Should the dynamic property loader ignore deletes from the location source.
     *
     * @param ignoreDeletesFromSource
     */
    public void setIgnoreDeletesFromSource(boolean ignoreDeletesFromSource) {
        this.ignoreDeletesFromSource = ignoreDeletesFromSource;
    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        return propertyPlaceholderSupport.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }

    @Override
    public void setLocation(Resource location) {
        try {
            propertyPlaceholderSupport.setLocation(
                    location, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
        } catch (Exception ex) {
            LOGGER.error("Problem setting the location.", ex);
            throw new RuntimeException("Problem setting the location.", ex);
        }
        super.setLocation(location);
    }

    @Override
    public void setLocations(Resource[] locations) {
        try {
            propertyPlaceholderSupport.setLocations(
                    locations, ignoreResourceNotFound, initialDelayMillis, delayMillis, ignoreDeletesFromSource);
        } catch (Exception ex) {
            LOGGER.error("Problem setting the locations", ex);
            throw new RuntimeException("Problem setting the locations.", ex);
        }
        super.setLocations(locations);
    }
}

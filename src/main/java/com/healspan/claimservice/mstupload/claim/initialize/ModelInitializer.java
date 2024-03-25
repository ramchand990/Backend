package com.healspan.claimservice.mstupload.claim.initialize;

import com.healspan.claimservice.mstupload.config.PropertyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ModelInitializer  implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MasterStore masterStore;

    @Autowired
    private PropertyConfig propertyConfig;


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
        masterStore.initialize();
        propertyConfig.getPropertyDetails();
    }
}

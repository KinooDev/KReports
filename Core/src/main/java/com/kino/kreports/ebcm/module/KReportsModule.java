package com.kino.kreports.ebcm.module;

import com.kino.kreports.ebcm.provider.UUIDProvider;
import me.fixeddev.ebcm.parameter.provider.ParameterProviderRegistry;
import me.fixeddev.ebcm.parameter.provider.ProvidersModule;

import java.util.UUID;

public class KReportsModule implements ProvidersModule {
    @Override
    public void configure(ParameterProviderRegistry registry) {
        registry.registerParameterProvider(UUID.class, new UUIDProvider());
    }
}

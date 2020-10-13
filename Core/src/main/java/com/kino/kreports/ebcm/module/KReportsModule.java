package com.kino.kreports.ebcm.module;

import com.kino.kreports.ebcm.provider.ReportPriorityProvider;
import com.kino.kreports.ebcm.provider.ReportStateProvider;
import com.kino.kreports.ebcm.provider.UUIDProvider;
import com.kino.kreports.utils.ReportPriority;
import com.kino.kreports.utils.ReportState;
import me.fixeddev.ebcm.parameter.provider.ParameterProviderRegistry;
import me.fixeddev.ebcm.parameter.provider.ProvidersModule;

import java.util.UUID;

public class KReportsModule implements ProvidersModule {
    @Override
    public void configure(ParameterProviderRegistry registry) {
        registry.registerParameterProvider(UUID.class, new UUIDProvider());
        registry.registerParameterProvider(ReportPriority.class, new ReportPriorityProvider());
        registry.registerParameterProvider(ReportState.class, new ReportStateProvider());
    }
}

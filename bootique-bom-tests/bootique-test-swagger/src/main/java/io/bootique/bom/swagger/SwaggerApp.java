package io.bootique.bom.swagger;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nhl.bootique.jersey.JerseyModule;
import io.bootique.bom.swagger.api.Api1;

public class SwaggerApp implements Module {

    @Override
    public void configure(Binder binder) {
        JerseyModule.contributePackages(binder).addBinding().toInstance(Api1.class.getPackage());
    }
}

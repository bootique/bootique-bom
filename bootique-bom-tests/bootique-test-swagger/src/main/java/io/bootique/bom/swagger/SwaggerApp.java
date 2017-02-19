package io.bootique.bom.swagger;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.bom.swagger.api.Api1;
import io.bootique.jersey.JerseyModule;

public class SwaggerApp implements Module {

    @Override
    public void configure(Binder binder) {
        JerseyModule.extend(binder).addPackage(Api1.class.getPackage());
    }
}

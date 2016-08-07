package io.bootique.bom.swagger;


import io.bootique.jetty.test.junit.JettyTestFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class SwaggerIT {

    @ClassRule
    public static JettyTestFactory SERVER = new JettyTestFactory();
    public static WebTarget BASE_TARGET = ClientBuilder.newClient().target("http://127.0.0.1:8080/");

    @BeforeClass
    public static void beforeClass() {
        SERVER.newRuntime()
                .configurator(bootique -> bootique.autoLoadModules().module(SwaggerApp.class))
                .startServer();
    }
    @Test
    public void testApi_Yaml() {
        Response r = BASE_TARGET.path("/swagger.yaml").request().get();
        assertEquals(200, r.getStatus());
        assertEqualsToResourceContents("response1.yml", r.readEntity(String.class));
    }

    @Test
    public void testApi_Json() {
        Response r = BASE_TARGET.path("/swagger.json").request().get();
        assertEquals(200, r.getStatus());
        assertEqualsToResourceContents("response1.json", r.readEntity(String.class) + "\n");
    }

    private void assertEqualsToResourceContents(String expectedResource, String toTest) {

        try (Scanner scanner = new Scanner(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(expectedResource)), "UTF-8")) {

            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }

            assertEquals(builder.toString(), toTest);
        }
    }
}

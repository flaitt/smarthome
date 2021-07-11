package org.acme.getting.started.esp8266;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "esp-key")
public interface Esp8266Controller {
    @GET
    @Path("/connect")
    Esp8266Response connectEsp(@QueryParam("rede") String rede, @QueryParam("senha") String senha);
}

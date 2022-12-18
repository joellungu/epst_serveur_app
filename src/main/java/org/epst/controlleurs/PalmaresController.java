package org.epst.controlleurs;

import org.epst.models.palmares.PalmaresMetier;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/palmares")
public class PalmaresController {

    @Inject
    PalmaresMetier palmaresMetier;

    //@Path("/{nomecole}/{codeoption}/{anneescolaire}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<org.epst.models.palmares.Palmares> getPlainte(@QueryParam("nomecole") String nomecole,
                                                              @QueryParam("codeoption") String codeoption,
                                                              @QueryParam("anneescolaire") String anneescolaire) {
        return palmaresMetier.getAll(nomecole,codeoption,anneescolaire);
    }

}

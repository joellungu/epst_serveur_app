package org.epst.controlleurs;

import org.epst.models.palmares.Palmares;
import org.epst.models.palmares.PalmaresMetier;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

@Path("/palmares")
public class PalmaresController {

    @Inject
    PalmaresMetier palmaresMetier;

    //@Path("/{codeecole}/{codeoption}/{anneescolaire}")
    @POST()
    //@Path("/{codeecole}/{codeoption}/{anneescolaire}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<org.epst.models.palmares.Palmares> getPlainte(HashMap e) {
        System.out.println(e.get("codeecole"));
        System.out.println(e.get("codeoption"));
        System.out.println(e.get("anneescolaire"));
        String c0 = e.get("nomprovince").toString();
        String c1 = e.get("nomecole").toString();
        String c2 = e.get("codeoption").toString();
        String c3 = e.get("anneescolaire").toString();
        System.out.println(c1);
        /*
        @PathParam("codeecole") String codeecole,
        @PathParam("anneescolaire") String anneescolaire
         */
        return palmaresMetier.getAll(c0,c1,c2,c3);
    }

    @Path("resultat/{anneescolaire}/{codecandidat}")
    @GET()
    public Palmares getPalmares(@PathParam("anneescolaire") String anneescolaire, @PathParam("codecandidat") String codecandidat){
        //
        return palmaresMetier.getPalmare(anneescolaire,codecandidat);
        //
    }

}

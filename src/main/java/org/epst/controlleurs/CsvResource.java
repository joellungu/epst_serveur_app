package org.epst.controlleurs;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.epst.models.palmares.*;

import java.util.Map;

@Path("/csv")
@Produces(MediaType.APPLICATION_JSON)
public class CsvResource {
    //
    @Inject
    Palmares2008 palmares2008;

    @Inject
    Palmares2009 palmares2009;

    @Inject
    Palmares2010 palmares2010;

    @Inject
    Palmares2011 palmares2011;

    @Inject
    Palmares2012 palmares2012;

    @Inject
    Palmares2013 palmares2013;

    @Inject
    Palmares2014 palmares2014;

    @Inject
    Palmares2015 palmares2015;

    @Inject
    Palmares2016 palmares2016;

    @Inject
    Palmares2017 palmares2017;

    @Inject
    Palmares2018 palmares2018;

    @Inject
    Palmares2022 palmares2022;


    @GET
    @Path("/{annee}/{code}")
    public Response get(@PathParam("annee") String annee, @PathParam("code") String code) {
        //
        System.out.println("Annee: "+annee+"  code:"+code);
        //
        if(annee.equals("2007-2008")) {
            return palmares2008.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2008-2009")){
            return palmares2009.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2009-2010")){
            return palmares2010.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2010-2011")){
            return palmares2011.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2011-2012")){
            return palmares2012.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2012-2013")){
            return palmares2013.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2013-2014")){
            return palmares2014.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2014-2015")){
            return palmares2015.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } else if(annee.equals("2015-2016")){
            return palmares2016.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        }
        else if(annee.equals("2016-2017")){
            return palmares2017.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        }
        else if(annee.equals("2017-2018")){
            return palmares2018.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        }
        else if(annee.equals("2021-2022")){
            return palmares2022.findByAnneeAndCode(annee, code)
                    .map(line -> Response.ok().entity(Map.of("data", line)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        }
        else {
            return Response.status(405).build();
        }
    }
}

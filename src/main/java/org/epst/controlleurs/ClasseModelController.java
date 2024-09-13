package org.epst.controlleurs;

import org.epst.models.ClasseModel;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.List;

@Path("classe")
public class ClasseModelController {
    //
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClasses(){
        //
        List<ClasseModel> classeModels = ClasseModel.listAll();
        //
        return Response.ok(classeModels).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveClasse(ClasseModel classeModel){
        //
        classeModel.persist();
        //
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveClasse(@QueryParam("id") Long id){
        //
        ClasseModel.deleteById(id);
        //
        return Response.ok().build();
    }


}

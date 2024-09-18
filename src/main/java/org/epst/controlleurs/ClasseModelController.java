package org.epst.controlleurs;

import org.epst.models.ClasseModel;
import org.epst.models.Cours.Cours;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    //
    @GET
    @Path("structure")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStructures(){
        //
        List<ClasseModel> classeModels = ClasseModel.listAll();
        //
        List<List<Cours>> cours = new LinkedList<>();
        //
        //classeModels.forEach((cm)->
        //
        for(ClasseModel cm : classeModels){
            //
            HashMap params = new HashMap();
            //params.put("cours", cm.nom.toLowerCase());
            params.put("categorie", cm.categorie.toLowerCase());
            params.put("classe", cm.cls);
            //params.put("propriete", "Eleve");
            //cours: "+cm.nom+"
            System.out.println(" : categorie : "+cm.categorie+" : classe : "+cm.cls+" : propriete : Eleve ");
            //cours =: cours and
            List<Cours> css = Cours.find("categorie =: categorie and classe =: classe", params).list();
            css.forEach(c -> {
                c.data = new byte[0];
            });
            cours.add(css);
            //
        }
        //
        //_________________
        //
        return Response.ok(cours).build();
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

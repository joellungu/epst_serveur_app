package org.epst.controlleurs;

import org.epst.models.Classe;
import org.epst.models.ClasseModel;
import org.epst.models.Cours.Cours;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Path("classe-model")
public class ClasseModelController {
    //
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClasseById(@PathParam("id") Long id){
        //
        ClasseModel classeModel = ClasseModel.findById(id);
        //
        if(classeModel == null){
            return Response.status(405).build();
        }
        //
        return Response.ok(classeModel).build();
    }

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
        List<Classe> classes = Classe.listAll();
        //
        List<List<Cours>> cours = new LinkedList<>();
        //
        //classeModels.forEach((cm)->
        //
        for(Classe cm : classes){
            //
            HashMap params = new HashMap();
            //params.put("cours", cm.nom.toLowerCase());
            params.put("cycle", cm.cycle.toLowerCase());
            //params.put("cls", cm.cls);
            //params.put("propriete", "Eleve");
            //cours: "+cm.nom+"
            System.out.println(" : cycle : "+cm.cycle+" : propriete : Eleve ");
            //cours =: cours and
            List<Cours> csss = Cours.listAll();
            List<Cours> css = new LinkedList<>();
            //c.cls == cm.niveau &&
            csss.forEach(c -> {
                if(c.cycle.toLowerCase().equals(cm.cycle.toLowerCase())){
                    css.add(c);
                }
                //c.data = new byte[0];
            });
                    //.find("categorie =: categorie and cls =: cls", params).list();
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

package org.epst.online;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;

@Path("/online/classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OnlineClassResource {

    public static class CreateClassRequest {
        public String code;
        public String name;
        public String niveau;
        public String ecoleCode;
        public String province;
        public String district;
        public String createdByMatricule;
        public Integer maxParticipants;
    }

    @POST
    @Transactional
    public Response create(CreateClassRequest request) {
        if (request == null || request.code == null || request.name == null || request.ecoleCode == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields").build();
        }

        OnlineClass onlineClass = new OnlineClass();
        onlineClass.code = request.code;
        onlineClass.name = request.name;
        onlineClass.niveau = request.niveau;
        onlineClass.ecoleCode = request.ecoleCode;
        onlineClass.province = request.province;
        onlineClass.district = request.district;
        onlineClass.createdByMatricule = request.createdByMatricule;
        if (request.maxParticipants != null && request.maxParticipants > 0) {
            onlineClass.maxParticipants = request.maxParticipants;
        }
        onlineClass.persist();

        return Response.status(Response.Status.CREATED).entity(onlineClass).build();
    }

    @GET
    public List<OnlineClass> listAll() {
        return OnlineClass.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        OnlineClass onlineClass = OnlineClass.findById(id);
        if (onlineClass == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(onlineClass).build();
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("ecoleCode") String ecoleCode,
                           @QueryParam("province") String province,
                           @QueryParam("district") String district) {
        HashMap params = new HashMap();
        StringBuilder where = new StringBuilder("1 = 1");
        if (ecoleCode != null && !ecoleCode.isBlank()) {
            where.append(" and ecoleCode = :ecoleCode");
            params.put("ecoleCode", ecoleCode);
        }
        if (province != null && !province.isBlank()) {
            where.append(" and province = :province");
            params.put("province", province);
        }
        if (district != null && !district.isBlank()) {
            where.append(" and district = :district");
            params.put("district", district);
        }
        return Response.ok(OnlineClass.list(where.toString(), params)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, OnlineClass update) {
        OnlineClass onlineClass = OnlineClass.findById(id);
        if (onlineClass == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (update.name != null) {
            onlineClass.name = update.name;
        }
        if (update.niveau != null) {
            onlineClass.niveau = update.niveau;
        }
        if (update.province != null) {
            onlineClass.province = update.province;
        }
        if (update.district != null) {
            onlineClass.district = update.district;
        }
        if (update.maxParticipants > 0) {
            onlineClass.maxParticipants = update.maxParticipants;
        }
        onlineClass.active = update.active;
        return Response.ok(onlineClass).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = OnlineClass.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}

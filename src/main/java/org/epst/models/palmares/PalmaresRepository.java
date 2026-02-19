package org.epst.models.palmares;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PalmaresRepository implements PanacheRepository<Palmares> {

}

package org.epst.models;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class PersistenceConfig {

    @Singleton
    public Jdbi jdbi() {
        return Jdbi.create("jdbc:postgresql://bqlujt3tbffdu10fdgql-postgresql.services.clever-cloud.com:5434/bqlujt3tbffdu10fdgql",
                "uuypo0h3cqqun990xnbu",
                "bP1lkHFA26CrKH4RU9r1")
                .installPlugin(new SqlObjectPlugin());
    }

    /*
    @Produces
    public ActorRepository actorRepository(final Jdbi jdbi) {
        return jdbi.onDemand(ActorRepository.class);
    }
    */
}

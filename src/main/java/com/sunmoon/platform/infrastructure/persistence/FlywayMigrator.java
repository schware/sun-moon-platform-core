package com.sunmoon.platform.infrastructure.persistence;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

/**
 * Runs migrations from the calling service's own
 * {@code src/main/resources/db/migration} — the kernel supplies the
 * mechanism and never the migrations (docs/adr/0014).
 *
 * <p>Each service therefore numbers its migrations from V1 and needs its
 * own database. Pointing two services at one database is not silently
 * wrong: Flyway refuses to migrate when the history table holds applied
 * migrations it cannot resolve locally.
 *
 * <p>Not exercised against a live PostgreSQL instance in this environment
 * — see docs/adr/0005.
 */
public final class FlywayMigrator {

    public static void migrate(DataSource dataSource) {
        Flyway.configure()
                .dataSource(dataSource)
                .load()
                .migrate();
    }

    private FlywayMigrator() {
    }
}

# sun-moon-platform-core

The kernel the `sun-moon` Java services are built on: Netty listener
binding, REST routing, the blocking-work contract, and the shared
infrastructure wiring (MyBatis/HikariCP/Flyway, Micrometer, OpenTelemetry).

It is a **library, not an application** — no `main`, no ports of its own,
no knowledge of any domain.

That constraint was written for two consumers — Back Office and the
device-facing runtime — so that neither could reach the other through the
kernel. **BO has since left**: it was rebuilt as a Spring service in the
`sun-moon-java-platform` family (its `main` branch), because BO is
administration with no throughput requirement and everything it
hand-wrote is what `spring-boot-starter-security` provides. Its Netty
implementation is archived as
[sun-moon-platform-bo-netty](https://github.com/schware/sun-moon-platform-bo-netty).

So this kernel has one consumer today —
[sun-moon-java-platform](https://github.com/schware/sun-moon-java-platform)
`master`. The two rules below are still worth keeping at one consumer,
and whether a single-consumer kernel deserves its own repository is an
open question rather than a settled one (that repo's `docs/adr/0015`).

## What's in it

| Package | |
|---|---|
| `core` | `CoreRuntime` binds a list of `ListenerSpec(name, port, ChannelInitializer)` onto one pair of event-loop groups. It does not know whether a listener speaks HTTP. |
| `transport.http` | `RestEndpoint`, `RouteKey(method, path)`, `RestRequestRouter`, `HttpServerInitializer` (optional WebSocket), `JsonResponses`, `RequestValidation`, plus `/health` and `/metrics` |
| `observability` | Micrometer Prometheus registry, OpenTelemetry tracer |
| `infrastructure.persistence` | `MyBatisConfig` (mappers passed in, never listed here), `PostgresConnectionSettings`, `FlywayMigrator` |

## Two rules it keeps

**Endpoints never run on an event-loop thread.** `RestRequestRouter`
dispatches `handle()` to an executor supplied by the composing
application, because endpoints call repositories and JDBC blocks. A query
run inline would stall every connection that thread serves.

**The kernel imports nothing downward.** No domain types, no `main`, no
service names — configuration like "which port a service listens on" belongs to it,
not here.

## Use

Consumed as a Gradle composite build (a git submodule at `core/`), so
there is no artifact registry or token in the loop:

```kotlin
// settings.gradle.kts
includeBuild("core")

// build.gradle.kts
dependencies { implementation("com.sunmoon:sun-moon-platform-core") }
```

Requires JDK 21.

package cz.cvut.fit.gykas.core.auth

import cz.cvut.fit.gykas.core.plugins.RoleAuthorizationPlugin
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext

fun Route.withRoles(vararg roles: String, build: Route.() -> Unit) {
    val route = createChild(object : RouteSelector() {
        override suspend fun evaluate(
            context: RoutingResolveContext, segmentIndex: Int
        ): RouteSelectorEvaluation = RouteSelectorEvaluation.Transparent
    })

    route.install(RoleAuthorizationPlugin) {
        roles(roles.toSet())
    }

    route.build()
}


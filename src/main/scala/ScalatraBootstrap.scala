import javax.servlet.ServletContext
import org.scalatra._
import org.scalatra.mediatech.controllers._
import org.scalatra.mediatech.persistence.PostgresConnection
import org.scalatra.swagger.ApiKey

class ScalatraBootstrap extends LifeCycle with PostgresConnection {

  implicit val swagger: SwaggerController = new SwaggerController()
  swagger.addAuthorization(ApiKey("Authorization"))

  override def init(context: ServletContext) {
    //-- init database
//    openDatabaseConnection()

    //-- configure routes for each controller
    context.mount(new MediatechController, "/api/mediatech")
    context.mount (new ResourcesApp, "/api-docs")
  }

  override def destroy(context: ServletContext): Unit = {
    closeDatabaseConnection()
  }
}

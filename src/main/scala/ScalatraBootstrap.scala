import javax.servlet.ServletContext
import org.scalatra._
import org.scalatra.mediatech.controllers._

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {
    //-- configure routes for each controller
    context.mount(new MediatechController, "/api/mediatech")
//    context.mount (new ResourcesApp, "/api-docs")
  }

  override def destroy(context: ServletContext): Unit = {
//    closeDatabaseConnection()
  }
}

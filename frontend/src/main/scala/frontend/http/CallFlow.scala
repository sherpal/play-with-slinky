package frontend.http

import akka.NotUsed
import akka.stream.scaladsl.Flow
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import monix.execution.Scheduler

object CallFlow {

  def apply[T, U](parallelism: Int)(requestTransformer: T => HttpRequest, responseTransformer: SimpleHttpResponse => U)(
      implicit scheduler: Scheduler
  ): Flow[T, U, NotUsed] =
    Flow[T].map(requestTransformer).mapAsync(parallelism)(_.send()).map(responseTransformer)

}

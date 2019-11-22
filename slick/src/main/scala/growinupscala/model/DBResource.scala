package growinupscala.model

import java.time.Instant
import growinupscala.Id

case class DBResource(id: Id, col1: Option[String], col2: Long, col3: Instant) extends Resource {
  override val name: String = id
}
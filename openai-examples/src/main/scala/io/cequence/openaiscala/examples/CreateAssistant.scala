package io.cequence.openaiscala.examples
import io.cequence.openaiscala.domain.{FunctionSpec, ModelId}

import scala.concurrent.Future

object CreateAssistant extends Example {
  override protected def run: Future[_] =
    for {
      assistant <- service.createAssistant(
        model = ModelId.gpt_3_5_turbo_1106,
        name = Some("Math Tutor"),
        instructions = Some(
          "You are a personal math tutor. When asked a question, write and run Python code to answer the question."
        ),
        tools = Seq(
          FunctionSpec("name", description = None, parameters = Map())
        ),
        toolResources = Seq()
      )
    } yield println(assistant)
}

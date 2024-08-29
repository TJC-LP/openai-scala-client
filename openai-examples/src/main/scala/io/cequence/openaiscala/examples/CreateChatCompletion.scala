package io.cequence.openaiscala.examples

import io.cequence.openaiscala.domain.settings.CreateChatCompletionSettings
import io.cequence.openaiscala.domain._

import scala.concurrent.Future

object CreateChatCompletion extends Example {

  private val messages = Seq(
    SystemMessage("You are a helpful assistant."),
    UserMessage("What is the weather like in Norway?")
  )

  override protected def run: Future[_] =
    service
      .createChatCompletion(
        messages = messages,
        settings = CreateChatCompletionSettings(
          model = ModelId.gpt_4o_2024_05_13,
          temperature = Some(0),
          max_tokens = Some(100)
        )
      )
      .map { content =>
        printMessageContent(content)
      }
}

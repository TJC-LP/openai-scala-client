package io.cequence.openaiscala

import io.cequence.openaiscala.JsonFormats._
import io.cequence.openaiscala.JsonFormatsSpec.JsonPrintMode
import io.cequence.openaiscala.JsonFormatsSpec.JsonPrintMode.{Compact, Pretty}
import io.cequence.openaiscala.domain.AssistantToolResource.{
  CodeInterpreterResources,
  FileSearchResources,
  VectorStore
}
import io.cequence.openaiscala.domain.response.AssistantToolResourceResponse.{
  CodeInterpreterResourcesResponse,
  FileSearchResourcesResponse
}
import io.cequence.openaiscala.domain.response.ResponseFormat.{
  JsonObjectResponse,
  StringResponse,
  TextResponse
}
import io.cequence.openaiscala.domain.response.{AssistantToolResourceResponse, ResponseFormat}
import io.cequence.openaiscala.domain._
import org.scalatest.Ignore
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import play.api.libs.json.{Format, Json}

object JsonFormatsSpec {
  sealed trait JsonPrintMode
  object JsonPrintMode {
    case object Compact extends JsonPrintMode
    case object Pretty extends JsonPrintMode
  }
}

class JsonFormatsSpec extends AnyWordSpecLike with Matchers {

  private val textResponseJson =
    """{
      |  "type" : "text"
      |}""".stripMargin

  private val jsonObjectResponseJson =
    """{
      |  "type" : "json_object"
      |}""".stripMargin

  private val codeInterpreterResourcesJson =
    """{
      |  "code_interpreter" : {
      |    "file_ids" : [ {
      |      "file_id" : "file-id-1"
      |    }, {
      |      "file_id" : "file-id-2"
      |    } ]
      |  }
      |}""".stripMargin

  private val fileSearchResourcesJson =
    """{
      |  "file_search" : {
      |    "vector_store_ids" : [ {
      |      "file_id" : "file-id-1"
      |    } ],
      |    "vector_stores" : [ {
      |      "file_ids" : [ {
      |        "file_id" : "file-id-1"
      |      } ],
      |      "metadata" : {
      |        "key" : "value"
      |      }
      |    } ]
      |  }
      |}""".stripMargin

  private val codeInterpreterResourcesResponseJson =
    """{
      |  "code_interpreter" : {
      |    "file_ids" : [ {
      |      "file_id" : "file-id-1"
      |    } ]
      |  }
      |}""".stripMargin

  private val fileSearchResourcesResponseJson =
    """{
      |  "file_search" : {
      |    "vector_store_ids" : [ {
      |      "file_id" : "file-id-1"
      |    } ]
      |  }
      |}""".stripMargin

  private val weightsAndBiasesIntegrationJson =
    """{
      |  "type" : "wandb",
      |  "wandb" : {
      |    "project" : "project the run belong to",
      |    "name" : "a run display name",
      |    "entity" : "integrations team",
      |    "tags" : [ "openai/finetune", "openai/chatgpt-4" ]
      |  }
      |}""".stripMargin

  private val fileCountsJson =
    """{
      |  "in_progress" : 1,
      |  "completed" : 2,
      |  "cancelled" : 3,
      |  "failed" : 4,
      |  "total" : 10
      |}""".stripMargin

  private val attachmentJson =
    """{
      |  "file_id" : {
      |    "file_id" : "file-id-1"
      |  },
      |  "tools" : [ {
      |    "type" : "code_interpreter"
      |  }, {
      |    "type" : "file_search"
      |  } ]
      |}""".stripMargin

  private val ThreadMessageContentJson =
    """{
      |  "type": "text",
      |  "text": {
      |    "value": "Based on the conference program you provided, here are some talks that align with your goal of digitising and optimizing procurement processes with the use of AI:\n\n1. **\"AI AS A TOOL OR A TOY?\"**  \n   - Description: This session covers the basics of AI in the context of corporate procurement, successful case studies, AI tools for procurement managers, integrating AI into existing systems, and measuring success.\n   - Speaker: Not specified\n   - Time: 9:50-10:30, Stage 6 Chat GPT and Other AI in Procurement\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\n2. **\"HOW TO 'TALK' TO AI FOR BETTER PROCUREMENT DECISIONS\"**  \n   - Description: Focuses on formulating prompts for effective interaction with AI, adapting AI to enterprise processes, overcoming communication challenges, and includes interactive demonstrations.\n   - Speaker: Not specified\n   - Time: 10:30-11:10, Stage 6 Chat GPT and Other AI in Procurement\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\n3. **\"WHAT AI DOESN'T SAY / HOW IT AFFECTS CORPORATE PROCUREMENT ERRORS & BIASES IN AI: RECOGNISING & MINIMISING RISKS\"**  \n   - Description: Covers understanding AI decision-making processes, legal and ethical aspects, risk mitigation strategies, and DeepFake attacks.\n   - Speaker: Not specified\n   - Time: 11:30-12:10, Stage 6 Chat GPT and Other AI in Procurement\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\n4. **\"FUTURE OF PROCUREMENT ECHOES\"**  \n   - Description: Explores the application of AI in contract management and workflow to simplify procurement processes.\n   - Speakers: Rasťo Kovaľ – Cequence, Jana Dubcová – CNPK\n   - Time: 14:30-15:30, Stage 7 The Digital Revolution in Procurement Practice\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\nAttending these talks can provide valuable insights into leveraging AI for digital transformation and optimization of procurement processes.",
      |    "annotations": [
      |      {
      |        "type": "file_citation",
      |        "text": "【4:0†source】",
      |        "start_index": 555,
      |        "end_index": 567,
      |        "file_citation": {
      |          "file_id": "file-gGBbjfqasohuF31BhkIpmTZk"
      |        }
      |      },
      |      {
      |        "type": "file_citation",
      |        "text": "【4:0†source】",
      |        "start_index": 973,
      |        "end_index": 985,
      |        "file_citation": {
      |          "file_id": "file-gGBbjfqasohuF31BhkIpmTZk"
      |        }
      |      },
      |      {
      |        "type": "file_citation",
      |        "text": "【4:0†source】",
      |        "start_index": 1398,
      |        "end_index": 1410,
      |        "file_citation": {
      |          "file_id": "file-gGBbjfqasohuF31BhkIpmTZk"
      |        }
      |      },
      |      {
      |        "type": "file_citation",
      |        "text": "【4:0†source】",
      |        "start_index": 1754,
      |        "end_index": 1766,
      |        "file_citation": {
      |          "file_id": "file-gGBbjfqasohuF31BhkIpmTZk"
      |        }
      |      }
      |    ]
      |  }
      |}""".stripMargin

  "JSON Formats" should {

    "serialize and deserialize a String response format" in {
      testCodec[ResponseFormat](StringResponse: ResponseFormat, """"auto"""")
    }

    "serialize and deserialize a Text response format" in {
      testCodec[ResponseFormat](TextResponse, textResponseJson, Pretty)
    }

    "serialize and deserialize a JSON object response format" in {
      testCodec[ResponseFormat](JsonObjectResponse, jsonObjectResponseJson, Pretty)
    }

    "serialize and deserialize code interpreter's resources" in {
      testCodec[AssistantToolResource](
        CodeInterpreterResources(Seq(FileId("file-id-1"), FileId("file-id-2"))),
        codeInterpreterResourcesJson,
        Pretty
      )
    }

    "serialize and deserialize file search's resources" ignore {
      testCodec[AssistantToolResource](
        FileSearchResources(
          Seq("vs_xxx"),
          Seq(VectorStore(Seq(FileId("file-id-1")), Map("key" -> "value")))
        ),
        fileSearchResourcesJson,
        Pretty
      )
    }

    "serialize and deserialize run tools" in {
      testCodec[RunTool](
        RunTool.CodeInterpreterTool,
        """{
             |  "type" : "code_interpreter"
             |}""".stripMargin,
        Pretty
      )

      testCodec[RunTool](
        RunTool.FileSearchTool,
        """{
            |  "type" : "file_search"
            |}""".stripMargin,
        Pretty
      )

      testCodec[RunTool](
        RunTool.FunctionTool("function-name"),
        """{
          |  "type" : "function",
          |  "function" : {
          |    "name" : "function-name"
          |  }
          |}""".stripMargin,
        Pretty
      )
    }

    "serialize and deserialize none, auto, and required tool choices" in {
      testCodec[ToolChoice](
        ToolChoice.None,
        "\"none\"".stripMargin,
        Pretty
      )

      testCodec[ToolChoice](
        ToolChoice.Auto,
        "\"auto\"".stripMargin,
        Pretty
      )

      testCodec[ToolChoice](
        ToolChoice.Required,
        "\"required\"".stripMargin,
        Pretty
      )
    }

    "serialize and deserialize enforced tool choices" in {
      testSerialization[ToolChoice](
        ToolChoice.EnforcedTool(RunTool.CodeInterpreterTool),
        """{
          |  "type" : "code_interpreter"
          |}""".stripMargin,
        Pretty
      )

      testSerialization[ToolChoice](
        ToolChoice.EnforcedTool(RunTool.FileSearchTool),
        """{
          |  "type" : "file_search"
          |}""".stripMargin,
        Pretty
      )

      testSerialization[ToolChoice](
        ToolChoice.EnforcedTool(RunTool.FunctionTool("function-name")),
        """{
          |  "type" : "function",
          |  "function" : {
          |    "name" : "function-name"
          |  }
          |}""".stripMargin,
        Pretty
      )
    }

    "serialize and deserialize a fine-tuning Weights and Biases integration" in {
      val integration = FineTune.WeightsAndBiases(
        "project the run belong to",
        Some("a run display name"),
        Some("integrations team"),
        Seq("openai/finetune", "openai/chatgpt-4")
      )
      testCodec[FineTune.Integration](
        integration,
        weightsAndBiasesIntegrationJson,
        Pretty,
        justSemantics = true
      )
    }

    "serialize and deserialize attachment" in {
      testCodec[Attachment](
        Attachment(
          fileId = Some(FileId("file-id-1")),
          tools = Seq(
            MessageAttachmentTool.CodeInterpreterSpec,
            MessageAttachmentTool.FileSearchSpec
          )
        ),
        attachmentJson,
        Pretty
      )
    }

    "serialize and deserialize FileCounts" in {
      val integration = FileCounts(
        inProgress = 1,
        completed = 2,
        cancelled = 3,
        failed = 4,
        total = 10
      )
      testCodec[FileCounts](
        integration,
        fileCountsJson,
        Pretty,
        justSemantics = true
      )
    }

    "serialize and deserialize VectorStore" in {
      val vectorStore = VectorStore(
        fileIds = Seq(FileId("file-123")),
        metadata = Map("key" -> "value")
      )
      testCodec[VectorStore](
        vectorStore,
        """{
          |  "file_ids" : [ {
          |    "file_id" : "file-123"
          |  } ],
          |  "metadata" : {
          |    "key" : "value"
          |  }
          |}""".stripMargin,
        Pretty
      )
    }

    "serialize and deserialize VectorStoreFileStatus" in {
      import VectorStoreFileStatus._
      testCodec[VectorStoreFileStatus](Cancelled, "\"cancelled\"".stripMargin, Pretty)
      testCodec[VectorStoreFileStatus](Completed, "\"completed\"".stripMargin, Pretty)
      testCodec[VectorStoreFileStatus](InProgress, "\"in_progress\"".stripMargin, Pretty)
      testCodec[VectorStoreFileStatus](Failed, "\"failed\"".stripMargin, Pretty)
    }

    "serialize and deserialize LastErrorCode" in {
      import LastErrorCode._
      testCodec[LastErrorCode](ServerError, "\"server_error\"".stripMargin, Pretty)
      testCodec[LastErrorCode](
        RateLimitExceeded,
        "\"rate_limit_exceeded\"".stripMargin,
        Pretty
      )
    }

    "serialize and deserialize ChunkingStrategy" in {
      import ChunkingStrategy._
      testCodec[ChunkingStrategy](
        AutoChunkingStrategy,
        """{
        |  "type" : "auto"
        |}""".stripMargin.stripMargin,
        Pretty
      )
      testCodec[ChunkingStrategy](
        StaticChunkingStrategy(None, None),
        """{
          |  "type" : "static",
          |  "max_chunk_size_tokens" : 800,
          |  "chunk_overlap_tokens" : 400
          |}""".stripMargin,
        Pretty
      )
      testDeserialization[ChunkingStrategy](
        StaticChunkingStrategy(None, None),
        """{
          |  "type" : "static"
          |}""".stripMargin
      )
      testDeserialization[ChunkingStrategy](
        StaticChunkingStrategy(Some(1000), None),
        """{
          |  "type" : "static",
          |  "max_chunk_size_tokens" : 1000
          |}""".stripMargin
      )
      testDeserialization[ChunkingStrategy](
        StaticChunkingStrategy(None, Some(150)),
        """{
          |  "type" : "static",
          |  "chunk_overlap_tokens" : 150
          |}""".stripMargin
      )
    }

    "serialize and deserialize VectorStoreFile" in {
      val vectorStoreFile = VectorStoreFile(
        id = "file-id-1",
        `object` = "file",
        usageBytes = 100,
        createdAt = 1000,
        vectorStoreId = "vs-123",
        status = VectorStoreFileStatus.Completed,
        lastError = Some(LastError(LastErrorCode.ServerError, "error message")),
        chunkingStrategy = ChunkingStrategy.StaticChunkingStrategy(Some(1000), Some(500))
      )
      testCodec[VectorStoreFile](
        vectorStoreFile,
        """{
            |  "id" : "file-id-1",
            |  "object" : "file",
            |  "usage_bytes" : 100,
            |  "created_at" : 1000,
            |  "vector_store_id" : "vs-123",
            |  "status" : "completed",
            |  "last_error" : {
            |    "code" : "server_error",
            |    "message" : "error message"
            |  },
            |  "chunking_strategy" : {
            |    "type" : "static",
            |    "max_chunk_size_tokens" : 1000,
            |    "chunk_overlap_tokens" : 500
            |  }
            |}""".stripMargin,
        Pretty,
        justSemantics = true
      )
    }

    "serialize and deserialize ThreadMessageContent" in {
      val threadMessageContent =
        ThreadMessageContent(
          `type` = ThreadMessageContentType.text,
          text = Some(
            ThreadMessageText(
              value =
                "Based on the conference program you provided, here are some talks that align with your goal of digitising and optimizing procurement processes with the use of AI:\n\n1. **\"AI AS A TOOL OR A TOY?\"**  \n   - Description: This session covers the basics of AI in the context of corporate procurement, successful case studies, AI tools for procurement managers, integrating AI into existing systems, and measuring success.\n   - Speaker: Not specified\n   - Time: 9:50-10:30, Stage 6 Chat GPT and Other AI in Procurement\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\n2. **\"HOW TO 'TALK' TO AI FOR BETTER PROCUREMENT DECISIONS\"**  \n   - Description: Focuses on formulating prompts for effective interaction with AI, adapting AI to enterprise processes, overcoming communication challenges, and includes interactive demonstrations.\n   - Speaker: Not specified\n   - Time: 10:30-11:10, Stage 6 Chat GPT and Other AI in Procurement\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\n3. **\"WHAT AI DOESN'T SAY / HOW IT AFFECTS CORPORATE PROCUREMENT ERRORS & BIASES IN AI: RECOGNISING & MINIMISING RISKS\"**  \n   - Description: Covers understanding AI decision-making processes, legal and ethical aspects, risk mitigation strategies, and DeepFake attacks.\n   - Speaker: Not specified\n   - Time: 11:30-12:10, Stage 6 Chat GPT and Other AI in Procurement\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\n4. **\"FUTURE OF PROCUREMENT ECHOES\"**  \n   - Description: Explores the application of AI in contract management and workflow to simplify procurement processes.\n   - Speakers: Rasťo Kovaľ – Cequence, Jana Dubcová – CNPK\n   - Time: 14:30-15:30, Stage 7 The Digital Revolution in Procurement Practice\n   - Source: `file-gGBbjfqasohuF31BhkIpmTZk`【4:0†source】\n\nAttending these talks can provide valuable insights into leveraging AI for digital transformation and optimization of procurement processes.",
              annotations = Seq(
                FileAnnotation(
                  FileAnnotationType.file_citation,
                  text = "【4:0†source】",
                  start_index = 555,
                  end_index = 567,
                  file_citation = Some(FileCitation("file-gGBbjfqasohuF31BhkIpmTZk"))
                ),
                FileAnnotation(
                  FileAnnotationType.file_citation,
                  text = "【4:0†source】",
                  start_index = 973,
                  end_index = 985,
                  file_citation = Some(FileCitation("file-gGBbjfqasohuF31BhkIpmTZk"))
                ),
                FileAnnotation(
                  FileAnnotationType.file_citation,
                  text = "【4:0†source】",
                  start_index = 1398,
                  end_index = 1410,
                  file_citation = Some(FileCitation("file-gGBbjfqasohuF31BhkIpmTZk"))
                ),
                FileAnnotation(
                  FileAnnotationType.file_citation,
                  text = "【4:0†source】",
                  start_index = 1754,
                  end_index = 1766,
                  file_citation = Some(FileCitation("file-gGBbjfqasohuF31BhkIpmTZk"))
                )
              )
            )
          )
        )

      testCodec[ThreadMessageContent](
        threadMessageContent,
        ThreadMessageContentJson,
        Pretty,
        justSemantics = true
      )(threadMessageContentFormat)
    }

  }

  private def testCodec[A](
    value: A,
    json: String,
    printMode: JsonPrintMode = Compact,
    justSemantics: Boolean = false
  )(
    implicit format: Format[A]
  ): Unit = {
    val jsValue = Json.toJson(value)
    val serialized = printMode match {
      case Compact => jsValue.toString()
      case Pretty  => Json.prettyPrint(jsValue)
    }

    if (!justSemantics) serialized shouldBe json

    val json2 = Json.parse(json).as[A]
    json2 shouldBe value
  }

  private def testSerialization[A](
    value: A,
    json: String,
    printMode: JsonPrintMode = Compact
  )(
    implicit format: Format[A]
  ): Unit = {
    val jsValue = Json.toJson(value)
    val serialized = printMode match {
      case Compact => jsValue.toString()
      case Pretty  => Json.prettyPrint(jsValue)
    }
    serialized shouldBe json
  }

  private def testDeserialization[A](
    value: A,
    json: String
  )(
    implicit format: Format[A]
  ): Unit = {
    Json.parse(json).as[A] shouldBe value
  }
}

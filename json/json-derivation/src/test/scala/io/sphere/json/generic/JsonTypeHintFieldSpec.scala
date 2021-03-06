package io.sphere.json.generic

import cats.data.Validated.Valid
import io.sphere.json._
import org.json4s._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class JsonTypeHintFieldSpec extends AnyWordSpec with Matchers {
  import JsonTypeHintFieldSpec._

  "JSONTypeHintField" must {
    "allow to set another field to distinguish between types (toMongo)" in {
      val user = UserWithPicture("foo-123", Medium, "http://example.com")
      val expected = JObject(
        List(
          "userId" -> JString("foo-123"),
          "pictureSize" -> JObject(List("pictureType" -> JString("Medium"))),
          "pictureUrl" -> JString("http://example.com")))

      val json = toJValue[UserWithPicture](user)
      json must be(expected)
    }

    "allow to set another field to distinguish between types (fromMongo)" in {
      val json =
        """
        {
          "userId": "foo-123",
          "pictureSize": { "pictureType": "Medium" },
          "pictureUrl": "http://example.com"
        }
        """

      val Valid(user) = fromJSON[UserWithPicture](json)

      user must be(UserWithPicture("foo-123", Medium, "http://example.com"))
    }
  }

}

object JsonTypeHintFieldSpec {

  @JSONTypeHintField(value = "pictureType")
  sealed trait PictureSize
  @JSONTypeHintField(value = "pictureType")
  case object Small extends PictureSize
  @JSONTypeHintField(value = "pictureType")
  case object Medium extends PictureSize
  @JSONTypeHintField(value = "pictureType")
  case object Big extends PictureSize

  object PictureSize {
    implicit val json: JSON[PictureSize] = deriveJSON[PictureSize]
  }

  case class UserWithPicture(userId: String, pictureSize: PictureSize, pictureUrl: String)

  object UserWithPicture {
    implicit val json: JSON[UserWithPicture] = jsonProduct(apply _)
  }
}

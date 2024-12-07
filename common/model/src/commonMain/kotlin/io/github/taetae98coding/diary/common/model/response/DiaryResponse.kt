package io.github.taetae98coding.diary.common.model.response

import kotlinx.serialization.Serializable

@Serializable
public data class DiaryResponse<T>(
	val code: Int = 0,
	val message: String = "",
	val body: T? = null,
) {
	public companion object {
		public val Success: DiaryResponse<Unit> = DiaryResponse(200, "SUCCESS", Unit)
		public val Created: DiaryResponse<Unit> = DiaryResponse(201, "CREATED", Unit)
		public val Unauthorized: DiaryResponse<Unit> = DiaryResponse(401, "Unauthorized", Unit)
		public val InternalServerError: DiaryResponse<Unit> = DiaryResponse(500, "InternalServerError", Unit)

		public val AlreadyExistEmail: DiaryResponse<Unit> = DiaryResponse(1000, "AlreadyExistEmail", Unit)
		public val AccountNotFound: DiaryResponse<Unit> = DiaryResponse(1001, "AccountNotFound", Unit)

		public fun <T> success(body: T): DiaryResponse<T> =
			DiaryResponse(
				code = 200,
				message = "SUCCESS",
				body = body,
			)
	}
}

package io.github.taetae98coding.diary.core.model.account

public sealed class Account {
	public abstract val uid: String

	public data object Guest : Account() {
		override val uid: String = ""
	}

	public data class Member(
		val email: String,
		override val uid: String,
	) : Account()
}

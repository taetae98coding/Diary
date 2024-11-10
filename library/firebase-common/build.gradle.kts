plugins {
    id("diary.android.library")
    id("diary.kotlin.multiplatform.all")
}

android {
    namespace = "${Build.NAMESPACE}.library.firebase.common"
}

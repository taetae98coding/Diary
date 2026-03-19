# libs.versions.toml Guidelines

## 정렬 기준

versions, libraries, plugins 섹션 모두 동일한 순서를 따른다:

**kotlin → agp → jetbrains → kotlinx → androidx → abc 순**

각 그룹은 주석(`# kotlin`, `# agp`, `# kotlinx`, `# jetbrains`, `# androidx`, `# abc`)으로 구분한다.

## Release Note 주석

versions 항목에는 release note URL을 탭으로 정렬하여 인라인 주석으로 추가한다.
버전은 URL에 포함하지 않는다.
- github.com: releases 페이지 우선, releases가 없으면 tags 사용. 
- developer.android.com: 항상 `?hl=en` 파라미터를 추가한다.

```toml
kotlin = "2.3.20"				# https://github.com/JetBrains/kotlin/releases
logback = "1.5.32"				# https://github.com/qos-ch/logback/releases
androidx-activity = "1.13.0"	# https://developer.android.com/jetpack/androidx/releases/activity?hl=en
```

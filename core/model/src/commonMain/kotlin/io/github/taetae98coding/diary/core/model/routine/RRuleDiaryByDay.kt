package io.github.taetae98coding.diary.core.model.routine

import kotlinx.datetime.DayOfWeek

/**
 * Diary 도메인의 BYDAY 표현. RFC 5545 BYDAY와 의미가 다르다.
 *
 * 선택된 요일 집합과 단일 ordinal(주차)을 보유한다.
 *
 * `ordinal` 의미:
 * - `null` → 매주
 * - `> 0` → 그 달의 N번째 주 (일요일 시작, 1주차 = 1일이 속한 주)
 * - `< 0` → 그 달 끝에서 N번째 주
 *
 * RFC 5545의 BYDAY ordinal("N번째 발생")과 달리 **일요일 시작 캘린더의 N번째 주**를 의미한다.
 * 예) 4/1=수요일인 달에서 1주차는 [전월 일요일~4/4 토요일]이므로
 * `RRuleDiaryByDay({SUN}, 1)`은 그 달에 매칭되는 일요일이 없다.
 */
public data class RRuleDiaryByDay(
    val days: Set<DayOfWeek> = emptySet(),
    val ordinal: Int? = null,
)

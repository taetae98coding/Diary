package io.github.taetae98coding.diary.library.datetime

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.datetime.LocalDate

class LocalDateRangeOverlapsTest : FunSpec() {
    init {
        test("완전히 겹치는 경우 true") {
            val a = LocalDate(1998, 1, 5)..LocalDate(1998, 1, 15)
            val b = LocalDate(1998, 1, 5)..LocalDate(1998, 1, 15)

            (a overlaps b).shouldBeTrue()
        }

        test("부분적으로 겹치는 경우 true") {
            val a = LocalDate(1998, 1, 1)..LocalDate(1998, 1, 9)
            val b = LocalDate(1998, 1, 5)..LocalDate(1998, 1, 15)

            (a overlaps b).shouldBeTrue()
        }

        test("한쪽이 다른쪽을 포함하는 경우 true") {
            val a = LocalDate(1998, 1, 1)..LocalDate(1998, 1, 31)
            val b = LocalDate(1998, 1, 9)..LocalDate(1998, 1, 20)

            (a overlaps b).shouldBeTrue()
            (b overlaps a).shouldBeTrue()
        }

        test("끝점이 시작점과 같은 경우 true") {
            val a = LocalDate(1998, 1, 1)..LocalDate(1998, 1, 9)
            val b = LocalDate(1998, 1, 9)..LocalDate(1998, 1, 20)

            (a overlaps b).shouldBeTrue()
            (b overlaps a).shouldBeTrue()
        }

        test("겹치지 않는 경우 false") {
            val a = LocalDate(1998, 1, 1)..LocalDate(1998, 1, 9)
            val b = LocalDate(1998, 1, 10)..LocalDate(1998, 1, 20)

            (a overlaps b).shouldBeFalse()
            (b overlaps a).shouldBeFalse()
        }

        test("하루짜리 범위끼리 같은 날이면 true") {
            val a = LocalDate(1998, 1, 9)..LocalDate(1998, 1, 9)
            val b = LocalDate(1998, 1, 9)..LocalDate(1998, 1, 9)

            (a overlaps b).shouldBeTrue()
        }

        test("하루짜리 범위끼리 다른 날이면 false") {
            val a = LocalDate(1998, 1, 9)..LocalDate(1998, 1, 9)
            val b = LocalDate(1998, 1, 10)..LocalDate(1998, 1, 10)

            (a overlaps b).shouldBeFalse()
        }
    }
}

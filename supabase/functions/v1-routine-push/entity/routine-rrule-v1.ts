import { RoutineRRuleDiaryByDayV1 } from "./routine-rrule-by-day-v1.ts";

export interface RoutineRRuleV1 {
  diaryByDay: RoutineRRuleDiaryByDayV1;
  byMonthDay: number[];
}

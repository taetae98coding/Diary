import { RoutineRRuleDiaryByDay } from "./routine-rrule-by-day.ts";

export interface RoutineRRule {
  diaryByDay: RoutineRRuleDiaryByDay;
  byMonthDay: number[];
}

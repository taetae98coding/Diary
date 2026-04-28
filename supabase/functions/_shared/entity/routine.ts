import { RoutineDetail } from "./routine-detail.ts";
import { RoutineRRule } from "./routine-rrule.ts";

export interface Routine {
  id: string;
  detail: RoutineDetail;
  rRules: RoutineRRule[];
  rDates: string[];
  exDates: string[];
  isCalendarVisible: boolean;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

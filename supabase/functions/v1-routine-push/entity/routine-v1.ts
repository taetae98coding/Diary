import { RoutineDetailV1 } from "./routine-detail-v1.ts";
import { RoutineRRuleV1 } from "./routine-rrule-v1.ts";

export interface RoutineV1 {
  id: string;
  detail: RoutineDetailV1;
  rRules: RoutineRRuleV1[];
  rDates: string[];
  exDates: string[];
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

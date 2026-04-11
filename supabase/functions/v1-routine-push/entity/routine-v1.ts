export interface RoutineV1 {
  id: string;
  title: string;
  description: string;
  start: string | null;
  endInclusive: string | null;
  color: number;
  rRules: string;
  rDates: string;
  exDates: string;
  routineCount: number;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

export interface MemoDetailV1 {
  title: string;
  description: string;
  isAllDay: boolean;
  start: string | null;
  endInclusive: string | null;
  color: number;
}

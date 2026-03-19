import { MemoDetailV1 } from "./memo-detail-v1.ts";

export interface MemoV1 {
  id: string;
  detail: MemoDetailV1;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

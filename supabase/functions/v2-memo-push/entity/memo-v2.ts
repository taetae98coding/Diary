import { MemoDetailV2 } from "./memo-detail-v2.ts";

export interface MemoV2 {
  id: string;
  detail: MemoDetailV2;
  primaryTag: string | null;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

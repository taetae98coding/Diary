import { MemoDetail } from "./memo-detail.ts";

export interface Memo {
  id: string;
  detail: MemoDetail;
  primaryTag: string | null;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

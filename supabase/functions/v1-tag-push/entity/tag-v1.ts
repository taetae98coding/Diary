import { TagDetailV1 } from "./tag-detail-v1.ts";

export interface TagV1 {
  id: string;
  detail: TagDetailV1;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

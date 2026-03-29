import { TagDetail } from "./tag-detail.ts";

export interface Tag {
  id: string;
  detail: TagDetail;
  isFinished: boolean;
  isDeleted: boolean;
  updatedAt: number;
  createdAt: number;
}

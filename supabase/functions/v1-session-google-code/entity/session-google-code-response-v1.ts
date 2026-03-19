import { AccountV1 } from "./account-v1.ts";

export interface SessionGoogleCodeResponseV1 {
  accessToken: string;
  refreshToken: string;
  account: AccountV1;
}

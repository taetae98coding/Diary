import { AccountV1 } from "../../v1-session-google-code/entity/account-v1.ts";

export interface SessionGoogleIdTokenResponseV1 {
  accessToken: string;
  refreshToken: string;
  account: AccountV1;
}

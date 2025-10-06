export interface AuthResponse {
  'access-token': string;
  'refresh-token': string;
  role?: string;        // <-- add this
  roles?: string[];     // <-- or this (if backend sends multiple)
}

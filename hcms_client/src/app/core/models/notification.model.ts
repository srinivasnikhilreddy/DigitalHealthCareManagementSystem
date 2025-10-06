export interface Notification {
  id: number;
  userId: number;
  type: 'EMAIL' | 'SMS';
  message: string;
  sent: boolean;
}

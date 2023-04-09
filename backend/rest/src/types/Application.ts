import { DateTime } from 'luxon';

export interface Application {
  _id: string;
  userId: string;
  jobId: string;
  status: ApplicationStatus;
  createdAt: DateTime;
  updatedAt: DateTime;
}

export enum ApplicationStatus {
  RECEIVED,
  IN_REVIEW,
  SHORTLISTED,
  INVITED,
  HIRED,
  REGRETTED,
}

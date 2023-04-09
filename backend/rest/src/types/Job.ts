import { DateTime } from 'luxon';
import { Company } from './Company';

export type GetJobsResponse = {
  page: number;
  size: number;
  total: number;
  hasNext: boolean;
  jobs: Job[];
};

export type Job = {
  _id: string;
  positionTitle: string;
  description: string;
  company: Company;
  salaryRange: SalaryRange;
  location: Locations;
  industry: Industries;
  status: Status;
  createdAt: DateTime;
  updatedAt: DateTime;
  haveIApplied?: boolean;
};

export type SalaryRange = {
  min: number;
  max: number;
};

export enum Status {
  DRAFT,
  PUBLISHED,
  EXPIRED,
  BANNED,
}

export enum Industries {
  Technology,
  Marketing,
  HR,
}

export enum Locations {
  Australia,
  Malaysia,
  Singapore,
  Indonesia,
}

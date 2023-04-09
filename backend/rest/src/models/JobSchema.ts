import mongoose from 'mongoose';

import { Job } from '../types/Job';
import { CompanySchema } from './CompanySchema';

const JobSchema = new mongoose.Schema<Job>(
  {
    _id: String,
    positionTitle: String,
    description: String,
    company: CompanySchema,
    salaryRange: { min: Number, max: Number },
    location: Number,
    industry: Number,
    status: Number,
  },
  { timestamps: true },
);

export default mongoose.model<Job>('Job', JobSchema);

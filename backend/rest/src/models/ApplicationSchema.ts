import mongoose from 'mongoose';

import { Application } from '../types/Application';

export const ApplicationSchema = new mongoose.Schema<Application>(
  {
    userId: String,
    jobId: String,
    status: Number,
  },
  { timestamps: true },
);

export default mongoose.model<Application>('Application', ApplicationSchema);

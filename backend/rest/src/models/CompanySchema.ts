import mongoose from 'mongoose';

import { Company } from '../types/Company';

export const CompanySchema = new mongoose.Schema<Company>(
  {
    _id: String,
    name: String,
  },
  { timestamps: true },
);

export default mongoose.model<Company>('Company', CompanySchema);

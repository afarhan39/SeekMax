import mongoose from 'mongoose';

import { config } from '../config';
import JobSchema from '../models/JobSchema';
import { migrationJobs } from './data/jobs';
import { migrationCompanies } from './data/companies';
import CompanySchema from '../models/CompanySchema';
import { migrationUsers } from './data/users';
import UserSchema from '../models/UserSchema';

import crypto from 'crypto';

export const dbConnect = async (mongoDbUri: string) => {
  try {
    await mongoose.connect(mongoDbUri);
    console.info('mongo db connected');
  } catch (err: unknown) {
    console.error(err);
  }
};

const initialiseData = async () => {
  const exists = await JobSchema.exists({ _id: 'job-1' });
  if (!exists) {
    console.info({ count: migrationCompanies.length }, 'importing companies');
    await Promise.all(
      migrationCompanies.map(async (company) => {
        const c = new CompanySchema(company);
        await c.save();
      }),
    );

    console.info({ count: migrationUsers.length }, 'importing users');
    await Promise.all(
      migrationUsers.map(async (user) => {
        const u = new UserSchema(user);
        const salt = crypto.randomBytes(16).toString('hex');
        const crypted = await crypto.scryptSync(user.password, salt, 64);
        u.password = `${salt}:${crypted.toString('hex')}`;
        await u.save();
      }),
    );

    console.info({ count: migrationJobs.length }, 'importing jobs');
    await Promise.all(
      migrationJobs.map(async (job) => {
        const j = new JobSchema(job);
        await j.save();
      }),
    );
  }
};

export default () => {
  dbConnect(config.databaseUrl);
  mongoose.connection.on('open', () => {
    initialiseData().then(
      () => {
        console.info('Data initialised');
      },
      (err: Error) => console.error({ err }, 'Error initialising data'),
    );
  });
};

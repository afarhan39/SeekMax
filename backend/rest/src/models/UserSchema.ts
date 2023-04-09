import mongoose from 'mongoose';

import { User } from '../types/User';

export const UserSchema = new mongoose.Schema<User>(
  {
    _id: String,
    username: String,
    password: String,
    displayname: String,
  },
  { timestamps: true },
);

export default mongoose.model<User>('User', UserSchema);

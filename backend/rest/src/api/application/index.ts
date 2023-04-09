import { Handler } from 'express';
import ApplicationSchema from '../../models/ApplicationSchema';
import { ApplicationStatus } from '../../types/Application';

export const toggleApplicationHandler: Handler = async (req, res) => {
  const { userId } = req;
  const { jobId } = req.body;

  try {
    const application = await ApplicationSchema.findOne({ userId, jobId });
    if (application) {
      // Remove application
      application.delete();
    } else {
      const app = new ApplicationSchema({
        userId,
        jobId,
        status: ApplicationStatus.RECEIVED,
      });
      await app.save();
    }

    return res.status(201).send();
  } catch (err) {
    console.error(err);
    return res.status(500).send();
  }
};

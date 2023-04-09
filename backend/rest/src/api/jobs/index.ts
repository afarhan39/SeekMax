import { Handler } from 'express';
import jobSchema from '../../models/JobSchema';
import { GetJobsResponse, Status } from '../../types/Job';
import applicationSchema from '../../models/ApplicationSchema';

export const getPaginatedJobsHandler: Handler = async (req, res) => {
  const { per_page: perPage = 5, page = 1 } = req.query;

  try {
    const jobs = await jobSchema
      .find()
      .skip((Number(page) - 1) * Number(perPage))
      .limit(Number(perPage))
      .sort({ createdAt: 1 });

    const total = await jobSchema.find().count();

    const hasNext = (Number(page) + 1) * Number(perPage) < total;

    const jobsWithApplied = jobs.map((job) => {
      const nJob = job;
      // @ts-ignore
      nJob._doc.haveIApplied = false;
      return nJob;
    });

    if (req.userId !== 'unauthenticated') {
      // get whether the user has applied for the current page of jobs
      const applications = await applicationSchema.find({
        userId: req.userId,
        jobId: { $in: jobs.flatMap((job) => job._id) },
      });

      jobsWithApplied.forEach((job, i) => {
        if (applications.flatMap((app) => app.jobId).includes(job._id)) {
          // @ts-ignore
          jobsWithApplied[i]._doc.haveIApplied = true;
        }
      });
    }

    const response: GetJobsResponse = {
      page: Number(page),
      size: jobs.length,
      total,
      hasNext,
      jobs: jobsWithApplied,
    };

    return res.status(200).json(response);
  } catch (err) {
    res.status(500).send();
  }
};

export const getPaginatedActiveJobsHandler: Handler = async (req, res) => {
  const { per_page: perPage = 5, page = 1 } = req.query;

  try {
    const jobs = await jobSchema
      .find({ status: Status.PUBLISHED })
      .skip((Number(page) - 1) * Number(perPage))
      .limit(Number(perPage))
      .sort({ createdAt: 1 });

    if (jobs.length === 0) {
      return res.status(404).send();
    }

    const total = await jobSchema.find({ status: Status.PUBLISHED }).count();

    const hasNext = Number(page) * Number(perPage) < total;

    const jobsWithApplied = jobs.map((job) => {
      const nJob = job;
      // @ts-ignore
      nJob._doc.haveIApplied = false;
      return nJob;
    });

    if (req.userId !== 'unauthenticated') {
      // get whether the user has applied for the current page of jobs
      const applications = await applicationSchema.find({
        userId: req.userId,
        jobId: { $in: jobs.flatMap((job) => job._id) },
      });

      jobsWithApplied.forEach((job, i) => {
        if (applications.flatMap((app) => app.jobId).includes(job._id)) {
          // @ts-ignore
          jobsWithApplied[i]._doc.haveIApplied = true;
        }
      });
    }

    const response: GetJobsResponse = {
      page: Number(page),
      size: jobs.length,
      total,
      hasNext,
      jobs: jobsWithApplied,
    };

    return res.status(200).json(response);
  } catch (err) {
    console.error(err);
    return res.status(500).send();
  }
};

export const getJobHandler: Handler = async (req, res) => {
  const { id } = req.params;

  try {
    const job = await jobSchema.findOne({ _id: id });

    // @ts-ignore
    job._doc.haveIApplied = false;

    if (job && req.userId !== 'unauthenticated') {
      const application = await applicationSchema.findOne({
        userId: req.userId,
        jobId: job!._id,
      });

      if (application) {
        // @ts-ignore
        job._doc.haveIApplied = true;
      }
    }

    return res.status(200).json(job);
  } catch (err) {
    res.status(500).send();
  }
};

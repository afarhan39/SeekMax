import bodyParser from 'body-parser';
import cors from 'cors';
import express from 'express';
import { config } from './config';
import mongoDB from './bootstrap/mongoDB';
import {
  getJobHandler,
  getPaginatedActiveJobsHandler,
  getPaginatedJobsHandler,
} from './api/jobs';
import { authHandler, getUserById } from './api/auth';
import { authMiddleware, optionalAuthMiddleware } from './middleware/auth';
import { toggleApplicationHandler } from './api/application';

declare global {
  // eslint-disable-next-line @typescript-eslint/no-namespace
  namespace Express {
    export interface Request {
      userId: string;
      displayName: string;
      authenticated: boolean;
    }
  }
}

const corsOption = {
  origin: '*',
};

mongoDB();

const app = express()
  .use(cors(corsOption))
  .use(bodyParser.urlencoded({ extended: false }))
  .use(bodyParser.json())
  .get('/health', (_req, res) => {
    res.send({ status: 'ok' });
  })
  .post('/auth', authHandler)
  .get('/user/:id', [authMiddleware, getUserById])
  .get('/jobs', [optionalAuthMiddleware, getPaginatedJobsHandler])
  .get('/jobs/published', [
    optionalAuthMiddleware,
    getPaginatedActiveJobsHandler,
  ])
  .get('/jobs/:id', [optionalAuthMiddleware, getJobHandler])
  .put('/application', [authMiddleware, toggleApplicationHandler]);

const listener = app.listen(config.port, () => {
  const address = listener.address();

  if (typeof address === 'object' && address) {
    console.log(`listening on port ${address.port}`);
  }
});
export default app;

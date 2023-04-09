import { ApolloServer } from '@apollo/server';
import { startStandaloneServer } from '@apollo/server/standalone';
import { JobsAPI } from './API/JobsAPI';

// A schema is a collection of type definitions (hence "typeDefs")
// that together define the "shape" of queries that are executed against
// your data.
const typeDefs = `#graphql
  # Comments in GraphQL strings (such as this one) start with the hash (#) symbol.

  type SalaryRange {
    min: Int
    max: Int
  }
  
  # This "Job" type defines the queryable fields for every job in our data source.
  type Job {
    _id: String
    positionTitle: String
    description: String
    salaryRange: SalaryRange
    location: Int
    industry: Int
    haveIApplied: Boolean
  }
  
  type GetJobsResponse {
    page: Int
    size: Int
    hasNext: Boolean
    total: Int
    jobs: [Job]
  }

  type Query {
    active(limit: Int, page: Int): GetJobsResponse
    job(id: String!): Job
    jobs(limit: Int, page: Int): GetJobsResponse
  }
  
  type Mutation {
    auth(username: String!, password: String!): String
    apply(jobId: String!): Boolean
  }
`;

const resolvers = {
  Query: {
    // @ts-ignore
    active: async (
      _: unknown,
      { limit = '5', page = '1' },
      { dataSources, authorization }: ContextValue,
    ) => dataSources.jobsAPI.getActiveJobs(limit, page, authorization.token),
    // @ts-ignore
    job: async (_: unknown, { id }, { dataSources, authorization }) =>
      dataSources.jobsAPI.getJob(id, authorization.token),
    // @ts-ignore
    jobs: async (
      _: unknown,
      { limit = '5', page = '1' },
      { dataSources, authorization }: ContextValue,
    ) => dataSources.jobsAPI.getJobs(limit, page, authorization.token),
  },
  Mutation: {
    // @ts-ignore
    auth: async (_, { username, password }, { dataSources }: ContextValue) =>
      dataSources.jobsAPI.auth(username, password),
    // @ts-ignore
    apply: async (_, { jobId }, { dataSources, authorization }: ContextValue) =>
      dataSources.jobsAPI.apply(jobId, authorization.token),
  },
};

interface ContextValue {
  dataSources: {
    jobsAPI: JobsAPI;
  };
  authorization: {
    token: string;
  };
}

const server = new ApolloServer({
  typeDefs,
  resolvers,
});

startStandaloneServer(server, {
  listen: {
    port: 3002,
  },
  context: async ({ req }) => {
    const { cache } = server;
    return {
      dataSources: {
        jobsAPI: new JobsAPI({ cache }),
      },
      authorization: {
        token: req.headers.authorization || '',
      },
    };
  },
}).then((res) => {
  const { url } = res;
  console.log(`🚀  Server ready at: ${url}`);
});

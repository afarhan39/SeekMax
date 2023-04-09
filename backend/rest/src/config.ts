import * as process from 'process';

interface Config {
  environment: Environment;

  version: string;

  port?: number;
  databaseUrl: string;

  passwordSaltRounds: number;
  jwtSecret: string;
}

type Environment = typeof environments[number];

const environments = ['local', 'test', 'dev', 'prod'] as const;

const environment = (process.env.ENVIRONMENT as Environment) || 'local';

const defaultDbUrl = 'mongodb://127.0.0.1:27017/app';

const configs: Record<Environment, () => Omit<Config, 'environment'>> = {
  local: () => ({
    port: 3001,
    databaseUrl: process.env.DATABASE_URL || defaultDbUrl,
    version: 'local',
    passwordSaltRounds: 10,
    jwtSecret: 'quiteSecret1nn1t?',
  }),

  test: () => ({
    ...configs.local(),

    version: 'test',
  }),

  dev: () => ({
    ...configs.prod(),
  }),

  prod: () => ({
    version: process.env.VERSION || '0.0.1',
    databaseUrl: process.env.DATABASE_URL || defaultDbUrl,
    port: Number(process.env.PORT) || 3001,
    passwordSaltRounds: Number(process.env.PASSWORD_ROUNDS) || 10,
    jwtSecret: process.env.JWT_SECRET || 'quiteSecret1nn1t?',
  }),
};

export const config: Config = {
  ...configs[environment](),
  environment,
};

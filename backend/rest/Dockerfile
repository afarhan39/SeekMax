FROM node:19

WORKDIR /usr/src/app

COPY package.json ./
COPY yarn.lock ./

RUN rm -rf node_modules

COPY . .

RUN yarn

RUN yarn build

EXPOSE 3001

CMD ["yarn", "local"]


FROM node:19

WORKDIR /usr/src/app

COPY package.json ./
COPY yarn.lock ./

RUN apt-get update && apt-get install -y npm && npm install -g yarn

RUN yarn

COPY . .

RUN yarn build

EXPOSE 3002

CMD ["yarn", "start"]


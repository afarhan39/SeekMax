import { Handler } from 'express';
import jwt from 'jsonwebtoken';
import { config } from '../config';

export const authMiddleware: Handler = async (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (authHeader && authHeader !== 'null') {
    const token = authHeader.split(' ')[1];

    try {
      const jsonObject: any = jwt.verify(token, config.jwtSecret);
      if (!jsonObject.userId) {
        return res.status(401).send();
      }
      req.userId = jsonObject.userId;
      req.displayName = jsonObject.display;
      req.authenticated = true;
      next();
    } catch (err) {
      return res.status(403).send();
    }
  } else {
    return res.status(403).send();
  }
};

export const optionalAuthMiddleware: Handler = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (authHeader && authHeader !== 'null') {
    const token = authHeader.split(' ')[1];

    try {
      const jsonObject: any = jwt.verify(token, config.jwtSecret);
      if (!jsonObject.userId) {
        req.userId = 'unauthenticated';
        req.displayName = 'unauthenticated';
        req.authenticated = false;
        next();
      }
      req.userId = jsonObject.userId;
      req.displayName = jsonObject.display;
      req.authenticated = true;
      next();
    } catch (err) {
      req.userId = 'unauthenticated';
      req.displayName = 'unauthenticated';
      req.authenticated = false;
      next();
    }
  } else {
    req.userId = 'unauthenticated';
    req.displayName = 'unauthenticated';
    req.authenticated = false;
    next();
  }
};

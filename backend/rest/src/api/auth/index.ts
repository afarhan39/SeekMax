import { Handler } from 'express';
import userSchema from '../../models/UserSchema';
import crypto from 'crypto';
import jwt from 'jsonwebtoken';
import { config } from '../../config';

export const authHandler: Handler = async (req, res) => {
  const { user: username, password } = req.body;

  try {
    const user = await userSchema.findOne({ username });

    if (!user) {
      return res.status(401).send('Either username or password are wrong!1');
    }

    const [salt, key] = user.password.split(':');

    const hashedInput = await crypto.scryptSync(password, salt, 64);

    if (hashedInput.toString('hex') !== key) {
      return res.status(401).send('Either username or password are wrong!2');
    }

    const token = jwt.sign(
      { userId: user._id, display: user.displayname },
      config.jwtSecret,
      { expiresIn: '8h' },
    );

    return res.status(200).json(token);
  } catch (err) {
    return res.status(500).send();
  }
};

export const getUserById: Handler = async (req, res) => {
  const { id } = req.params;

  if (id !== req.userId) {
    return res.status(403).send();
  }

  try {
    const user = await userSchema.findOne({ _id: id });

    return res.status(200).json(user);
  } catch (err) {
    return res.status(500).send();
  }
};

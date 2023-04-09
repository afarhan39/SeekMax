import { DateTime } from 'luxon';

export const getDate = (daysAgo: number): DateTime => {
  const date = DateTime.now();
  return date.minus({ day: daysAgo });
};

import { Time12Pipe } from './time12.pipe';

describe('Time12Pipe', () => {
  it('formats 24-hour times to a zero-padded 12-hour format', () => {
    const pipe = new Time12Pipe();

    expect(pipe.transform('09:00')).toBe('09:00 AM');
    expect(pipe.transform('13:45')).toBe('01:45 PM');
  });
});

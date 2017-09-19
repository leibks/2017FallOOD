package cs3500.hw01.duration;

/**
 * Abstract base class for implementations of {@link Duration}.
 */
abstract class AbstractDuration implements Duration {
  /**
   * Constructs a {@link Duration} in a manner selected by concrete subclasses of this class.
   *
   * @param inSeconds the length in seconds
   * @return the new {@code Duration}
   */
  protected abstract Duration fromSeconds(long inSeconds);

  @Override
  public String toString() {
    return asHms();
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (!(that instanceof Duration)) {
      return false;
    }

    return ((Duration) that).inSeconds() == this.inSeconds();
  }

  @Override
  public int hashCode() {
    return Long.hashCode(inSeconds());
  }

  @Override
  public int compareTo(Duration that) {
    return Long.compare(this.inSeconds(), that.inSeconds());
  }

  @Override
  public Duration plus(Duration that) {
    return fromSeconds(this.inSeconds() + that.inSeconds());
  }

  /**
   * Converts an unpacked hours-minutes-seconds duration to its length in seconds.
   *
   * @param hours   the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @return the duration in seconds
   */
  protected static long inSeconds(int hours, int minutes, int seconds) {
    return 3600 * hours + 60 * minutes + seconds;
  }

  /**
   * Formats an unpacked hours-minutes-seconds duration in the same {@code H:MM:SS} format that
   * {@link Duration#asHms()} returns. Assumes that
   *
   * @param hours   the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @return formatted duration
   * @throws IllegalArgumentException if any argument is negative
   */
  protected static String asHms(int hours, int minutes, int seconds) {
    return String.format("%d:%02d:%02d", hours, minutes, seconds);
  }

  /**
   * Ensures that the hours, minutes, and seconds are all non-negative. Is factoring this out
   * overkill? Or should we also factor out the {@code inSeconds < 0} check in the two unary
   * constructors? Discuss.
   *
   * @param hours   the number of hours
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   * @throws IllegalArgumentException if any argument is negative
   */
  protected static void ensureHms(int hours, int minutes, int seconds) {
    if (hours < 0 || minutes < 0 || seconds < 0) {
      throw new IllegalArgumentException("must be non-negative");
    }
  }

  /**
   * Returns the number of whole hours in the given number of seconds.
   *
   * @param inSeconds the total number of seconds
   * @return the number of hours
   * @throws ArithmeticException if the correct result cannot fit in an {@code int}.
   */
  protected static int hoursOf(long inSeconds) {
    if (inSeconds / 3600 > Integer.MAX_VALUE) {
      throw new ArithmeticException("result cannot fit in type");
    }

    return (int) (inSeconds / 3600);
  }

  /**
   * Returns the number of whole minutes in the given number of seconds, less the number of whole
   * hours.
   *
   * @param inSeconds the total number of seconds
   * @return the number of remaining minutes
   */
  protected static int minutesOf(long inSeconds) {
    return (int) (inSeconds / 60 % 60);
  }

  /**
   * Returns the number of seconds remaining after all full minutes are removed from the given
   * number of seconds.
   *
   * @param inSeconds the total number of seconds
   * @return the number of remaining seconds
   */
  protected static int secondsOf(long inSeconds) {
    return (int) (inSeconds % 60);
  }



  //adding assignment of format method here
  /**
   * Formats a duration as a string by substituting for format specifiers in the template.
   * @param template the format specifiers in the template
   * @return formatted duration
    * @throws IllegalArgumentException if {@code template} is malformed
   */
  public String format(String template) {
    int size = template.length();
    String result = "";

    //for every character in the template, we do some change according to format rule
    for (int i = 0; i < size ; i++) {
      if (i == size - 1) {
        //if the last character in the template is %, that means template is malformed
        //if not, result append this character directly
        if (template.substring(size - 1, size).compareTo("%") == 0) {
          throw new IllegalArgumentException("given template is malformed");
        }
        else {
          result = result + template.substring(size - 1, size);
        }
      } else {
        String given = template.substring(i, i + 1);
        String next = template.substring(i + 1, i + 2);
        if (given.compareTo("%") == 0) {
          //jump this format specifier
          i++;
          if (next.compareTo("t") == 0) {
            result = result + this.inSeconds();
          } else if (next.compareTo("h") == 0) {
            result = result + hoursOf(this.inSeconds());
          } else if (next.compareTo("H") == 0) {
            if (hoursOf(this.inSeconds()) < 10) {
              result = result + "0" + hoursOf(this.inSeconds());
            } else {
              result = result + hoursOf(this.inSeconds());
            }
          } else if (next.compareTo("m") == 0) {
            result = result + minutesOf(this.inSeconds());
          } else if (next.compareTo("M") == 0) {
            if (minutesOf(this.inSeconds()) < 10) {
              result = result + "0" + minutesOf(this.inSeconds());
            } else {
              result = result + minutesOf(this.inSeconds());
            }
          } else if (next.compareTo("s") == 0) {
            result = result + secondsOf(this.inSeconds());
          } else if (next.compareTo("S") == 0) {
            if (secondsOf(this.inSeconds()) < 10) {
              result = result + "0" + secondsOf(this.inSeconds());
            } else {
              result = result + secondsOf(this.inSeconds());
            }
          } else if (next.compareTo("%") == 0) {
            result = result + "%";
          } else {
            throw new IllegalArgumentException("given template is malformed");
          }
        } else {
          result = result + given;
        }
      }
    }

    return result;
  }


}

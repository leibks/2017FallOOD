package cs3500.hw01.duration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the format method of {@link Duration}s. Add your tests to this class to assure that
 * your format method works properly
 */
public abstract class AbstractDurationFormatTest {


  //Test for given hms abstract duration and fixed text (string) with %h , %m, and %s
  @Test
  public void formatExample1() {
    assertEquals("4 hours, 0 minutes, and 9 seconds",
            hms(4, 0, 9)
                    .format("%h hours, %m minutes, and %s seconds"));
  }


  //Test for given hms abstract duration and fixed text (:) with %h , %m, and %s

  @Test
  public void formatExample2() {
    assertEquals("4:05:17",
            hms(4, 5, 17).format("%h:%M:%S"));
  }

  // ADD MORE TESTS HERE
  // Your tests must only use hms(...) and sec(...) to construct new Durations
  // and must *not* directly say "new CompactDuration(...)" or
  // "new HmsDuration(...)"


  // Test for given sec abstract duration and any fixed text
  @Test
  public void formatSec() {
    //Test for given sec abstract duration and any fixed text with %t and %%
    assertEquals("11115 seconds",
            sec(11115)
                    .format("%t seconds"));
    assertEquals("",
            sec(11115)
                    .format(""));
    assertEquals("11115",
            sec(11115)
                    .format("%t"));
    //special and non-meaning but correct template
    assertEquals("11115%seconds",
            sec(11115)
                    .format("%t%%seconds"));
    assertEquals("120%s",
            sec(120)
                    .format("%t%%s"));
    assertEquals("%120s/",
            sec(120)
                    .format("%%%ts/"));
  }


  /**
   * Test for given sec abstract duration and fixed test (string)
   * with :  %h, %m, %s, %H, %M, %S.
   */
  public void formatSec2() {
    assertEquals("1 hours, 40 minutes, and 10 seconds/",
            sec(6010)
                    .format("%h hours, %m minutes, and %s seconds/"));
    assertEquals("1 hours, 40 minutes, and 10 seconds/",
            sec(6010)
                    .format("%h hours, %M minutes, and %s seconds/"));
    assertEquals("12 hours, 05 minutes, and 10 seconds/",
            sec(43510)
                    .format("%h hours, %M minutes, and %S seconds/"));
    assertEquals("01 hours, 05 minutes, and 00 seconds/",
            sec(3900)
                    .format("%H hours, %M minutes, and %S seconds/"));

  }


  /**
   * Test for given sec abstract duration and any fixed text
   * with %h, %m, %s, %H, %M, %S, %t and %%.
   */
  public void formatSec3() {
    assertEquals("03 hr 10 minutes, %--3 hours, 05",
            sec(11405)
                    .format("%H hr %S minutes, %%--%h hours, %S"));
    assertEquals("01 hours, 05 minutes, and 00 seconds/, 3900,"
                    + "1 hours, 5 minutes, and 0 seconds, %",
            sec(3900)
                    .format("%H hours, %M minutes, and %S seconds/, %t,"
                            +  "%h hours, %m minutes, and %s seconds, %%"));
    assertEquals("03 hours, 30 seconds, 3 h, 10 minutes, %11430",
            sec(11430)
                    .format("%H hours, %S seconds, %h h, %m minutes, %%%t"));
  }

  //Test for given sec abstract duration illegal situations
  @Test(expected = IllegalArgumentException.class)
  public void hmsShowUnavailableSpecifier() {
    sec(120)
            .format("%%xas%q%t");
    sec(120)
            .format("%%xas%%%`:");
    sec(120)
            .format("%%%!d%q%t");
    sec(120)
            .format("%%d%%%3");
    sec(120)
            .format("%%d%%%/%h%H%t");
  }

  @Test(expected = IllegalArgumentException.class)
  public void hmsUninterpretedCharaFalse() {
    //individual %
    sec(120)
            .format("%t%");
    sec(120)
            .format("%%t%%%");
  }

  //Test for given hms abstract duration and any fixed text

  /**
   * Test for given hms abstract duration and fixed text (string)
   * with %h, %m, %s, %H, %M, and %S.
   */
  @Test
  public void formatHms1() {
    assertEquals("5 hours, 8 minutes, and 5 seconds",
            hms(5, 8, 5)
                    .format("%h hours, %m minutes, and %s seconds"));
    assertEquals("15 h, 0 minutes, and 5 seconds",
            hms(15, 0, 5)
                    .format("%H h, %m minutes, and %s seconds"));
    assertEquals("09 hours, 10 minutes, and 05 s",
            hms(9, 10, 5)
                    .format("%H hours, %m minutes, and %S s"));
    assertEquals("05 hr, 08 minutes, and 05 sec",
            hms(5, 8, 5)
                    .format("%H hr, %M minutes, and %S sec"));
  }

  /**
   * Test for given hms abstract duration and any fixed text with %t and %%.
   */
  public void formatHms2() {
    assertEquals("18485",
            hms(5, 8, 5)
                    .format("%t"));
    assertEquals("",
            hms(5, 8, 5)
                    .format(""));
    assertEquals("18485%--//",
            hms(5, 8, 5)
                    .format("%t%%--//"));
    assertEquals("%_18485_18485",
            hms(5, 8, 5)
                    .format("%%_%t_%t"));
  }

  /**
   * Test for given hms abstract duration and any fixed text
   * with : %h, %m, %s, %H, %M, %S, %t and %%.
   */
  public void formatHms3() {
    assertEquals("05 hr, 08 minutes, and 05 sec"
                    + "5 hours, 8 minutes, and 5 seconds" + "18545 + %",
            hms(5, 8, 5)
                    .format("%H hr, %M minutes, and %S sec"
                            + "%h hours, %m minutes, and %s seconds" + "%t + %%"));
    assertEquals("05 hr, 05 sec 18 minutes, "
                    + "and 5 seconds 19085 + %",
            hms(5, 18, 5)
                    .format("%H hr, %S sec %m minutes, "
                            + "and %s seconds %t + %%"));
    assertEquals("051235 s ec",
            hms(5, 8, 5)
                    .format("%H123%s s ec"));
    assertEquals("5:0:0 05,0,/0",
            hms(5, 0, 0)
                    .format("%h:%m:%s %H,%m,/%s"));
  }


  // Test for given hms abstract duration illegal situations
  @Test(expected = IllegalArgumentException.class)
  public void secShowUnavailableSpecifier() {
    hms(5, 0, 0)
            .format("%%xas%q%h%H");
    hms(5, 0, 0)
            .format("%%%s%xas%%%`:");
    hms(5, 0, 0)
            .format("%%%!d%q%t");
    hms(5, 0, 0)
            .format("%%d%%%3");
    hms(5, 0, 0)
            .format("%%d%%%/%h%H%t");
  }

  @Test(expected = IllegalArgumentException.class)
  public void secUninterpretedCharaFalse() {
    //individual %
    hms(5, 0, 0)
            .format("%t%");
    hms(5, 0, 0)
            .format("%%t%%%");
  }


  /*
    Leave this section alone: It contains two abstract methods to
    create Durations, and concrete implementations of this testing class
    will supply particular implementations of Duration to be used within
    your tests.
   */

  /**
   * Constructs an instance of the class under test representing the duration given in hours,
   * minutes, and seconds.
   *
   * @param hours   the hours in the duration
   * @param minutes the minutes in the duration
   * @param seconds the seconds in the duration
   * @return an instance of the class under test
   */
  protected abstract Duration hms(int hours, int minutes, int seconds);

  /**
   * Constructs an instance of the class under test representing the duration given in seconds.
   *
   * @param inSeconds the total seconds in the duration
   * @return an instance of the class under test
   */
  protected abstract Duration sec(long inSeconds);

  public static final class HmsDurationTest extends AbstractDurationFormatTest {
    @Override
    protected Duration hms(int hours, int minutes, int seconds) {
      return new HmsDuration(hours, minutes, seconds);
    }

    @Override
    protected Duration sec(long inSeconds) {
      return new HmsDuration(inSeconds);
    }
  }

  public static final class CompactDurationTest extends AbstractDurationFormatTest {
    @Override
    protected Duration hms(int hours, int minutes, int seconds) {
      return new CompactDuration(hours, minutes, seconds);
    }

    @Override
    protected Duration sec(long inSeconds) {
      return new CompactDuration(inSeconds);
    }
  }
}

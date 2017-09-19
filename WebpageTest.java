/* Assignment 1
lei bowen
NUID: 001693669
bowenleis
created on September 15, 2017
*/

package cs3500.hw01.publication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;



public class WebpageTest {
  Publication cs3500 = new Webpage("CS3500: Object-Oriented Design",
          "http://www.ccs.neu.edu/course/cs3500/",
           "August 11, 2014");

  Publication cs3200 = new Webpage("CS 3200: Database Design",
          "http://www.ccs.neu.edu/home/kathleen/",
          "September 1, 2016");

  //test for webpage cs3500 in Apa format
  @Test
  public void testCiteApaFor3500() {
    assertEquals("CS3500: Object-Oriented Design."
                    + " Retrieved August 11, 2014, from "
                    + "http://www.ccs.neu.edu/course/cs3500/.",
            cs3500.citeApa());
  }

  //test for webpage cs3500 in Mla format
  @Test
  public void testCiteMlaFor3500() {
    assertEquals("\"CS3500: Object-Oriented Design.\" Web. "
                    + "August 11, 2014 <"
                    + "http://www.ccs.neu.edu/course/cs3500/>.",
            cs3500.citeMla());
  }

  //test for webpage cs3200 in Apa format
  @Test
  public void testCiteApaFor3200() {
    assertEquals("CS 3200: Database Design."
                    + " Retrieved September 1, 2016, from "
                    + "http://www.ccs.neu.edu/home/kathleen/.",
            cs3200.citeApa());
  }

  //test for webpage cs3200 in Mla format
  @Test
  public void testCiteMlaFor3200() {
    assertEquals("\"CS 3200: Database Design.\" Web. "
                    + "September 1, 2016 <"
                    + "http://www.ccs.neu.edu/home/kathleen/>.",
            cs3200.citeMla());
  }


}
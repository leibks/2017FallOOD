// Assignment 1
// lei bowen
// NUID: 001693669
//bowenleis
//created on September 15, 2017

package cs3500.hw01.publication;

/**
 * Represents bibliographic information for webpage.
 */
public class Webpage implements Publication {
  private final String title;
  private  final String url;
  private final String retrieved;

  /**
   * Constructs an Webpage.
   *
   * @param title   the title of the Webpage
   * @param url  the url of the Webpage
   * @param retrieved the retrieved date of the Webpage
   */
  public Webpage(String title, String url, String retrieved) {
    this.title = title;
    this.url = url;
    this.retrieved = retrieved;
  }

  //method to format web page citations in APA style
  @Override
  public String citeApa() {
    return title + ". Retrieved "
            + retrieved + ", from " + url + ".";
  }

  //method to format web page citation in MLA style
  @Override
  public String citeMla() {
    return "\"" + title + ".\" Web. "
            + retrieved + " <" + url + ">.";
  }
}

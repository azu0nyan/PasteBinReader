import org.jsoup.Jsoup
import scala.collection.JavaConverters._

object Reader {
  var readed:Set[String]= Set()
  def main(args: Array[String]): Unit = {
    println("Paste reader started.")
    while (true) {
      checkForUpdates()
      Thread.sleep(1000)
    }
  }

  def checkForUpdates():Unit = {
    println("Downloading paste list.....")
    val doc = Jsoup.connect("https://pastebin.com/archive").get()
    println("Downloaded")
    val table = doc.select(".maintable")
    val hrefs = table.select("a[href]")
    val links = hrefs.asScala.
      map(x => x.attr("href")).
      filter(x => !x.contains("archive")).
      filter(x => !(readed contains x)).
      foreach(x => {
        readed = readed + x
        downloadAndPrint(x)
        Thread.sleep(1000)
      })
  }

  def downloadAndPrint(id:String):Unit = {
    println("Downloading page id:" + id)
    println("---------------------------------------------------------------")
    val downloaded = Jsoup.connect("https://pastebin.com" + id).get()
    val lines = downloaded.select("#code_frame").select(".li1")
    lines.asScala.foreach(x => println(x.text()))
    println("---------------------------------------------------------------")
  }
}
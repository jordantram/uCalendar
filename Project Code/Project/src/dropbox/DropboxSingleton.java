package dropbox;

public class DropboxSingleton {
  private static DropboxClient dropbox = new DropboxClient();
  
  private DropboxSingleton() {}
  
  public static DropboxClient getInstance() {
    return dropbox;
  }
}

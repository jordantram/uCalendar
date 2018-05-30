package dropbox;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.UploadErrorException;

public class TestClient {
	public static void main(String[] args) throws UploadErrorException, FileNotFoundException, IOException, DbxException {
		DropboxClient dbc = new DropboxClient();
		// Alternate which tests you run to not get confused.
//		dbc.uploadFile("C:\\Users\\user\\Desktop\\ClientClassTest.txt", "/ClientClassTest.txt");
//		dbc.uploadFile("C:\\Users\\user\\Desktop\\test.txt", "/test.txt");
//		dbc.deleteFile("/" + "test.txt");
		dbc.createFolder("/" + "TestFolder");
//		dbc.uploadFile("C:\\Users\\user\\Desktop\\test.txt", "/TestFolder/test.txt");
//		dbc.deleteFile("/" + "TestFolder");
//		 Root folder on dropbox
		for (String path : dbc.listFolder("")) {
			System.out.println(path);
			System.out.println("NAME: " + DropboxUtilities.getNameFromPath(path));
		}
		System.out.println();
//		TestFolder located at root folder.
		for (String path : dbc.listFolder("/TestFolder")) {
			System.out.println(path);
			System.out.println("NAME: " + DropboxUtilities.getNameFromPath(path));
		}
		dbc.readFile("C:\\Users\\user\\Desktop", "/DownloadTest.txt");
	}

}

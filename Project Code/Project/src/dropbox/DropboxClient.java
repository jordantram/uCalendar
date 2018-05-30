package dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class DropboxClient {
	private static final String ACCESS_TOKEN = "Pv42xRRoG2AAAAAAAAAAGrZUBgi0Ur1oTMIlN6TLZb4OZlkCorat0xX2okX2om83";
	private DbxRequestConfig config = null;
	private DbxClientV2 client = null;
	private FullAccount account = null;

	public DropboxClient() {
		// Create Dropbox client
		config = DbxRequestConfig.newBuilder("uCalendar/1.0").build();
		client = new DbxClientV2(config, ACCESS_TOKEN);

		try {
			this.account = client.users().getCurrentAccount();
			System.out.println(account.getName().getDisplayName());
		} 
		catch (DbxException dbxe) {
			dbxe.printStackTrace();
		}
	}

	/*
	 * Upload a file to the specified folder in the dropbox cloud.
	 * @param path A string object representing the local absolute path to the file to be uploaded.
	 * @param destination A string object specifying the destination folder for the file on the cloud.
	 */
	public void uploadFile(String pathToFile, String destination) {
		try {
			InputStream in = new FileInputStream(pathToFile);
			@SuppressWarnings("unused")
			FileMetadata metadata = client.files().uploadBuilder(destination).uploadAndFinish(in);
		}
		catch (FileNotFoundException fne)
		{
			fne.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (DbxException dbxe)
		{
			dbxe.printStackTrace();
		}
	}

	/*
	 * Remove the file at the specified path on the dropbox cloud.
	 * @param dropboxPath A String object that speficies a path of the file to be deleted.
	 */
	public void deleteFile(String dropboxPath) {
		try {
			@SuppressWarnings({ "deprecation", "unused" })
			Metadata metadata = client.files().delete(dropboxPath);
		} 
		catch (DbxException dbxe) {
			dbxe.printStackTrace();
		}
	}

	/*
	 * Creates a folder on the dropbox cloud at the desired path.
	 * @param folderPath A String objectrepresenting a path on the dropbox cloud for the folder.
	 */
	public void createFolder(String folderPath) throws DbxException {
		try {
			@SuppressWarnings("deprecation")
			FolderMetadata folder = client.files().createFolder(folderPath);
			System.out.println(folder.getName());
		} 
		catch (CreateFolderErrorException err) {
			if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
				System.out.println("Something already exists at the path.");
			} else {
				System.out.print("Some other CreateFolderErrorException occurred...");
				System.out.print(err.toString());
			}
		} 
		catch (Exception err) {
			System.out.print("Some other Exception occurred...");
			System.out.print(err.toString());
		}
	}

	/*
	 * Returns a list of all the file paths starting from the specified folderPath 
	 * on the dropbox cloud.
	 * @param folderPath Absolute path to the folder in dropbox. See examples in TestClient.
	 * @return A list of strings representing each representing file paths.
	 */
	public List<String> listFolder(String folderPath) {
		List<String> resultList = new ArrayList<String>();
		try {
			ListFolderResult result;
			// Contents of root direcotry.
			if (folderPath.equals("")) {
				// Get files and folder metadata from Dropbox root directory
				result = client.files().listFolder("");				
			}
			// Contents of folder specified by the dropbox path folderPath. 
			else {
				// Get files and folder metadata from Dropbox root directory
				result = client.files().listFolder(folderPath);
			}
			while (true) {
				for (Metadata metadata : result.getEntries()) {
//					System.out.println(metadata.getPathLower());
					resultList.add(metadata.getPathLower());
				}
				if (!result.getHasMore()) {
					break;
				}
				result = client.files().listFolderContinue(result.getCursor());
			}
			return resultList;
		}
		catch (DbxException dbxe) {
			dbxe.printStackTrace();
		}         
		return resultList;
	}

	/*
	 * Downloads a file off of the dropbox cloud into the specified location.
	 * @param localPath A String object representing the directory to place the file in.
	 * @param dropboxPath A String object the absolute path of the file on dropbox.
	 */
	public void readFile(String localPath, String foldername) {
		try {
			String filename = DropboxUtilities.getNameFromPath(foldername);
			File outputFile = new File(localPath + "\\" + filename);
			System.out.println(outputFile.getAbsolutePath());
			//output file for download --> storage location on local system to download file
			FileOutputStream downloadFile = new FileOutputStream(outputFile.getAbsolutePath());
			try {
				@SuppressWarnings("unused")
				FileMetadata metadata = client.files().downloadBuilder(foldername).download(downloadFile);
			} finally {
				downloadFile.close();
			}
		}
		//exception handled
		catch (DbxException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Reconfigures this dropbox client to work with another dropbox account.
	 * @param accessToken the A string representing the authentication token of another account.  
	 */
	public void reconfigureClient(String accessToken) {
		if (accessToken == null) {
			throw new IllegalArgumentException();
		}
		if (accessToken.trim().length() == 0) {
			throw new IllegalArgumentException();
		}
		// Create Dropbox client
		config = DbxRequestConfig.newBuilder("uCalendar/1.0").build();
		client = new DbxClientV2(config, accessToken);

		try {
			this.account = client.users().getCurrentAccount();
			System.out.println(account.getName().getDisplayName());
		} 
		catch (DbxException dbxe) {
			dbxe.printStackTrace();
		}
	}
}

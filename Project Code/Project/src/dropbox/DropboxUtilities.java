package dropbox;

public class DropboxUtilities {
	
	public DropboxUtilities() {
		
	}
	
	/*
	 * Extracts the name of the file or folder from its path on dropbox servers
	 * and returns it.
	 * @param dropboxPath The absolute path of the file or folder on dropbox.
	 * @return A string representing the name of the file or folder.
	 */
	public static String getNameFromPath(String dropboxPath) {
		if (dropboxPath == null) {
			throw new NullPointerException();
		}
		if (dropboxPath.isEmpty()) {
			throw new IllegalArgumentException();
		}
		String[] splitString = dropboxPath.split("/");
		return splitString[splitString.length - 1];
	}
}

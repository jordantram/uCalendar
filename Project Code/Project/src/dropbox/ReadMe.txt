 * IF YOU ARE SEEING A RED EXCLAMATION MARK ON THE PROJECT FOLDER ICON:
 * The build path got messed up. Probably because you cloned someone else's changes and hence their build path.
 * To fix this, right click on 'JRE System Library', go to 'Build Path', and click on 'Configure Build Path...'.
 * You will see for .jar file icons with red X's on them. 
 * Select all four of them and click 'Remove'. Then click 'Apply'.
 * Click 'Add External JARS...' and locate the jars in the '...\project-team-20\DropboxJars' folder.
 * Add all four of them and click 'Apply'.
 * Click 'Apply and Close'.
 * Done!
 
 Note: I tried just adding the jars to the project folder to avoid this crap, but build paths still get broken.
 The issue their is that eclipse caches (somewhere) all project related data and does not overwrite them when deleting and re-importing
 the .jar files. This only results in even worst errors and breaks compilation and build paths even more. For the sake of finishing
 this project on time, lets just stick to the steps I outline above.
 
 
 
******************** DROPBOX CLIENT INTERACTION INFO BELOW ********************
 
 
 
 DropboxClinet method documentation:
 NOTE: consult TestClient for concrete examples, and doc-strings in DropboxClient.
 
 uploadFile(p1, p2):
 - p1: the absolute path of the FILE (not folder) on your system that you want to upload.
 - e.g. C:\\Users\\user\\Desktop\\ClientClassTest.txt
 - p2: the absolute path on dropbox to which you want to upload.
 - e.g. for root directory: /ClientClassTest.txt
 - e.g. for some folder THAT WAS ALREADY CREATED: /TestFolder/test.txt
 
 deleteFile(p1):
 - p1: the absolute path on dropbox of the file you want deleted.
 - e.g. /test.txt
 NOTE: file or folder being deleted must be present prior to deletion.
 
 createFolder(p1):
 - p1: The directory in which you want to create the folder plus the name of the new folder.
 - e.g. "/" + "TestFolder" where "/" is the root directory and "TestFolder" is the name of the 
 		folder you are about to create.
 NOTE: dropbox sometime treats the root directory with the name "" (the empty string) like
 	   in the readFolder method. User "" or "/" based on the method you are accessing.
 	   When in doubt consult this ReadMe, the example tests in TestClient, and the doc-strings
 	   in the DropboxClient class.
 	   
listFolder(p1):
- p1 the name of the folder you want to list the contents of.
- e.g. use "" (the empty string) for the root folder.
- e.g. use "/TestFolder" for a folder located inside the dropbox root directory.

readFile(p1, p2):
- p1: the absolute path of the directory you want to place your file into on your machine.
- e.g. C:\\Users\\user\\Desktop
- p2: the absolute path of the file on dropbox that you want to download.
- e.g. /DownloadTest.txt 
NOTE: the file must already exist.
 	   
 	   
 	   
 	   
 	   
 	   
 	   
 	   
package frontend_coursepage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.dropbox.core.DbxException;

import dropbox.DropboxSingleton;
import dropbox.DropboxUtilities;
import frontend.FrontendStartup;
import frontend_coursepage.CoursePage;
import notes.Note;
import notes.NoteReader;

public class CoursePageController {

	private static CoursePage coursePage;
	
	public static void setCoursePage(CoursePage crsPage) {
		coursePage = crsPage;
		setActionListener();
	}
	
	public static void setActionListener() {
		coursePage.getPreviousButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				coursePage.getLocalNoteList().clearSelection();
				coursePage.getDropBoxNoteList().clearSelection();
				coursePage.getNoteDisplayTextArea().setText("");
				coursePage.getDeleteNoteButton().setEnabled(false);
				coursePage.getUploadNoteButton().setEnabled(false);
				coursePage.getDownloadNoteButton().setEnabled(false);
				coursePage.getDeleteFromDropBoxButton().setEnabled(false);
				FrontendStartup.switchMainPage();
			}
			
		});
		
		coursePage.getAddNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				importFile();
			}
		});
		
		coursePage.getDeleteNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteNote();
				coursePage.getDeleteNoteButton().setEnabled(false);
				coursePage.getUploadNoteButton().setEnabled(false);
			}
			
		});
		
		coursePage.getShowDropBoxButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				coursePage.getShowDropBoxButton().setEnabled(false);
				try {
					DropboxSingleton.getInstance().createFolder(String.format("/%s", coursePage.getCourse().getCourseCode()));
				} catch (DbxException e) {
					e.printStackTrace();
				}
				if(!coursePage.getLocalNoteList().isSelectionEmpty()) {
					coursePage.getUploadNoteButton().setEnabled(true);
				}
				updateDropBoxNoteListModel();
			}
		});
		
		coursePage.getUploadNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				uploadNote();
				coursePage.getLocalNoteList().clearSelection();
				coursePage.getNoteDisplayTextArea().setText("");
				coursePage.getDeleteNoteButton().setEnabled(false);
				coursePage.getUploadNoteButton().setEnabled(false);
				coursePage.getDownloadNoteButton().setEnabled(false);
				coursePage.getDeleteFromDropBoxButton().setEnabled(false);
			}
		});
		
		coursePage.getDownloadNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				downloadNote();
				coursePage.getDownloadNoteButton().setEnabled(false);
				coursePage.getDropBoxNoteList().clearSelection();
				coursePage.getNoteDisplayTextArea().setText("");
				coursePage.getDeleteNoteButton().setEnabled(false);
				coursePage.getUploadNoteButton().setEnabled(false);
				coursePage.getDeleteFromDropBoxButton().setEnabled(false);
			}
		});
		
		coursePage.getDeleteFromDropBoxButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteFromDropBox();
				coursePage.getDeleteFromDropBoxButton().setEnabled(false);
				coursePage.getDownloadNoteButton().setEnabled(false);
				coursePage.getDropBoxNoteList().clearSelection();
			}
		});
		
		coursePage.getSortBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            	coursePage.setSortOperation(coursePage.getSortBox().getSelectedItem().toString());
            }
        });
		
		coursePage.getJMenuBar().getDeleteCourseButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				coursePage.getLocalNoteList().clearSelection();
				coursePage.getDropBoxNoteList().clearSelection();
				coursePage.getNoteDisplayTextArea().setText("");
				coursePage.getDeleteNoteButton().setEnabled(false);
				coursePage.getUploadNoteButton().setEnabled(false);
				coursePage.getDownloadNoteButton().setEnabled(false);
				coursePage.getDeleteFromDropBoxButton().setEnabled(false);
				FrontendStartup.deleteCourseAndSwitch(coursePage.getCourse());
			}
		});
		
		coursePage.getLocalNoteList().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!coursePage.getLocalListModel().isEmpty() && !coursePage.getLocalNoteList().isSelectionEmpty()) {
					coursePage.getDeleteNoteButton().setEnabled(true);
					if(!coursePage.getShowDropBoxButton().isEnabled()) {
						coursePage.getUploadNoteButton().setEnabled(true);
					}
					displayNoteOnTextArea();
				}
			}
		});
		
		coursePage.getDropBoxNoteList().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!coursePage.getDropBoxListModel().isEmpty() && !coursePage.getDropBoxNoteList().isSelectionEmpty()) {
					coursePage.getDownloadNoteButton().setEnabled(true);
					coursePage.getDeleteFromDropBoxButton().setEnabled(true);
				}
			}
		});
	}
	
	public static void displayNoteOnTextArea() {
		coursePage.getNoteDisplayTextArea().setText(NoteReader.getNoteContents(coursePage.getLocalNoteList().getSelectedValue().getNoteFilePath()));
		coursePage.getNoteDisplayTextArea().setCaretPosition(0);
	}
	
	public static void deleteFromDropBox() {
		DropboxSingleton.getInstance().deleteFile("/" + coursePage.getCourse().getCourseCode() + "/" + coursePage.getDropBoxNoteList().getSelectedValue());
		updateDropBoxNoteListModel();
	}
	
	public static void uploadNote() {
		Note note = coursePage.getLocalNoteList().getSelectedValue();
		DropboxSingleton.getInstance().uploadFile(note.getNoteFilePath(), "/" + coursePage.getCourse().getCourseCode() + "/" + note.getNote());
		updateDropBoxNoteListModel();
	}
	
	public static void downloadNote() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(null);
		
		if(fileChooser.getSelectedFile() != null){
			DropboxSingleton.getInstance().readFile(fileChooser.getSelectedFile().getAbsolutePath(), String.format("/%s/%s",coursePage.getCourse().getCourseCode(), coursePage.getDropBoxNoteList().getSelectedValue()));
			Note note = new Note(coursePage.getDropBoxNoteList().getSelectedValue());
			note.setNoteFilePath(fileChooser.getSelectedFile().getAbsolutePath() + File.separator + coursePage.getDropBoxNoteList().getSelectedValue());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			addNote(LocalDate.now().format(formatter), note);
		}
	}
	
	public static void importFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.showOpenDialog(null);
		
		if(fileChooser.getSelectedFile() != null){
			Note note = new Note(fileChooser.getSelectedFile().getName());
			note.setNoteFilePath(fileChooser.getSelectedFile().getAbsolutePath());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			addNote(LocalDate.now().format(formatter), note);
		}
	}
	
	public static void addNote(String date, Note note) {
		coursePage.getCourse().addNote(date, note);
		updateLocalNoteListModel();
	}
	
	public static void deleteNote() {
		Note note = coursePage.getLocalNoteList().getSelectedValue();
		coursePage.getNoteDisplayTextArea().setText("");
		coursePage.getCourse().removeNote(note.getNoteDate(), note);
		updateLocalNoteListModel();
	}
	
	public static void updateLocalNoteListModel() {
		coursePage.getLocalListModel().clear();
		List<Note> notes;
		if(coursePage.getSortOperation().equals("Oldest")){
			notes = coursePage.getCourse().getNotesIncreasing();
		}else {
			notes = coursePage.getCourse().getNotesIncreasing();
		}
		
		for(Note no: notes) {
			coursePage.getLocalListModel().addElement(no);
		}
		
		coursePage.revalidate();
		coursePage.repaint();
	}
	
	public static void updateDropBoxNoteListModel() {
		coursePage.getDropBoxListModel().clear();
		
		for (String path : DropboxSingleton.getInstance().listFolder("/" + coursePage.getCourse().getCourseCode())) {
			coursePage.getDropBoxListModel().addElement(DropboxUtilities.getNameFromPath(path));
		}
		
		coursePage.revalidate();
		coursePage.repaint();
	}

}

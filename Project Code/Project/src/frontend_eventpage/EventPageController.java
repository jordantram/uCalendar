package frontend_eventpage;

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
import notes.Note;
import notes.NoteReader;

public class EventPageController {
	
	private static EventPage eventPage;
	
	public static void setEventPage(EventPage evtPage) {
		eventPage = evtPage;
		setActionListener();
	}
	
	public static void setActionListener() {
		eventPage.getPreviousButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				eventPage.getLocalNoteList().clearSelection();
				eventPage.getDropBoxNoteList().clearSelection();
				eventPage.getNoteDisplayTextArea().setText("");
				eventPage.getDeleteNoteButton().setEnabled(false);
				eventPage.getUploadNoteButton().setEnabled(false);
				eventPage.getDownloadNoteButton().setEnabled(false);
				eventPage.getDeleteFromDropBoxButton().setEnabled(false);
				FrontendStartup.switchMainPage();
			}
			
		});
		
		eventPage.getAddNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				importFile();
			}
		});
		
		eventPage.getDeleteNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteNote();
				eventPage.getDeleteNoteButton().setEnabled(false);
				eventPage.getUploadNoteButton().setEnabled(false);
			}
			
		});
		
		eventPage.getShowDropBoxButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventPage.getShowDropBoxButton().setEnabled(false);
				try {
					DropboxSingleton.getInstance().createFolder("/events");
				} catch (DbxException e) {
					e.printStackTrace();
				}
				if(!eventPage.getLocalNoteList().isSelectionEmpty()) {
					eventPage.getUploadNoteButton().setEnabled(true);
				}
				updateDropBoxNoteListModel();
			}
		});
		
		eventPage.getUploadNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				uploadNote();
				eventPage.getLocalNoteList().clearSelection();
				eventPage.getNoteDisplayTextArea().setText("");
				eventPage.getDeleteNoteButton().setEnabled(false);
				eventPage.getUploadNoteButton().setEnabled(false);
				eventPage.getDownloadNoteButton().setEnabled(false);
				eventPage.getDeleteFromDropBoxButton().setEnabled(false);
			}
		});
		
		eventPage.getDownloadNoteButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				downloadNote();
				eventPage.getDownloadNoteButton().setEnabled(false);
				eventPage.getDropBoxNoteList().clearSelection();
				eventPage.getNoteDisplayTextArea().setText("");
				eventPage.getDeleteNoteButton().setEnabled(false);
				eventPage.getUploadNoteButton().setEnabled(false);
				eventPage.getDeleteFromDropBoxButton().setEnabled(false);
			}
		});
		
		eventPage.getDeleteFromDropBoxButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteFromDropBox();
				eventPage.getDeleteFromDropBoxButton().setEnabled(false);
				eventPage.getDownloadNoteButton().setEnabled(false);
				eventPage.getDropBoxNoteList().clearSelection();
			}
		});
		
		eventPage.getSortBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            	eventPage.setSortOperation(eventPage.getSortBox().getSelectedItem().toString());
            }
        });
		
		eventPage.getJMenuBar().getDeleteEventButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventPage.getLocalNoteList().clearSelection();
				eventPage.getDropBoxNoteList().clearSelection();
				eventPage.getNoteDisplayTextArea().setText("");
				eventPage.getDeleteNoteButton().setEnabled(false);
				eventPage.getUploadNoteButton().setEnabled(false);
				eventPage.getDownloadNoteButton().setEnabled(false);
				eventPage.getDeleteFromDropBoxButton().setEnabled(false);
				FrontendStartup.deleteEventAndSwitch(eventPage.getEvent());
			}
		});
		
		eventPage.getLocalNoteList().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!eventPage.getLocalListModel().isEmpty() && !eventPage.getLocalNoteList().isSelectionEmpty()) {
					eventPage.getDeleteNoteButton().setEnabled(true);
					if(!eventPage.getShowDropBoxButton().isEnabled()) {
						eventPage.getUploadNoteButton().setEnabled(true);
					}
					displayNoteOnTextArea();
				}
			}
		});
		
		eventPage.getDropBoxNoteList().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!eventPage.getDropBoxListModel().isEmpty() && !eventPage.getDropBoxNoteList().isSelectionEmpty()) {
					eventPage.getDownloadNoteButton().setEnabled(true);
					eventPage.getDeleteFromDropBoxButton().setEnabled(true);
				}
			}
		});
	}
	
	public static void displayNoteOnTextArea() {
		eventPage.getNoteDisplayTextArea().setText(NoteReader.getNoteContents(eventPage.getLocalNoteList().getSelectedValue().getNoteFilePath()));
		eventPage.getNoteDisplayTextArea().setCaretPosition(0);
	}
	
	public static void deleteFromDropBox() {
		DropboxSingleton.getInstance().deleteFile("/events/" + eventPage.getDropBoxNoteList().getSelectedValue());
		updateDropBoxNoteListModel();
	}
	
	public static void uploadNote() {
		Note note = eventPage.getLocalNoteList().getSelectedValue();
		DropboxSingleton.getInstance().uploadFile(note.getNoteFilePath(), "/events/" + note.getNote());
		updateDropBoxNoteListModel();
	}
	
	public static void downloadNote() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(null);
		
		if(fileChooser.getSelectedFile() != null){
			DropboxSingleton.getInstance().readFile(fileChooser.getSelectedFile().getAbsolutePath(), String.format("/events/%s", eventPage.getDropBoxNoteList().getSelectedValue()));
			Note note = new Note(eventPage.getDropBoxNoteList().getSelectedValue());
			note.setNoteFilePath(fileChooser.getSelectedFile().getAbsolutePath() + File.separator + eventPage.getDropBoxNoteList().getSelectedValue());
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
		eventPage.getEvent().addNote(date, note);
		updateLocalNoteListModel();
	}
	
	public static void deleteNote() {
		Note note = eventPage.getLocalNoteList().getSelectedValue();
		eventPage.getNoteDisplayTextArea().setText("");
		eventPage.getEvent().removeNote(note.getNoteDate(), note);
		updateLocalNoteListModel();
	}
	
	public static void updateLocalNoteListModel() {
		eventPage.getLocalListModel().clear();
		List<Note> notes;
		if(eventPage.getSortOperation().equals("Oldest")){
			notes = eventPage.getEvent().getNotesIncreasing();
		}else {
			notes = eventPage.getEvent().getNotesIncreasing();
		}
		
		for(Note no: notes) {
			eventPage.getLocalListModel().addElement(no);
		}
		
		eventPage.revalidate();
		eventPage.repaint();
	}
	
	public static void updateDropBoxNoteListModel() {
		eventPage.getDropBoxListModel().clear();
		
		for (String path : DropboxSingleton.getInstance().listFolder("/events")) {
			eventPage.getDropBoxListModel().addElement(DropboxUtilities.getNameFromPath(path));
		}
		
		eventPage.revalidate();
		eventPage.repaint();
	}

}

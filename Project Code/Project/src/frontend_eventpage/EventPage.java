package frontend_eventpage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.dropbox.core.DbxException;

import event.Event;
import frontend.FrontEndUtilities;
import frontend_mainpage.PreviousButton;
import notes.Note;

@SuppressWarnings("serial")
public class EventPage extends JPanel{

	private Event event;
	private EventJMenu jMenu;
	private JPanel displayPanel;
	private JList<Note> noteList;
	private DefaultListModel<Note> listModel;
	private JButton previous;
	private JLabel description;
	private String sortOperation;
	private JButton addNote;
	private JButton deleteNote;
	private JButton uploadNote;
	private JComboBox<String> sortBox;
	private JTextArea noteTextArea;
	private DefaultListModel<String> dropBoxListModel;
	private JList<String> dropBoxNoteList;
	private JButton downloadNote;
	private JButton showDropBoxButton;
	private JButton deleteFileFromDropBox;
	
	public EventPage() throws DbxException {
		this.setLayout(new GridBagLayout());
		this.jMenu = new EventJMenu(this);
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT );
		menuPanel.setLayout(flow);
		menuPanel.add(jMenu);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 0, 0, 8, 1, 0, 0);
		this.add(menuPanel, c);
		
		this.previous = new PreviousButton();
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 0.05, 0, 1, 1, 0, 1);
		this.add(this.previous, c);
		
		this.description = new JLabel("", SwingConstants.CENTER);
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 0, 2, 1, 1, 1);
		this.add(this.description, c);
		
		this.displayPanel = new JPanel();
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 1, 3, 1, 0, 2);
		this.add(displayPanel, c);
		
		this.displayPanel.setLayout(new BorderLayout());
		JPanel notePanel = new JPanel(new GridLayout(2, 1));
		JPanel localNotePanel = new JPanel(new GridBagLayout());
		localNotePanel.setBackground(Color.white);
		localNotePanel.setOpaque(true);
		
		JLabel localNoteLabel = new JLabel("Local Notes:");
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 0, 1, 1, 0, 0);
		localNotePanel.add(localNoteLabel);
		
		this.listModel = new DefaultListModel<Note>();
		this.noteList = new JList<Note>(this.listModel);
		this.noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(this.noteList);
		scrollPane.setBorder(null);
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 1, 1, 1, 0, 1);
		localNotePanel.add(scrollPane, c);
		localNotePanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		notePanel.add(localNotePanel);
		
		JPanel dropBoxNotePanel = new JPanel(new GridBagLayout());
		dropBoxNotePanel.setBackground(Color.white);
		dropBoxNotePanel.setOpaque(true);
		
		JLabel dropBoxNoteLabel = new JLabel("DropBox Notes:");
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 0, 1, 1, 0, 0);
		dropBoxNotePanel.add(dropBoxNoteLabel);
		
		this.dropBoxListModel = new DefaultListModel<String>();
		this.dropBoxNoteList = new JList<String>(this.dropBoxListModel);
		this.dropBoxNoteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneDropBox = new JScrollPane(this.dropBoxNoteList);
		scrollPaneDropBox.setBorder(null);
		FrontEndUtilities.setGridBag(c, 1, 1, 1, 1, 0, 1);
		dropBoxNotePanel.add(scrollPaneDropBox, c);
		dropBoxNotePanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		notePanel.add(dropBoxNotePanel);
		this.displayPanel.add(notePanel, BorderLayout.LINE_START);
		
		this.noteTextArea = new JTextArea();
		noteTextArea.setEditable(false);
		JScrollPane noteDisplayScrollPane = new JScrollPane(noteTextArea);
		this.displayPanel.add(noteDisplayScrollPane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		buttonPanel.setLayout(new GridLayout(20 , 1));
		buttonPanel.setBackground(Color.white);
		this.displayPanel.add(buttonPanel, BorderLayout.LINE_END);
		
		this.addNote = new JButton("Add Note");
		buttonPanel.add(this.addNote);
		
		this.deleteNote = new JButton("Delete Note");
		this.deleteNote.setEnabled(false);
		buttonPanel.add(this.deleteNote);
		
		this.showDropBoxButton = new JButton("Show DropBox Notes");
		buttonPanel.add(this.showDropBoxButton);
		
		this.uploadNote = new JButton("Upload Note");
		this.uploadNote.setEnabled(false);
		buttonPanel.add(this.uploadNote);
		
		this.downloadNote = new JButton("Download Note");
		this.downloadNote.setEnabled(false);
		buttonPanel.add(this.downloadNote);
		
		this.deleteFileFromDropBox = new JButton("Delete File From DropBox");
		this.deleteFileFromDropBox.setEnabled(false);
		buttonPanel.add(this.deleteFileFromDropBox);
		
		String[] sortOptions = {"Oldest", "Newest"};
		
		this.sortBox = new JComboBox<String>(sortOptions);
		sortBox.setSelectedIndex(0);
		this.sortOperation = sortBox.getSelectedItem().toString();
		buttonPanel.add(this.sortBox);
		buttonPanel.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width * 2, buttonPanel.getPreferredSize().height));
		
		scrollPane.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width, scrollPane.getPreferredSize().height));
		scrollPaneDropBox.setPreferredSize(scrollPane.getPreferredSize());
	}
	
	public void setEvent(Event event) {
		this.event = event;
		this.description.setText(this.event.getName());
		EventPageController.updateLocalNoteListModel();
		this.noteTextArea.setText("");
		this.dropBoxListModel.clear();
		this.showDropBoxButton.setEnabled(true);
	}
	
	public JButton getPreviousButton() {
		return this.previous;
	}
	
	public JButton getAddNoteButton() {
		return this.addNote;
	}
	
	public JButton getDeleteNoteButton() {
		return this.deleteNote;
	}
	
	public JButton getUploadNoteButton() {
		return this.uploadNote;
	}
	
	public JButton getDownloadNoteButton() {
		return this.downloadNote;
	}
	
	public JComboBox<String> getSortBox(){
		return this.sortBox;
	}
	
	public void setSortOperation(String op) {
		this.sortOperation = op;
	}
	
	public String getSortOperation() {
		return this.sortOperation;
	}

	public Event getEvent() {
		return this.event;
	}

	public JList<Note> getLocalNoteList() {
		return this.noteList;
	}

	public DefaultListModel<Note> getLocalListModel() {
		return this.listModel;
	}
	
	public JList<String> getDropBoxNoteList() {
		return this.dropBoxNoteList;
	}

	public DefaultListModel<String> getDropBoxListModel() {
		return this.dropBoxListModel;
	}
	
	public EventJMenu getJMenuBar() {
		return this.jMenu;
	}
	
	public JTextArea getNoteDisplayTextArea() {
		return this.noteTextArea;
	}
	
	public JButton getShowDropBoxButton() {
		return this.showDropBoxButton;
	}
	
	public JButton getDeleteFromDropBoxButton() {
		return this.deleteFileFromDropBox;
	}
}

package frontend;

import java.awt.GridBagConstraints;
import java.io.File;

import calendar.Calendar;
import calendar.SerializerDeserializer;

public class FrontEndUtilities {

	public static void setGridBag(GridBagConstraints c, double weightx, double weighty, int width, int height, int gridx, int gridy) {
		c.weightx = weightx;
		c.weighty = weighty;
		c.gridwidth = width;
		c.gridheight = height;
		c.gridx = gridx;
		c.gridy = gridy;
	}
	
	public static void serializeCalendar(Calendar calendar) {
		String cwd = System.getProperty("user.dir") + File.separator + "calendar.ser";
		SerializerDeserializer.serializeCalendar(calendar, cwd);
	}

}

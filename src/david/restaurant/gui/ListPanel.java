package david.restaurant.gui;

import david.restaurant.CustomerAgent;
import david.restaurant.HostAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JTextField textField = new JTextField(20);

    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        this.setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        textField.addActionListener(this);
        textField.setMaximumSize(textField.getPreferredSize());
        add(textField);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == textField) {
        	if(textField.getText().isEmpty() == false)
        	{
        		addPerson(textField.getText());
        		textField.setText("");
        	}
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
        	
        	JPanel panel = new JPanel();
        	
        	Dimension paneSize = pane.getSize();
	    	Dimension panelSize = new Dimension(paneSize.width - 20,
	                 (int) (paneSize.height / 7));
	        panel.setPreferredSize(panelSize);
	        panel.setMinimumSize(panelSize);
	        panel.setMaximumSize(panelSize);
        	
        	panel.setLayout(new BorderLayout());
        	
            JButton button = new JButton(name);
            button.setBackground(Color.white);
            button.addActionListener(this);
            panel.add(button, BorderLayout.CENTER);
            
            JCheckBox checkbox = new JCheckBox("",false);
            checkbox.setName(type + name);
            panel.add(checkbox, BorderLayout.EAST);
                      
            list.add(button);
            view.add(panel);
            restPanel.addPersonToList(type, name, checkbox);//puts customer on list
            validate();
        }
    }
}

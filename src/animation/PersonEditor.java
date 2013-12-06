package animation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import agents.Person;
import simcity201.gui.GlobalMap;

public class PersonEditor extends JPanel implements ActionListener, ItemListener{

	private JComboBox<Person> persons = new JComboBox<Person>();
	
	Person selectedPerson = null;
	
	ControlPanel control;
	
	int previousCount;
	
	public PersonEditor(ControlPanel control)
	{
		this.control = control;
		
		setLayout(new BorderLayout());
        
        Dimension addDim = new Dimension(200, 30);
        persons.setPreferredSize(addDim);
        persons.setMinimumSize(addDim);
        persons.setMaximumSize(addDim);
        
        this.add(persons);
		
		for(Person p: GlobalMap.getGlobalMap().getListOfPeople())
		{
			persons.addItem(p);
		}
		
		persons.setVisible(true);
		previousCount = persons.getItemCount();
		persons.addItemListener(this);
	}
	
	public void addPerson(Person p)
	{
		persons.addItem(p);
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

	public void itemStateChanged(ItemEvent arg0) {
		if(arg0.getSource() == persons)
		{
			control.showInfo((Person)persons.getSelectedItem());
		}
	}
}

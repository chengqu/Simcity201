package animation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import agents.Person;
import simcity201.gui.GlobalMap;

public class PersonEditor extends JPanel implements ActionListener{

	private JComboBox<Person> persons = new JComboBox<Person>();
	
	Person selectedPerson = null;
	
	ControlPanel control;
	
	public PersonEditor(ControlPanel control)
	{
		this.control = control;
		
		setLayout(new BorderLayout());
        
        Dimension addDim = new Dimension(200, 30);
        persons.setPreferredSize(addDim);
        persons.setMinimumSize(addDim);
        persons.setMaximumSize(addDim);
        
        persons.addActionListener(this);
        
        this.add(persons);
		
		for(Person p: GlobalMap.getGlobalMap().getListOfPeople())
		{
			persons.addItem(p);
		}
		
		persons.setVisible(true);
	}
	
	public void addPerson(Person p)
	{
		persons.addItem(p);
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == persons)
		{
			if(selectedPerson == null)
			{
				if(persons.getItemCount() > 0)
				{
					selectedPerson = (Person)persons.getSelectedItem();
					control.showInfo(selectedPerson);
					this.repaint();
					return;
				}
			}
			else if(selectedPerson != (Person)persons.getSelectedItem())
			{
				System.out.println("SWITCHEDHEHDH");
				selectedPerson = (Person)persons.getSelectedItem();
				control.showInfo(selectedPerson);
				this.repaint();
				return;
			}
		}
	}
}

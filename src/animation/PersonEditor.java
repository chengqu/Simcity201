package animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.RoleStatus;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agents.Person;
import agents.Role;
import simcity201.gui.GlobalMap;

public class PersonEditor extends JPanel implements ActionListener{

	private JComboBox<Person> persons = new JComboBox<Person>();
	private List<JCheckBox> BehaviorCheckboxes = new ArrayList<JCheckBox>();
	private List<JButton> BehaviorButtons = new ArrayList<JButton>();
	
	private List<JButton> dataButtons = new ArrayList<JButton>();
	private List<JTextField> dataFields = new ArrayList<JTextField>();
	
	private List<JButton> roleButtons = new ArrayList<JButton>();
	private List<JCheckBox> roleBoxes = new ArrayList<JCheckBox>();
	
	JPanel listPanels;
	private JButton selectPerson;
	
	private GenericListPanel behaviorEditor;
	private GenericListPanel personDataEditor;
	private GenericListPanel roleEditor;
	
	Person selectedPerson = null;
	
	ControlPanel control;
	
	public PersonEditor(ControlPanel control)
	{
		selectPerson = new JButton();
		selectPerson.setText("Select");
		this.control = control;
		listPanels = new JPanel();
		listPanels.setLayout(new GridLayout(1, 0));
		
		JPanel temp = new JPanel();
		
		setLayout(new BorderLayout());
        
        Dimension addDim = new Dimension(200, 30);
        persons.setPreferredSize(addDim);
        persons.setMinimumSize(addDim);
        persons.setMaximumSize(addDim);
        
        temp.add(selectPerson);
        temp.add(persons);
		
        this.add(temp, BorderLayout.NORTH);
        
        behaviorEditor = new GenericListPanel();
        roleEditor = new GenericListPanel();
        personDataEditor = new GenericListPanel();
        
        listPanels.add(behaviorEditor);
        listPanels.add(personDataEditor);
        listPanels.add(roleEditor);
        
        this.add(listPanels, BorderLayout.CENTER);
        
		for(Person p: GlobalMap.getGlobalMap().getListOfPeople())
		{
			persons.addItem(p);
		}
		
		persons.setVisible(true);
		selectPerson.setVisible(true);
		selectPerson.addActionListener(this);
	}
	
	public void addPerson(Person p)
	{
		persons.addItem(p);
		this.repaint();
	}
	
	public void updatePerson(Person p)
	{
		if(persons.getSelectedItem() != null && p.equals((Person)persons.getSelectedItem()))
		{
			for(JCheckBox box : BehaviorCheckboxes)
			{
				if(box.getName().equalsIgnoreCase("Deposit Groceries"))
				{
					box.setSelected(p.depositGroceries);
				}
				if(box.getName().equalsIgnoreCase("Create Account"))
				{
					box.setSelected(p.createAccount);
				}
				if(box.getName().equalsIgnoreCase("Get Money"))
				{
					box.setSelected(p.getMoneyFromBank);
				}
				if(box.getName().equalsIgnoreCase("Deposit Money"))
				{
					box.setSelected(p.depositMoney);
				}
				if(box.getName().equalsIgnoreCase("Buy Groceries"))
				{
					box.setSelected(p.buyGroceries);
				}
				if(box.getName().equalsIgnoreCase("Eat food"))
				{
					box.setSelected(p.eatFood);
				}
				if(box.getName().equalsIgnoreCase("Pay bills"))
				{
					box.setSelected(p.payBills);
				}
				if(box.getName().equalsIgnoreCase("Go to sleep"))
				{
					box.setSelected(p.goToSleep);
				}
			}

			/*
			 * "Money"
			 * "Paycheck"
			 * "HungerLevel"
			 * "HungerThreshold"
			 */
			for(int i = 0; i < dataButtons.size(); i++)
			{
				if(dataFields.get(i).getName().equalsIgnoreCase("Money"))
				{
					dataButtons.get(i).setText(Float.toString(p.money));
				}
				if(dataFields.get(i).getName().equalsIgnoreCase("Paycheck"))
				{
					dataButtons.get(i).setText(Float.toString(p.payCheck));
				}
				if(dataFields.get(i).getName().equalsIgnoreCase("HungerLevel"))
				{
					dataButtons.get(i).setText(Integer.toString(p.hungerLevel));
				}
				if(dataFields.get(i).getName().equalsIgnoreCase("HungerThreshold"))
				{	
					dataButtons.get(i).setText(Integer.toString(p.hungerThreshold));
				}
			}
		}
	}
	
	public boolean show = true;
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == selectPerson)
		{
			if(persons.getSelectedItem() != null)
			{
				//setup behavior editor
				dataFields.clear();
				dataButtons.clear();
				BehaviorCheckboxes.clear();
				BehaviorButtons.clear();
				behaviorEditor.clearPane();
				personDataEditor.clearPane();
				roleEditor.clearPane();
				
				List<JComponent> components;
				JCheckBox box;
				JButton button;
				Person p = (Person)persons.getSelectedItem();
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setName("Deposit Groceries");
				box.setSelected(p.depositGroceries);
				button = new JButton();
				button.setText("Deposit Groceries");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setName("Create Account");
				box.setSelected(p.createAccount);
				button = new JButton();
				button.setText("Create Account");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.getMoneyFromBank);
				button = new JButton();
				box.setName("Get Money");
				button.setText("Get Money");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.depositMoney);
				button = new JButton();
				box.setName("Deposit Money");
				button.setText("Deposit Money");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.buyGroceries);
				button = new JButton();
				box.setName("Buy Groceries");
				button.setText("Buy Groceries");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.eatFood);
				button = new JButton();
				box.setName("Eat food");
				button.setText("Eat food");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.payBills);
				button = new JButton();
				box.setName("Pay bills");
				button.setText("Pay bills");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				box = new JCheckBox();
				box.setSelected(p.goToSleep);
				button = new JButton();
				box.setName("Go to sleep");
				button.setText("Go to sleep");
				button.setBackground(Color.white);
				BehaviorCheckboxes.add(box);
				BehaviorButtons.add(button);
				box.addActionListener(this);
				button.addActionListener(this);
				
				components.add(box);
				components.add(button);
				box.setVisible(true);
				button.setVisible(true);
				
				behaviorEditor.addParams(components);
				
				
				//setup personDataEditor
				JTextField editor;
				
				/*
				 * "Money"
				 * "Paycheck"
				 * "HungerLevel"
				 * "HungerThreshold"
				 */
				
				JPanel label;
				
				//current money editor
				components = new ArrayList<JComponent>();
		        label = new JPanel();
		        label.add(new JLabel("<html><pre>" + "Money" + "</pre></html>"));
		        components.add(label);
		        personDataEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				button = new JButton();
				editor = new JTextField();
				Dimension editorDimension = new Dimension(60, 20);
				
				editor.setPreferredSize(editorDimension);
				editor.setMaximumSize(editorDimension);
				editor.setMinimumSize(editorDimension);
				editor.setName("Money");
				editor.addActionListener(this);
				button.setText(Float.toString(p.money));
				components.add(editor);
				components.add(button);
				dataButtons.add(button);
				dataFields.add(editor);
				personDataEditor.addParams(components);
				
				//paycheck money editor
				components = new ArrayList<JComponent>();
		        label = new JPanel();
		        label.add(new JLabel("<html><pre>" + "Paycheck" + "</pre></html>"));
		        components.add(label);
		        personDataEditor.addParams(components);
				
				components = new ArrayList<JComponent>();
				button = new JButton();
				editor = new JTextField();
				
				editor.setPreferredSize(editorDimension);
				editor.setMaximumSize(editorDimension);
				editor.setMinimumSize(editorDimension);
				editor.setName("Paycheck");
				editor.addActionListener(this);
				button.setText(Float.toString(p.payCheck));
				components.add(editor);
				components.add(button);
				dataButtons.add(button);
				dataFields.add(editor);
				personDataEditor.addParams(components);
				
				//hungerlevel editor
				components = new ArrayList<JComponent>();
		        label = new JPanel();
		        label.add(new JLabel("<html><pre>" + "Hungerlevel" + "</pre></html>"));
		        components.add(label);
		        personDataEditor.addParams(components);
		        
				components = new ArrayList<JComponent>();
				button = new JButton();
				editor = new JTextField();
				
				editor.setPreferredSize(editorDimension);
				editor.setMaximumSize(editorDimension);
				editor.setMinimumSize(editorDimension);
				editor.setName("HungerLevel");
				editor.addActionListener(this);
				button.setText(Integer.toString(p.hungerLevel));
				components.add(editor);
				components.add(button);
				dataButtons.add(button);
				dataFields.add(editor);
				personDataEditor.addParams(components);
				
				//hunger threshold editor
				components = new ArrayList<JComponent>();
		        label = new JPanel();
		        label.add(new JLabel("<html><pre>" + "HungerThreshold" + "</pre></html>"));
		        components.add(label);
		        personDataEditor.addParams(components);
		        
				components = new ArrayList<JComponent>();
				button = new JButton();
				editor = new JTextField();
				
				editor.setPreferredSize(editorDimension);
				editor.setMaximumSize(editorDimension);
				editor.setMinimumSize(editorDimension);
				editor.setName("HungerThreshold");
				editor.addActionListener(this);
				button.setText(Integer.toString(p.hungerThreshold));
				components.add(editor);
				components.add(button);
				dataButtons.add(button);
				dataFields.add(editor);
				personDataEditor.addParams(components);
				
				//role editor
				for(Role.roles r : Role.roles.values())
				{
					if(r.toString().contains("Worker") || r.toString().contains("Renter") 
							|| r.toString().contains("Owner"))
					{
						continue;
					}
					else
					{
						/*List<JComponent> components;
						JCheckBox box;
						JButton button;*/
						components = new ArrayList<JComponent>();
						button = new JButton();
						box = new JCheckBox();
						box.setName("ROLE" + r.toString());
						button.setText("ROLE" + r.toString());
						box.addActionListener(this);
						box.setSelected(p.roles.contains(r));
						components.add(box);
						components.add(button);
						roleButtons.add(button);
						roleBoxes.add(box);
						roleEditor.addParams(components);
					}
				}
			}
		}
		else if(arg0.getSource().getClass() == JCheckBox.class)
		{
			JCheckBox temp = (JCheckBox)arg0.getSource();
			Person p = (Person)persons.getSelectedItem();
			synchronized(p.commandLock)
			{
				if(temp.getName().equalsIgnoreCase("Deposit Groceries"))
				{
					p.depositGroceries = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Create Account"))
				{
					p.createAccount = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Get Money"))
				{
					p.getMoneyFromBank = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Deposit Money"))
				{
					p.depositMoney = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Buy Groceries"))
				{
					p.buyGroceries = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Eat food"))
				{
					p.eatFood = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Pay bills"))
				{
					p.payBills = temp.isSelected();
				}
				else if(temp.getName().equalsIgnoreCase("Go to sleep"))
				{
					p.goToSleep = temp.isSelected();
				}
				else if(temp.getName().contains("ROLE"))
				{
					boolean found = false;
					if(temp.isSelected() == true)
					{
						for(Role r : p.roles)
						{
							if(temp.getName().contains(r.toString()))
							{
								found = true;
								break;
							}
						}
						if(!found)
						{
							for(Role.roles r : Role.roles.values())
							{
								if(temp.getName().contains(r.toString()))
								{
									p.roles.add(new Role(r, null));
									break;
								}
							}
						}
					}
					else
					{
						for(Role r : p.roles)
						{
							if(temp.getName().contains(r.toString()))
							{
								p.roles.remove(r);
								break;
							}
						}
					}
				}
			}
		}
		else if(arg0.getSource().getClass() == JTextField.class)
		{
			/*
			 * "Money"
			 * "Paycheck"
			 * "HungerLevel"
			 * "HungerThreshold"
			 */
			JTextField temp = (JTextField)arg0.getSource();
			JButton tempB = dataButtons.get(dataFields.indexOf(temp));
			Person p = (Person)persons.getSelectedItem();
			synchronized(p.commandLock)
			{
				if(temp.getName().equalsIgnoreCase("Money"))
				{
					try{
						float m = Float.parseFloat(temp.getText());
						p.money = m;
						tempB.setText(Float.toString(m));
					}
					catch(NumberFormatException e){
						temp.setText("Not a valid number, try again");
					}
				}
				else if(temp.getName().equalsIgnoreCase("Paycheck"))
				{
					try{
						float m = Float.parseFloat(temp.getText());
						p.payCheck = m;
						tempB.setText(Float.toString(m));
					}
					catch(NumberFormatException e){
						temp.setText("Not a valid number, try again");
					}
				}
				else if(temp.getName().equalsIgnoreCase("HungerLevel"))
				{
					try{
						int m = Integer.parseInt(temp.getText());
						p.hungerLevel = m;
						tempB.setText(Integer.toString(m));
					}
					catch(NumberFormatException e){
						temp.setText("Not a valid number, try again");
					}
				}
				else if(temp.getName().equalsIgnoreCase("HungerThreshold"))
				{
					try{
						int m = Integer.parseInt(temp.getText());
						p.hungerThreshold = m;
						tempB.setText(Integer.toString(m));
					}
					catch(NumberFormatException e){
						temp.setText("Not a valid number, try again");
					}
				}
			}
		}
	}
}

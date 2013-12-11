team10
======


##Directions to Run System:

	-At the start of the program, it allows you to choose a configuration file from a dropdown box. Currently, there 
	is a "config1" and "config2". There is no difference between the two.
	-The user can select whether or not to play music during our program from a dropdown box beneath the confic 
	dropdown box.. The mu
	
	-Added functionality in the control panel now allows the user to modify specific elements of a selected person to control the person's actions. 
	
		-To do so, select a person from the drop down box and then click the "select" button. A menu will appear 
		in the bottom left full of checkboxes that can tell the person do to actions such as get groceries, open 
		account, eat food.
		-You can set different member variables of Person such as money in the middle bottom section.
		-You can set different roles for the person on the bottom right.
		
	-There is a log filter to filter house the print statements of a selected person. 
		-To use the log filter, select a type of person in the dropdown box beneath the "add" button. Once 
		clicked, the log filter will begin to show the print statements on the right. Wtih this, the user 
		can follow the actions of a specific type of person.
--------------------------------------------------------------------------------------------------------------------------
	-There is currently no format validation for adding in only numbers to the control panel textfields, so entering non-digits will result in errors.

	-If you add a person with a Hunger Level greater than 20, they will be hungry and want to go a restaurant.

	-The vehicle preference drop-down list determines how the Person will travel to different building.
	
	-We have hard-coded a Bank Teller, an Apartment Owner, and workers in all our restaurants to show that our buildings are functional
	
	-Our Decide function in PersonAgent determines the actions that the person will perform. The Hierarchy 
	of this function is as follows:
		
		-Deposit Groceries (If Person has groceries)
		
		-Make Bank Account (If Person does not have a bank account)
		
		-Desosit Money (If Pay Check is above pay check threshold ($100))
		
		-Get Money From Bank (If Person's cash is below low cash threshold ($20))
		
		-Buy Groceries (If has a home and his fridge contains no groceries)
		
		-Eat Food (If Hunger Level > 20)
		
		-Pay Bills (If Bills list is not empty)
		
		-Go Sleep (Default)
		
## Issues
	-We weren't able to create configuration files to run specific scenarios right away. However, with the 
	functionality of the control panel, we should be able to run any type of scenario
	
	-Working isn't completely functional. People can start and end work but workers are unable to shift jobs.
	
##Contributed Work
###All Helped on:
               -Implementation of the Decide function within the Person Agent
               
               -Design and Implementation of the Market Store
               
####Gueho Choi:
--------------------------------------------------
		-Managed room reservation and team meeting time most of times.
               -Switching AnimationPanel using BaseAnimationalPanel on separate window.
               -Design, Implementation, and unit testing of Bank using line as a shared data as well as gui implementation.
               	-It should be able to work with non-normative too since they are all desgined already, but they are removed for now since person is not fully updated yet.
               	-Although ATM, Security, and Robbery are designed, they are removed and not yet implemented since we decided not to support it by v1. 
               	-Unit testing on normative scenarios on the both customer and teller.
               	-The Teller and Customer are functioning completely without non-norms.
               	-gui is not yet fully supported, but visible what it's doing. It may be out of control if too many customers come in.
               	-working function is not implemented as well for v1.
               -Initial GlobalMap Design with functionality to add buildings.
               -Initial design of person interaction using black-box approach with help from David.
               	-Entering and leaving a building method idea rather than proposed role idea in class. Role is different from the proposed role in class. The role is just what he would do, not different persona. Different persona is simulated using decision logic and self-reflection construction of person in each building.
               	-Helping David for decision logic.
               -Helped people general implementation issues, bugs, and ideas.
               -Implemented basics for configuration file.
               -Redesign-Reimplementation of restaurant cook - new market implementation with help from team members.
               	-re-implemented v2.2 individual restaurant in order to interact with new market.
               -Individual restaurants do not support new requirements except ordering from new market.
------------------------
####Eric Liu: 
	       -Implemented A Star in transportation for people walking. People will not J-walk or walk through buildings.
	       They will calculate the shortest path using my "findPath" function in "walkingAStar" class to locate with 
	       tiles on the map to go to. 
	 	
	       -Implemented the traffic light system in transportation so that walkers will not cross the intersection 
	       unless the lights are green. The lights will change every 10 seconds, but there is a slight delay in 
	       sending this information back to the worker. This delay causes some people to rush across the street even 
	       though it is red. It sort of imitates real life.
	 	
	       -Implemented Producer-Consumer code in my waiter agent and cook to create a class 'WaiterProducer" that 
	       uses producer consumer code to send orders to the cook.
	       
	       -Added unit tests to my producer consumer agents to ensure that they work. You can also check its 
	       functionality in the map when people go into "Rest4"
	 
               -Control Panel and Person Panel for adding new people into the Sim City. Users can choose custom data                     
               members to add to the person to influence the Person's decide function. Person's info shows upon clicking                
               the person's button.
     
               -Coded the ApartmentGui. Added in an apartment layout and coded the ApartmentPersonAgent to go to fridge to                  
               store and retrieve groceries, go to stove to cook food, go to table to eat food, and go to living room                  
               when doing nothing. Coded part of the ApartmentPersonAgent agent code to be functional with the gui.
               
               -Designed the design document for the Car in transportation.
               
               -Unit Tested the newMarket agents to ensure that they work and fixed bugs in the newMarket found when unit                  testing. Created eight tests for the Market to test all normative scenarios.
               
               -Integrated the newMarket into own restaurant to make sure that the newMarket is used for purchases.
               
               -Added in GUI images for buildings in Sim City main map to improve user interface.

####David Ivan:
		V2 stuff
		-Created the producer consumer code that everybody then implemented
		 and helped everybody integrate it into their restaurant
		-Updated the control to be able to manipulate people and different properties about them
		-Helped implement working in people's restaurants and the bank and helped implement it in
		 my restaurant
		-implemented the job board so people can get jobs and when they quit, other people can
		 consume those jobs
		-Helped create and implement traffic light agent subscription model
		-added control panels to people's restaurants so we can manipulate data within them
			-however, some people chose to remove theirs, since they didn't like them since it made the animation
			 look weird
		-Helped with MANY bug fixes and general implementation details that people needed help with.
		-Wrote unit tests for my producer consumer code, its called "ProducerTest" its in david.restaurant.test
		-Finally fixed the apartment
		-Updated person to be modular and make better decisions to avoid logical loops
		 	-also removed many of the little bugs that made him go to the wrong place
	-Designed the structure of person with help from Ryan
		-Helped come up with the scheduler, the enter function, and the decision function. Helped come up with idea
		 for having the person calls certain agents and buildings based on his internal roles and flags, rather than owning
		 the roles agents directly. 
		-Implemented almost all of the functions and have maintained and updated it. 
		-Unfortunately i haven't been able to test it, but there isn't much testing that needs to be done for it
	-Designed the apartments
		-Designed the ApartmentPerson agent, which completely works
		-Made apartment complex and apartment animation panel, which allows users to "flip" through apartments based on how
		 many people are in the apartment.
		-Mostly implemented it, however much of the gui stuff was done by eric
		-added some extra functionality, such as getting evicted from the apartmentcomplex if they are a renter and haven't
		 paid all their bills
		-all scenarios tested and working
	-Made the initial design of the house
		-It was redesigned by Lin though. no implementation or testing on my part
	-Helped people with general implementation and bug fixes
		-helped people use some of the backend systems that ryan and i came up with to implement other peoples functionality,
			-ex: helping qu get the trucks going, by showing him how to use the global map to pull the restaurants and get the cooks
	

####Josh Faskowitz:

    -Designed and implemented the OLD MarketManagerAgent, MarketCustomerAgent, and MarketEmplyoeeAgent. 
    -OLD Market Agents to fit within the Market, used to interact with Person
    -Make GUI for the OLD market
    -Make GUI for the NEW market and all of the agents inside of the Market
    -Became steward for the newMarket
    --Reverse documented the newMarket design and added annotations to the design and uploaded a new design document
    --make design changes were necessry to improve function and usability of the newMarket
    --modify the market to use an inventory system
    --work with Cheng to get the delivery to the market's via the truck
    --work with Cheng to get Customer Agents to get a car on the outside
    --debug the issues associated with the newMarket and in general fix and modify it
    -debugging Person (with David) decide agent to make sure that person is coming to market to propery buy a car or to buy groceries
    --this behavior is still not perfect but we rid for the most part repeptive behaviors 
    --implemented some hacks in cases were there is still a bug, in order to better insure usability
    ---for example, if there person shows up with 0 dollars to their name, we let them order still...but still people who do not have enough money will leave
    -With help from David made the Market interaction interface to establish interface system between all the different restaurants and the market
    -With help from Ryan was able to upload personal restaurant with sim city and make sure it could appear in the main gui. 
    -With help from David implemented personal producer consumer
    -adapted newMarket tests for changes made from v1 to v2
    -helped to get room at Founders reserved, commonly brought food for everybody

####Cheng Qu: 
		-Designed and impelemented BusAgent,CarAgent, StopAgent,TruckAgent,  PassengerAgent, BusGui, TruckGui, CarGui,PassengerGui. Basically every moving thing outside the building. Let the bus and truck only goes on right, which is legal. everything is working
               -Designed and implemented layout of buildings in the world.
               -Tested All transportation agents to work
               -designed and implemented MarketDealerAgent but doesn't work.
               -integrated own restaurant to work with the person and market.
	-implemented Astar on Car, let the car seldom bump up each other
	-designed and implemented the car crashes people
	-producer consumer for cook and waiter interaction
	-implemented second bus
	-basically designed and implemented all the transportation system.


####Yunan Lin:  
		-Draw the entire World Map for simcity, including the roads, MouseOver Effect, and Night Animation every several minutes; 
		
		-added the animationPanel for each building when clicked; 
		
		-In charge or the House stuff, can add Person into the house, and updated the Person's condition after finishing task in the house; 
		
		-The house can do : Sleep at home, Eat at home, Store Groceries at home, Pay bills at home if he is a Renter, and also, a Person can do multiple task in the house. 
		
		-Basically, I design the house, implemented it, including the guis(take a look at the TV Animation Stuff!!!), and Unit Test it for three scenarios(Eat at home, sleep at home, store groceries at home);Did the integration of restaurant and person, restaurant and Store interaction.
               
                 



## v2 Scrern Shot of Map and Control Panel
<img src="http://i195.photobucket.com/albums/z67/hooploopz/simCityMap.png" </img>
<img src="http://i195.photobucket.com/albums/z67/hooploopz/controlPanel.png"</img>
## v1 Screen Shot of Overall Map
<img src="http://i195.photobucket.com/albums/z67/hooploopz/simCity_screenshot.png" </img>

## picutes of TEAM VEGUS!!!
![food](http://i.imgur.com/jKnOX14.jpg)

![teamwork](http://i.imgur.com/YhUxczm)

![coffee](http://i.imgur.com/s2BrONo)


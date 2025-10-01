Q1: Specifications for Health-dev framework:

- **Data collection and sensor**:
    - Describe on what all sensers to use ( camera, mic, motion sensor, vibration etc ).
    - Describe on how to use these sensors. Ex - Use mic for recording heart beat sound, use camera for recording the blood color
- **User screen** 
  - Explain on what all features should a user see on it's device 
  - Explain on how these features will be used, what buttons will do what kind of functioning when clicked. Explain what kind of output should be shown to user after processing is done
- **Hardware to use for processing/storage:** 
  -  Describe what hardware devices are available and can be used for each specific type of processing. 
  -  Describe all types of storage devices available (Hard disc, memory card, etc) which can be used to store the data.
  -  **Data and processing flow**. Describe what the expected flow is. For example -> First take input, clean data, pre-process, do the expected computation and then show it to user output screen.
-  **Describe the type of user:** This part is very important for the development of the mobile application. We should hjave details on what are the users for the app we are targetting. Applicaiton can be varied on the basis of the user. For example - for a non technical user we should keep application very simple, clean and easy to use with more convenient user interface.

Q2: Using bHealthy

-  Build new app: 
  -  Use data from bHealthy 
  - Combine it with data stored in database in project 1. Find the regular pattern like monthly headaches, fever during specific weather time and temperature. Send alerts to user to take precautions
-  Understand the regular health issues of the user. Over the time use all the data from different sources and observe user symptoms. Use this data to predict the user's upcoming health issues
-  We store use past medical details in the SQL databse.We can enhance this to link to other user medical data like user medical history, alergies, medicines used etc. We can combine data from multiple different sources and types and aggregate them to find the more accurate user medical analysis. This will help us to give better feedback to the user and use this to further improve the medical health monitoring for the specific user.
-  Receive user feedbacks and improve apps: get user specific feedback on their thoughts on the application. Understand if they think that the output of the application matches with their expectations. If not, we can get more detailed user inputs like what is the expected outcome in the scenarios where they feel app needs i provements.Use that feedback to improve the  app functioning.

Q3: Yes. My thoughts about this have changed. Before i used to think that mobile computing is only about developing mobile apps but now I have a wider context around the same.

Mobile computing can include following aspects as well:

-  **Surrounding awareness:** System should be aware of the data around the surroundings it is present in. For example - It should know the temperature it is in, the speed at which it is moving etc
-  **Context Understanding:** the system should be aware of the context around which it is performing. For example - if we are gathering the data of user health, the app should know what are we collecting and why are we collecting. Using this information app should adjust it's according to the situations and perform better.
-  **Hardware used:** We are using local server for storing the data locally. bHealthy used Health-Dev for connecting to sensors and phones. There can be multiple options on using the hardware and cloud resources. We should understand the different possibilities and use the one that best fits our requirements and use case.
-  **Feedback and improvement:** The mobile should also be able to gather feedback and enhance its behaviour to best fit the user.

In summary, Mobile computing is is not just about app development but alot more including surrounding awareness, hardware usage, context understanding, sensors, feedback and improvements etc.
### Q1: Specifications for Health-dev framework:

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
-  **Describe the type of user:** This part is very important for the development of the mobile application. We should hjave details on what are the users for the app we are targeting. Application can be varied on the basis of the user. For example - for a non technical user we should keep application very simple, clean and easy to use with more convenient user interface.
- **Constraints**: 
  - Share details like less usage of battery. Some devices can not handle high requirement of some heavy games. This can eventually result in degradation of battery health with time if not set up properly.
  - Some mobile devices can communicate between a specific wavelength and frequency. it is very important to define the wavelength range so that networking can be done properly in the system.
  - **Target platforms**
    -Operating system targeted: Some apps can only run on a specific operating system. The app should be designed according to the targeted operating system
  - In case of multi platform application, specific type of tech stacks should be used while keeping in mind that configuration is set for all the type of mobiles and operating system.
### Q2: Using bHealthy
- Use data from bHealthy and combine it with data stored in database in project 1. Find the regular pattern like monthly headaches, fever during specific weather time and temperature. Send alerts to user to take precautions
-  Understand the regular health issues of the user. Over the time use all the data from different sources and observe user symptoms. Use this data to predict the user's upcoming health issues
-  We store use past medical details in the SQL database.We can enhance this to link to other user medical data like user medical history, alergies, medicines used etc. We can combine data from multiple different sources and types and aggregate them to find the more accurate user medical analysis. This will help us to give better feedback to the user and use this to further improve the medical health monitoring for the specific user.
-  Receive user feedbacks and improve apps: get user specific feedback on their thoughts on the application. Understand if they think that the output of the application matches with their expectations. If not, we can get more detailed user inputs like what is the expected outcome in the scenarios where they feel app needs i provements.Use that feedback to improve the  app functioning.
- **Suggestions**
  - Suggest other wellness apps like (ex. if an user has regular anxiety and fever issues. They should be suggested other wellness apps like meditation and relaxation. Moreover they can be recomended stress relieving exercise applications).
  - Show user their wellness score on the basis of the data like symptoms and health status. 
  - Visual feedback: Red light on screen in case we se something unexpected health status or green light on screen in case of good health status
- **Develop context aware tracker**:
  - Use logic to evaluate output on the basis of other data like evulating heart rate considering thef ever, headache, hair fall, fatigue et in consideration.
- **Automatic feedback gathering**
  - logic like less time used this app means user doesn't like the app. If user keeps using the app more this means that app shows correct data and user feedback is good. 
-  It is important to move from manual feedback to smart context aware feedback.

### Q3: Yes. My thoughts about this have changed. Before i used to think that mobile computing is only about developing mobile apps but now I have a wider context around the same.

Mobile computing can include following aspects as well:

-  **Surrounding awareness:** System should be aware of the data around the surroundings it is present in. For example - It should know the temperature it is in, the speed at which it is moving etc
-  **Context Understanding:** the system should be aware of the context around which it is performing. For example - if we are gathering the data of user health, the app should know what are we collecting and why are we collecting. Using this information app should adjust it's according to the situations and perform better.
-  **Hardware used:** We are using local server for storing the data locally. bHealthy used Health-Dev for connecting to sensors and phones. There can be multiple options on using the hardware and cloud resources. We should understand the different possibilities and use the one that best fits our requirements and use case.
-  **Feedback and improvement:** The mobile should also be able to gather feedback and enhance its behaviour to best fit the user.
- Project 1 showed the importance of not only the frontont of the application but also showed how the frontend interacts with the backend. how the backend interacts with the databse and store/fetch the data. Furthermore we also saw how the files are uploaded and processed in the backend. 
We also used multiple tech stacks like SQL, kotlin, XML etc. We have used multiple different type of software technologies which were combined with hardware tools like camera and flashlight. 
We have seen the blend of all the technologies which combine togethet to make an amazing working application. This shows  that the mobile application is not just an algorithms but combination of multiple aspects.
- **Examples are as follow**
  - Fitbit: This combines smart hardware sensors, smart processing and centralised data storage
  - Pokemon mobile game - This uses user input, mobile movement, location, camera and other sensors. This is perfect example of smart application under mobile computing
  - Battleground OPubg game - This is a smart game which makes use of touch sensors and gyroscope. For such appss, mobile movement and sensors are important and used widely. This is used to provide smarter user experience. This makes use of more precise movement sensors for enhancing the user experience.
In summary, Mobile computing is is not just about app development but alot more including surrounding awareness, hardware usage, context understanding, sensors, feedback and improvements etc.
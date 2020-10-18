# Functionality
This document record the functionality design process when developing the Quinzical game.
- [Quinzical introduction](#quinzical-introduction)
- [Database](#database)
- [Load Question](#load-question)
- [GUI](#gui)
- [Text to Speech](#text-to-speech)
- [Playback and Manipulation Audio](#playback-and-manipulation-audio)
- [Project Structure](#project-structure)
- [Decisions Down the Line](#decisions-down-the-line)


## Quinzical introduction
Quinzical gives users a deeper understanding of New Zealand through a quiz format game. The game
allows users to select different categories of questions to answer. There are two modules for user
to play:

### The Practice Module
In the practice module, There are all categories from the question set will be displayed on the board.
Users can select any category from the New Zealand Question Set. A randomly selected clue will provide the user with both the text and the TTS read version. The user is told whether they are right or wrong after the answer is typed in. The user is allowed three attempts. In the third attempt, they are given the first letter of the answer as a hint. If they are wrong after the third attempt, the answer is written on the screen along with the original clue.

### The Games Module
The interface will display 5 randomly selected categories and 5 randomly selected clues from each category on the board in a grid format in the game module. Users can select the clue to answer the question. The clue
is read out by the TTS system. Users can only choose the lowest money value for any category. After the user types in the correct answer, their worth increases accordingly. If the user does not type in the correct answer or clicks the “Don’t know” the clue disappears off the grid, but their worth does not increase. Each clue can only
be answered once. Once asked, regardless of whether the answer right or wrong, the clue is removed from the grid.
Once all the clues are gone, the user is treated to a reward screen, and the user has the option to play again. 
The score is reset to zero.

## Database
Question source is from [here](https://docs.google.com/document/d/18TYDYfCBuwa5r1V9EMVGWLjJgfCSVY_UPbPtNPCPnCw/edit?usp=sharing)
Use a txt file to store all questions of each category and place all txt files under the category folder. 
Set the category as the name of the file. Each question occupies one row in the txt file with the format.
"CLUE...,(what is/Who is) ANSWER".

## Load Question
Use java File class to read all the files under the category folder and save them into a File[] array. Create a method to
generate 5 random numbers from the required range and store them into an array. We can use these 5 numbers to select the element from File[] array. We can achieve the requirement of display 5 randomly chosen categories. Similar to the clues, we use the 5 random numbers to represent the line number in a txt file. When we read the content of a category file, we only read the appointed 5 lines in which the line numbers are matched with the 5 random number. We can achieve the requirement of randomly selected 5 clues form a category.

## GUI
We are using Java FXML to create GUI that can reduce the design time and is very easy to modify. We make the user interface in SceneBuilder based on the design draft. Different packages are used to store the GUIs and controllers of the game module and practice module. It is easy to debug the functionality of each element in each interface without confusing it.

## Archive
All data is stored in the .save folder. The folder category-index store the file that has five numbers which represents the five random categories in it. The questions-index folder contains 5 files which store five numbers for five random quesiton of each category respectively. The answered folder store all the answered questions and the winnings folder store the user's current score. If the user closes the game, all the information is saved in these folders. When the user reopens the game again, the program will first load the data from these files. When the user selects Reset, the.save folder will be first deleted and then recreated.   

## Text to Speech
Using festival to convert the text into speech. For the NZ english accent, we need to unzip akl_nz_jdt_diphone.zip into /usr/share/festival/voices/english. We also need to install festlex-oald for some pronunciations. The user need to type "sudo apt-get install festlex-oald" in the terminal.  

## Playback and Manipulation Audio
In the TTS class, we first create a .scm file that include the type of accent,duration stretch parameter and the text that need to speak. Then we use the linux script command to play the .scm file. The type of accent is linked to the radio buttons and the duration stretch is linked to the slider bar in the GUI. User could change the value of them in the GUI. The replay button in the answer quesion scene will re-load the .scm again.   

## Project Structure
We decided to organise our code into different packages. We considered using just one package, but we wanted our code to be easy to find and organised. We structured in terms of the Model-View-Controller pattern. Within these packages, we also organised our files in play vs practice modules to keep them separate. Some files might have the same name between play and practice so it's very useful to have them in different packages

## Decisions Down the Line
Here, we'll document any changes as development continues

### Practice Section
Initially, when the user quit the game when answering a question in the practice module, we would save the number of attempts left and continue on with the question when the game started back up. After some discussion, we ultimately decided to remove this feature as players can actually re-attempt the question due the random selection of questions. So, there wasn't any point to saving the number of attempts if the question can be attempted again.

### Espeak vs. Festival
We are currently using espeak due to how easy it is to set the speed. However, after a client meeting (30/09/2020), we will be using festival down the line. This is because we are able to set NZ voices that can pronounce Māori words (the best that it can). Ultimately, we believe that this is a good decision as we want users to be able to properly learn the pronunciation of the Māori language. This is currently low priority as we currently have espeak working and other important things need to also be implemented.

We also decided to provided support for both the NZ and US accent so that the user can choose their preference. People like different things so it's good to have an option like that. In the scenes, you can see radio buttons that give you the option to change the accent.

### Macrons
After the client meeting (30/09/2020), macrons were decided to be very important. There are two options:
1. Normalize the answer and compare them
2. Allow the user to enter macrons into the answer section
We think that the second option is best. This allows users to learn how to properly *spell* Māori words which maintains respect for the Māori language and culture.

### Enter
We have now provided support for the enter key being pressed when answering a question. This means one less click to get to the next stage and also, pressing enter is a natural thing for people to do when they want to confirm something.

### Timer
We've added a timer for 30 seconds. The player will have 30 seconds to answer the question. If time runs out, then the next scene will show - incorrect if the answer is incorrect, correct if the user had entered the correct answer but never confirmed the answer

### TTS
We managed to kill the festival process if text-to-speech needs to occur but the previous process hasn't finished yet. We just had to destroy all descendent processed that it created so it was only a couple of lines of code.

### Scores
There is now a scoreboard! We decided to add an extra button for this on the start page so users can see the top 10 scores. After the user enters their name (upon completion of all questions), they are redirected to this page as well.

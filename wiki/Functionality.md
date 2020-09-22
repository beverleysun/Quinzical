# Functionality
This document record the functionality design process when developing the Quinzical game.
- [Quinzical introduction](#quinzical-introduction)
- [Database](#database)
- [Load Question](#load-question)
- [GUI](#gui)
- [Text to Speech](#text-to-speech)
- [Playback and Manipulation Audio](#playback-and-manipulation-audio)

## Quinzical introduction
Quinzical gives users a deeper understanding of New Zealand through a quiz format game. The game
allows users to select different categories of questions to answer. There are two modules for user
to play:

### The Practice Module

In the practice module, There are all categories from question set will be display on the board.
User can select any category from the New Zealand Question Set. A randomly selected clue will provide
to user with both the text and the TTS read version. The user is told whether they are right or wrong 
after the anser is typted in. The user is allowed three attempts. In the third attempt they are given 
the first letter of the answer as a hint.If they are wrong after the third attempt the answer is written 
on the screen along with the original clue.

### The Games Module

In the game module, The interface will display 5 randomly selected categories and 5 randomly selected clues
from each category on the board in a grid format. User can select the clue to answer the question. The clue
is read out by the TTS system. User can only choose the lowest money value for any vategory. After the user 
types in the correct answer, their worth increases accordingly.If the user does not type in the correct answer 
or clicks the “Don’t know” the clue disappears off the grid, but their worth does not increase.Each clue can only
be answered once. Once asked, regardless of whether the answer right or wrong the clue is removed from the grid.
Once all the clues are gone, the user is treated to a reward screen, and the user has the option to play again. 
The score is reset to zero.

## Database
Question source is form https://docs.google.com/document/d/18TYDYfCBuwa5r1V9EMVGWLjJgfCSVY_UPbPtNPCPnCw/edit?usp=sharing
Use a txt file to store all questions of each category and place all txt files under the category folder. 
Set the category as the name of the file. Each question occupies one row in the txt file with the format
"CLUE...,(what is/Who is) ANSWER".

## Load Question
Use java File class to read all the files under category folder and save them into a File[] array. Create a method to
generate 5 random numbers from the required range and store them into an array. We can use this 5 numbers to select element 
from File[] array. We can achieve the requirement of display 5 randomly selected categories. Similar for the clues, we use 
the 5 random numbers to represent the line number in a txt file. When we read the content of a category file, we only read the 
appointed 5 lines which the line numbers are matched with the 5 random number. We can achieve the requirement of randomly selected
5 clues form a category.

## GUI

## Archive

## Text to Speech

## Playback and Manipulation Audio





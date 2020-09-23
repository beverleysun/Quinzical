# Design
This is where we are documenting our design process when developing the Quinzical game. In here, we will be discussing
both [UI](#ui) and [UX](#ux), covering:
- [Colour](#color)
- [Layout](#layout)
- [Typography](#typography)
- [Navigation](#navigation)

## UI
### Colour
#### Colour Scheme

This was the first decision that had to be made to form the base of our design. The main requirements were:
- Colours must complement each other well
- Colours had to high contrast so maintain readability and accessibility
- Use no more than 5 different colours (to keep things clean and easy on the eye)

We started off with some research:

<div>
    <img src="./img/kahoot.png" width=200 alt="Kahoot"/>
    <img src="./img/lotto.jpg" width=200 alt="Lotto"/>
    <img src="./img/jeopardy.jpg" width=200 alt="Jeopardy"/>
    <img src="./img/millionaire.jpg" width=200 alt="Who wants to be a millionaire?"/>
</div>
<br>

A main theme that we noticed in these popular game-show/quiz type platforms is the use of purple and bright/bold colours.
So we decided to take inspiration by using purple and complementing it with a brighter and contrasting colour, orange.

#### Sample scenes
<div>
    <img src="../mockups/start.jpg" width=285 alt="Start page"/>
    <img src="../mockups/play-question-board.jpg" width=285 alt="Question board"/>
    <img src="../mockups/practice-1-attempt.jpg" width=285 alt="One attempt left in practice mode"/>
</div>
<br>

Upon mouse-over on the buttons, the cursor turns into a hand so that the user knows that it is clickable. Text colour was chosen such that
it provides enough contrast in the background for it to be readable.

#### Accessibility
Accessibility is very important for those who are visually impaired. For this, we used the Chrome extension, 
[Colorblindly](https://chrome.google.com/webstore/detail/colorblindly/floniaahmccleoclneebhhmnjgdfijgg?hl=en), to ensure that
all elements of Quinzical are able to be read and distinguished.

<div>
  <img src="./img/achromatopsia.png" alt="Achromatopsia (monochrome)" width=430/>
  <img src="./img/tritanopia.png" alt="Tritanopia (blue-blind)" width=430/>
  <p><b>Left to right</b>: Achromatopsia (Monochrome), Tritanopia (Blue-Blind)</p>
</div>

### Layout
We have also decided to group elements together such as the buttons for each category so that the user intuitively knows which button belongs to which category.
All elements are also laid out in such a way that the eye naturally follows the progression of the game. *Quit* is the last button as it
signifies the end of the game while also keeping with basic design tropes. We have the highest value questions ($500) at the top, and the 
lowest value questions ($100) at the bottom so that the user "works their way up" to the higher values. The back arrow is placed in the top left corner
as, traditionally, left-side buttons are associated with go-back functionality and right-side buttons with go-forwards. There
were plenty of other design/layout decisions made however, we won't delve too deep into that.

### Typography
Typography was simple. We needed a simple, easy to read font. For readability, using sans serif font was important, especially for on screen
use. The clean lines and sharp edges are able to be rendered more clearly on a screen which increases legibility for users. The font size for
all elements are at least 14pt so that text does not get too small to read (especially on higher resolution screens where everything gets relatively smaller).
Titles and subtitles were larger to form a sense hierarchy.

The font chosen was "PT Sans", a free to use Google font that does not require any licensing.

## UX
### Navigation
We tried to make the game behave the way a user would expect it to behave. So, when the user is in any of the modules (game or practice)
then they would except to be able to change the voice speed. Each button is labeled in such a way that the user knows what
the button would do. So a reset button is literally labeled "Reset" to reduce any ambiguity that may come with, for example, an image.
As said before, only buttons have the hand cursor on hover to signify that they can be clicked on. If, after we complete the main
deliverables of the project, we still have time, keyboard shortcuts may be used to enhance the experience of the user. (We all love
the shortcut ctrl+c and ctrl+v over right click and select).
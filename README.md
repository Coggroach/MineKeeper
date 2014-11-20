MineKeeper
----------

![alt tag](https://github.com/Coggroach/MineKeeper/blob/master/ExampleGame.png)
![alt tag](https://github.com/Coggroach/MineKeeper/blob/master/ColorCodeGame.png)

- Colour Class  - Storing RGBA
- Tile Class 	- Storing Colour, isMine, Texture (Multiplier)

- InputListener - ITouchable for Each Activity
		- Decode MVPMatrix and Point touched into  

- ITouchable   	- Buttons, Tiles, 

- GameActivity 	- Contains OpenGl Surface and Game Components
- MenuActivity 	- Options, New Game, GameModeType, etc...
- InfoActivity  - Storing GameRules etc...

- ActivMessanger- Storing synchronized methods and Data.
- Options Class - Board Width, Height, MaxWidth, Difficulty
- EnumDifficulty- Various Variables for Board Size, Number of Turns.

- Game Class 	- Contains Logic, Tiles specified to Options
		- Tiles are stored in an Array.
- GameModeType	- extends Game, Multiple Types.

- TileRenderer 	- Takes Array of Tiles
		- Translate Cube around to make Grid
		- extends AbstractGLRenderer
	 	- eyeZ dependant on Number of Rows and Columns 
		- Shaders for Colours,
		- Light sources behind eyeZ
		- Load Textures for Tiles. Moddable.




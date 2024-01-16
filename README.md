--- Tanks VS Zombies ---

A 2D game written in Java using the LibGDX framework

Created by Joshua Lor

Contact Information
    Email: joshualor@yahoo.com


## Version of Java Used:
    16.0.2

## IDE used:
    IntelliJ IDEA Ultimate Edition version 2023.1.2

## Steps to Import project into IDE:
    Using IntelliJ:
    1. Select File -> New -> Project from Existing Sources...
    2. From the \src folder of this repo, navigate to the \tankGame\out folder and then select "build.gradle"
    5. Press "OK".

## Steps to Build the Project:
    Using IntelliJ:
    1. Navigate to the terminal.
    2. Type in the command:

        ./gradlew desktop:dist

    3. The new .jar file should be located in projectDirectoryName/desktop/build/libs/.

    Using Command Prompt:
    1.	Similar to the steps to above, navigate to the project directory.
    2.	Type in the command:

        gradlew desktop:dist

    3.	The new .jar file should be located in projectDirectoryName/desktop/build/libs/.

## Steps to run the Project:
    Using Windows Explorer:
    1.	Double-click the tankgame.jar file in the \jar folder to run the application.

    Using IntelliJ:
    1. With the project imported into IntelliJ, navigate the project structure from desktop -> src -> com.tank.game and click on the DesktopLauncher.java class.
    2. Run the main method in the DesktopLauncher class.

    Using Command Prompt:
    1.	Navigate to the .jar's location.
    2.	Run the command:

        java -jar tankgame.jar


Note: The .jar file has trouble finding assets, despite all the assets already being inside of the .jar. It expects the assets to be in the same folder as itself. Thus, if the assets are not in the same folder as the .jar, a viable workaround is to extract all of the assets out of the jar and then place them into the same folder. This can be done by copying the .jar and then changing the new .jar's extension into a .zip for extraction.


## Controls to play the Game:

|               | Player 1 | Player 2 |
|---------------|----------|----------|
|  Forward      |    T     |    Up    |
|  Backward     |    G     |   Down   |
|  Rotate left  |    F     |   Left   |
|  Rotate Right |    H     |   Right  |
|  Shoot        |    A     |     ;    |
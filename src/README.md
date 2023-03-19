### Assignment Overview

#### Project Structure
The structure of this project is divided into three directories / packages:
1. IterativeDeepening --> this directory contains all of the code for the Iterative Deepening algorithm
2. HillClimbing --> this directory contains all of the code for the Hill Climbing algorithm
3. SharedUtils --> this directory contains the shared classes between the algorithms
```
Artifical Intelligence Programming Assignment 1
│
└───src
    │   -   README.md
    │
    └───IterativeDeepening
    │   -   Main.java
    │   -   input.txt
    │
    │
    └───HillClimbing
    │   -   Main.java
    │   -   input.txt.java
    │
    │
    └───SharedUtils
    │   -   Graph.java
    │   -   Vertex.java
```

#### How to Run
As mentioned in the assignment instructions, the `input.txt` file should be located in the same directory as the `Main.java` file for the algorithm. Then, run the following commands (in order):

##### Compiling
1. While in the `src` directory, execute `javac SharedUtils/*.java`
2. While in the `src` directory, execute `javac -cp . IterativeDeepening/Main.java`
3. While in the `src` direcotry, execute `javac -cp . HillClimbing/Main.java`

##### Running
To run the IterativeDeepening program, while in the `src` directory, execute `java -cp . IterativeDeepening.Main`

To run the HillClimbing program, while in the `src` directory, execute `java -cp . HillClimbing.Main`

##### Important Note
It is really important to follow the order of compiling `SharedUtils` before the individual algorithms, since they both utilize classes from within the `SharedUtils` package.
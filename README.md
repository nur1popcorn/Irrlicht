# Irrlicht

> Your cross-version cheating champ

Irrlicht is an easy to use cheat base which aims to make cross-version cheating attractive by automazing the tedious process of making it compatible for each version.

## Index
* What it does
  * Explain it to me
    * What it is
    * What's unique
      * Other solutions
      * My solution
  * I don't get it
* Features
* Getting started
  * Mapper
    * Wrappers
    * CustomHandlers
  * Hooker
    * Events
  * Modules
* Built with
* Release history
* Contributing
* Authors
* License
* Contact

## What it does

Irrlicht recognises logic and structure in obfuscated classes, makes them easily accessible and enables you to inject your personal cheating-desires. 

### Explain it to me

The game is obfuscated. Therefore it makes it difficult to access data structures or invoke methods reliably. Not only that but the games fields, methods and classes change with each version so even if you manage to access those, they will change their names in the next version.

Irrlicht aims to solve exactly this problem.

* ### What it is

  Is it a bird? Is it a plane? No, it's an injection-client.

  But what is an injection client you might ask...

  Well, let me explain it to you in simple terms:
  - Injection clients are clients desgined for minecraft *(usually)*.
  - An injection-client is a client that is loaded **while** the game is running.
  - It infiltrates and modifies the targeted client thereby adding it's own set of logic.
  
* ### What's unique
  Cross version cheating usually causes a lot of problems, Irrlicht solves them all and automizies the tedious process of making your cheat compatible for each version.
  * #### Other solutions: <br/>
  Currently there are two public solutions to tackle this problem. One of which involves deobfuscating the whole source code and a loss of      cross version compatibility is the result (without putting some manual effort into making it compatible with the next version). And even if this effort is put into the project, there is a high probability that it won’t be compatible with desired mods for the game.<br/><br/> Another solution is to use some sort of modloader, a middle man, if you will. This results in it being pretty useless to obfuscate your client since you can see all of the api calls. There will **STILL** be compatibility issues with mods that decide to use another modloader or maybe don't use a modloader at all. If you wanted to switch from one to another you are going to face all kinds of problems. Then there is also the fact that the creator has to manually update his mod for it to be compatible with newer versions. This results in you being unable to cheat in snapshot versions of the game. These are a very desirable target for cheaters since there is no public snapshot-server that uses    any kind of anticheat.
  * #### My solution: <br/>
  

### I don't get it

As I mentioned before, Irrlicht rather analyses static structure than wonky names.

The example below shows the difference between an older version of the game and a newer one: 

```diff
- public abstract class pk implements m
+ public abstract class sn implements n
{
-   public double p;
+   public double m;
-   public double q;
+   public double n;
-   public double r;
+   public double o;
}
```

The structure stays the same but the names change, here is where Irrlicht comes into play.
It analyses the structure you give it and tries to find this pattern within the obfuscated classes.

Irrlicht will then through [`Wrapper`](https://github.com/nur1popcorn/Irrlicht)s make this code accessible to you.

Still don't get it?<br/>
Well, too bad for you. Go somewhere else.<br/>
Just kidding you can contact me [here](https://github.com/nur1popcorn/Irrlicht) and i will try my best to explain it to you.

## Features
- Quality
- Cross version compatibility  "write it once, use it always"
- Easy usability while maintaining a high level of customize-ability
- High probability for mod support

## Getting started

Now its time to enter the playground. These few guides will give you some advice on the more basic stuff:

### Mapper

### Hooker

### Modules

## Built with
- [Maven](https://maven.apache.org/) - Dependency Management.
- [ASM](http://asm.ow2.org/) - Bytecode manipulation.
- [JavaAssist](http://jboss-javassist.github.io/javassist/) - I pretty much only use this libary to generate proxies for concrete classes. (Which isnt to diffcult hence i am thinking about booting this one..)

## Release history
* 1.0.0-alpha
  * Inital commit.
## Contributing
*NOTE:* **DO NOT** contribute to the gui as i am currently rewriting big portions of it.<br/>

Other than that just create a new pull request.

## Authors
- **nur1popcorn** *(Hackerman)* - *Inital work*

## License
Copyright (C) Keanu Poeschko - All Rights Reserved

License.

## Contact
If you have any questions regarding the project please contact: nur1popcorn@gmail.com

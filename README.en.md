# AgeDefiner Java Telegram Bot

The bot can calculate time length between two dates (e.g. age or length of experience etc.).
The bot can also do arithmetical operations on that lengths.

The bot operates as an interpreter of the specific commands. So it should be interacted using its syntax.

<h3>Bot features and syntax with examples</h3>

---

1) User variables can be defined in the following way
 
   _--config-alias Neo = Age(02.09.1964) -m"Keanu Reeves age"_
   
   _--config-alias T800 = Age(30.07.1947) -m"Arnold Schwarzenegger age"_
   
   _--config-alias aDiff = Diff(02.09.1964, 30.07.1947) -m"Age difference between Arnold and Keanu"_
   
**Neo** - is the variable. Symbols 'a..Z' and digits are only supported, non case-sensitive

**Age(02.09.1964)** - is the function (date format is 'dd.mm.yyyy')

**-m"Keanu Reeves age"** - is the description (optional param, should be in quotes "")

2) Variables and functions are supported as input

````
> Neo
Keanu Reeves age:
  58 years 1 month 10 days
````

````
> aDiff
Arnold Schwarzenegger age:
  17 years 1 month 3 days
````

````
> Diff(31.03.1999, 02.09.1964) -m"Age of Keanu Reeves on the Matrix release date"
Age of Keanu Reeves on the Matrix release date:
  34 years 6 months 29 days
````
Available functions: Age(d1) и Diff(d2, d1). Age -> Diff(Now(), d1)

3) It could be added or subtracted each other
````
> Age(30.07.1947) - Age(02.09.1964)
17 years 1 months 3 days
````

````
> T800 - Neo -m"Age difference between Arnold and Keanu"
Age difference between Arnold and Keanu:
  17 years 1 months 3 days
````

4) Multiline input is available
````
> Neo
> Diff(31.03.1999, 02.09.1964) -m"Age of Keanu Reeves on the Matrix release date"
Keanu Reeves age:
  58 years 1 month 15 days
Age of Keanu Reeves on the Matrix release date:
  34 years 6 months 29 days
````

5) Additional features

_--config-alias-show Neo_ - Show the key Neo
   
_--config-alias-show_ - Show all keys

_--config-alias-delete aDiff_ - Delete the key aDiff

_--config-alias-delete -all_ - Delete all keys

<h3>Local run</h3>

---

Before the running you should create test bot on Telegram using [BotFather](https://t.me/BotFather) and get token and its user name.
Config file looks like this

````
BOT_USER_NAME = <BotUserName>
BOT_TOKEN = <Token>
````
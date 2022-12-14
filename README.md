# Telegram бот AgeDefiner на Java

English version is [here](README.en.md)

[Бот](https://t.me/AgeDefinerBot) умеет вычислять продолжительность чего-то между двумя датами (например возраст или опыт работы и т.д.).
Так же бот умеет производить арифмитические операции над этими величинами.

Бот работает как интерпретатор команд. Поэтому с ним нужно общаться используя его синтаксис.

## Описание возможностей и синтаксиса бота с примерами

---

1) Переменные задаются так

Общий случай:

> -config-alias _VarName_ = _Expression_ -m"_Description_"

- ***-config-alias*** - ключевое слово, означающее что нужно создать пользовательскую переменную <br>
- ***VarName*** - допустимое имя переменной (символы 'a..Z' и цифры) <br>
- ***Expression*** - Любое допустимое выражение. Может содержать функции Age(), Diff() или пользовательские переменные.
И арифмитические операции, например: Age() - Diff() + Variable. Определение описывается следующим образом:
  - expression : factor ( ( '+' | '-' ) factor )*
  - factor     : var | func
  - func       : Age(date) | Diff(date, date)
  - var        : NAME
  - date       : dd.mm.yyyy
- ***-m*** - опциональный ключ, сразу за ним должно следовать описание в кавычках и без пробелов ***"Description"***.
Его цель описать смысл этого выражения именно для вас <br>

Примеры:

>_-config-alias Neo = Age(02.09.1964) -m"Возраст Киану Ривза"_ <br>
_-config-alias T800 = Age(30.07.1947) -m"Возраст Арнольда"_ <br>
_-config-alias aDiff = Diff(02.09.1964, 30.07.1947) -m"Разница между Шварцом и Киану"_
   
- ***Neo*** - это переменная. Регистр не имеет значения <br>
- ***Age(02.09.1964)*** - функция (формат даты только dd.mm.yyyy) <br>
- ***-m"Возраст Киану Ривза"*** - описание (опциональный параметр, обязательно в кавычках)

2) Можно вызывать переменные или функции

````
> Neo
Возраст Киану Ривза:
  58 years 1 month 17 days //на момент 19.10.2022
````

````
> aDiff
Разница между Шварцом и Киану:
  17 years 1 month 3 days
````

````
> Diff(31.03.1999, 02.09.1964) -m"Возраст Киану на момент релиза Матрицы"
Возраст Киану на момент релиза Матрицы:
  34 years 6 months 29 days
````
Доступные функции: Age(d1) и Diff(d2, d1). Age -> Diff(Now(), d1)

3) Можно складывать и вычитать
````
> Diff(11.11.1918, 28.07.1914) + Diff(02.09.1945, 01.09.1939) -m"Длительность мировых войн"
Длительность мировых войн
  10 years 3 months 15 days
````

````
> T800 - Neo -m"Разница между Шварцом и Киану"
Разница между Шварцом и Киану:
  17 years 1 months 3 days
````

4) Поддерживается многострочный ввод
````
> Neo
> Diff(31.03.1999, 02.09.1964) -m"Возраст Киану на момент релиза Матрицы"
Возраст Киану Ривза:
  58 years 1 month 17 days //на момент 19.10.2022
Возраст Киану на момент релиза Матрицы:
  34 years 6 months 29 days
````

5) Дополнительные возможности

_-config-alias-show Neo_ - показать ключ Neo
   
_-config-alias-show_ - показать все ключи

_-config-alias-delete aDiff_ - удалить ключ aDiff

_-config-alias-delete -all_ - удалить все ключи

## Локальный запуск

---

Для создания тестового бота нужно обратиться к [BotFather](https://t.me/BotFather) и получить от него токен и userName бота.
Конфигурационный файл имеет следующий вид

````
BOT_USER_NAME = <BotUserName>
BOT_TOKEN = <Token>
````

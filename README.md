# AgeDefiner Java Telegram Bot

Бот умеет вычислять продолжительность чего-то между двумя датами (например возраст или опыт работы). Так же бот умеет производить арифмитические операции над этими продолжительностями.

Бот работает как интерпретатор команд. Поэтому с ним нужно общаться используя его синтаксис.

<h3>Описание возможностей и синтаксиса бота с примерами</h3>

1) Переменные задаются так
 
   <i>--config-alias Neo = Age(02.09.1964) -m"Возраст Киану Ривза"</i>

   <i>--config-alias Schwarz = Age(30.07.1947) -m"Возраст Арнольда"</i>

   <i>--config-alias aDiff = Diff(02.09.1964, 30.07.1947) -m"Разница между Шварцом и Киану"</i>
   
Neo - это переменная (только символы 'a..Z' и цифры)
   Age(02.09.1964) - функция (формат даты только такой как тут)
   -m"Возраст Киану Ривза" - описание (обязательно в кавычках)

3) Можно вызывать переменные или функции
Neo
Возраст Киану Ривза:
58 years 1 month 10 days

> aDiff
Разница между Шварцом и Киану:
17 years 1 month 3 days

> Diff(31.03.1999, 02.09.1964) -m"Возраст Киану на момент релиза Матрицы"
Возраст Киану на момент релиза Матрицы:
34 years 6 months 29 days

3) Можно складывать и вычитать
> Age(30.07.1947) - Age(02.09.1964)
17 years 1 months 3 days

> Schwarz - Neo -m"Разница между Шварцом и Киану"
Разница между Шварцом и Киану:
17 years 1 months 3 days

4) Дополнительные возможности
   --config-alias-show Neo - показать ключ Neo
   --config-alias-show - показать все ключи
   --config-alias-delete aDiff - удалить ключ aDiff
   --config-alias-delete -all - удалить все ключи

P.S. Бот 'клал' такси Bolt на GDPR. Он собирает и использует все что Вы ему отдаете
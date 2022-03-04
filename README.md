# job4j_grabber
Проект Агрегатор вакансий. Программа ищет вакансии и записывает их в базу.

#Описание.
Система запускается по расписанию. Период запуска указывается в настройках - app.properties.  
Первый сайт будет sql.ru. В нем есть раздел job.  
Программа должна считывать все вакансии относящиеся к Java и записывать их в базу.  
Доступ к интерфейсу будет через **REST API**.

#Расширение.
1. В проект можно добавить новые сайты без изменения кода.
2. В проекте можно сделать параллельный парсинг сайтов.

[![Build Status](https://app.travis-ci.com/max-piter/job4j_grabber.svg?branch=master)](https://app.travis-ci.com/github/max-piter/job4j_grabber)
[![codecov](https://codecov.io/gh/max-piter/job4j_grabber/branch/master/graph/badge.svg?token=YLLCJV2C4T)](https://codecov.io/gh/max-piter/job4j_grabber)
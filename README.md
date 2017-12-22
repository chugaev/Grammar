# Grammar
- первый алгоритм
# Использование
  1. клонировать репозиторий
  2. Для тестирования выполнить gradle test(результат о тестировании будет в build/reports/tests/test/index.html)
  3. Для ввода файлов руками выполнить gradle run, после написать три файла через пробел (grammar_name, graph_name, output_name) предварительно скопировав эти файлы  в папку с build.gradle. Пример: "grammar1 skos.dot output.txt".Без имени выходного файла, вывод будет в stdout.

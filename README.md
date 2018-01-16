# Grammar
- Алгоритмы.
# Использование
  1.  git clone git@github.com:chugaev/Grammar.git
  2. cd Grammar/ 
  3. Для тестирования выполнить gradle test(результат о тестировании будет в build/reports/tests/test/index.html)
  4. Для ввода файлов руками выполнить gradle run, после написать три файла через пробел (grammar_name, graph_name, output_name) предварительно скопировав эти файлы  в папку с build.gradle или прописав полные пути. Пример: "data/grammars/anbn data/graphs/cycle output.txt".Без имени выходного файла, вывод будет в stdout.
  5. Для выбора алгоритма в следующей строке написать число: 1 - матричный алгоритм, 2 - GLL, 3 - GLR.

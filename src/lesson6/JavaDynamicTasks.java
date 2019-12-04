package lesson6;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     *
     * Трудоёмкость: O(n*m); Ресурсоёмкость: O(n*m), где n - длина first, m - длина second
     */
    public static String longestCommonSubSequence(String first, String second) {
        int fLength = first.length();
        int sLength = second.length();
        int[][] matrix = new int[fLength + 1][sLength + 1];

        for (int i = 0; i < fLength; i++)
            for (int j = 0; j < sLength; j++) {
                if (first.charAt(i) == second.charAt(j))
                    matrix[i + 1][j + 1] = matrix[i][j] + 1;
                else if (matrix[i][j + 1] >= matrix[i + 1][j])
                    matrix[i + 1][j + 1] = matrix[i][j + 1];
                else
                    matrix[i + 1][j + 1] = matrix[i + 1][j];
            }

        StringBuilder result = new StringBuilder();

        for (int lastIndex = matrix[fLength][sLength]; lastIndex > 0; lastIndex--) {
            while (matrix[fLength - 1][sLength] != lastIndex - 1)
                fLength--;
            while (matrix[fLength][sLength - 1] != lastIndex - 1)
                sLength--;

            fLength--;
            sLength--;

            result.append(first.charAt(fLength));
        }
        return result.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     *
     * Трудоёмкость: O(n^2); Ресурсоёмкость: O(n), где n - размер list
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int size = list.size();
        if (size == 0 || size == 1)
            return list;

        int[] lengthsOfLIS = new int[size];
        int[] indexesOfLIS = new int[size];

        for (int i = 0; i < size; i++) {
            lengthsOfLIS[i] = 1;
            indexesOfLIS[i] = -1;
            for (int j = 0; j < i; j++) {
                if (list.get(j) < list.get(i) && lengthsOfLIS[j] + 1 > lengthsOfLIS[i]) {
                    lengthsOfLIS[i] = lengthsOfLIS[j] + 1;
                    indexesOfLIS[i] = j;
                }
            }
        }
        int lastIndex = 0;
        int maxLength = lengthsOfLIS[0];

        for (int i = 0; i < size; i++) {
            if (lengthsOfLIS[i] > maxLength) {
                lastIndex = i;
                maxLength = lengthsOfLIS[i];
            }
        }

        List<Integer> result = new ArrayList<>();

        while (lastIndex != -1) {
            result.add(list.get(lastIndex));
            lastIndex = indexesOfLIS[lastIndex];
        }
        reverse(result);

        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}

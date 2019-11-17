package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     *
     * Трудоёмкость в худшем случае: O(N); Трудоёмкость в лучшем случае: O(logN); Ресурсоёмкость: O(1)
     */
    @Override
    public boolean remove(Object o) {
        if (root == null) return false;
        return uniRemove((T) o);
    }


    private boolean uniRemove (T number) {
        if (root.value.compareTo(number) == 0) { //обрабатываем случай, когда необходимо заменить корневой узел
            //обрабатываем случай, когда у удаляемого нет потомков
            if (root.left == null && root.right == null)
                root = null;
                //обрабатываем случай, когда есть левый наследник
            else if (root.left != null) {
                Node<T> max = maximum(root.left);

                if (max == root.left) {
                    max.right = root.right;
                    root = max;
                } else {
                    Node<T> parentMax = searchParent(root, max.value);

                    parentMax.right = max.left;
                    max.left = root.left;
                    max.right = root.right;
                    root = max;
                }
            }  //обрабатываем случай, когда нет левого потомка
            else {
                Node<T> min = minimum(root.right);

                if (min == root.right) {
                    root = min;
                } else {
                    Node<T> parentMin = searchParent(root, min.value);

                    parentMin.left = min.right;
                    min.right = root.right;
                    root = min;
                }
            }
            size--;
            return true;
        } else { //обрабатываем случай, искомым узлом является не корень
            Node<T> parent = searchParent(root, number);
            if (parent == null)
                return false;
            Node<T> shifted = find(number);
            //обрабатываем случай, когда у удаляемого нет потомков
            if (shifted.left == null && shifted.right == null) {
                if (parent.left != null && parent.value.compareTo(shifted.value) > 0)
                    parent.left = null;
                else
                    parent.right = null;
            } //обрабатываем случай, когда есть левый наследник
            else if (shifted.left != null) {
                Node<T> max = maximum(shifted.left);

                if (max == shifted.left) {
                    max.right = shifted.right;
                } else {
                    Node<T> parentMax = searchParent(shifted, max.value);

                    parentMax.right = max.left;
                    max.left = shifted.left;
                    max.right = shifted.right;
                }
                if (parent.value.compareTo(max.value) > 0)
                    parent.left = max;
                else
                    parent.right = max;
            }  //обрабатываем случай, когда нет левого потомка
            else {
                Node<T> min = minimum(shifted.right);

                if (min != shifted.right) {
                    Node<T> parentMin = searchParent(shifted, min.value);

                    if (shifted != parentMin) {
                        parentMin.left = min.right;
                        min.right = shifted.right;
                    }
                }
                if (parent.value.compareTo(min.value) > 0)
                    parent.left = min;
                else
                    parent.right = min;
            }
        }
        size--;
        return true;
    }

    private Node<T> minimum(Node<T> node) {
        if (node.left == null)
            return node;
        else
            return minimum(node.left);
    }

    private Node<T> maximum(Node<T> node) {
        if (node.right == null)
            return node;
        else
            return maximum(node.right);
    }

    private Node<T> searchParent(Node<T> node, T number) {
        int comparison = number.compareTo(node.value);

        if (node.right != null && comparison >= 0) {
            if (node.right.value.compareTo(number) == 0 ) {
                return node;
            }
            else return searchParent(node.right, number);
        }
        if (node.left != null && comparison <= 0) {
            if (node.left.value.compareTo(number) == 0 ) {
                return node;
            }
            else return searchParent(node.left, number);
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {
        private Node<T> current = null;

        private BinaryTreeIterator() {}

        /**
         * Проверка наличия следующего элемента
         * Средняя
         *
         * Трудоёмкость в худшем случае: O(N); Трудоёмкость в лучшем случае: O(logN); Ресурсоёмкость: O(1)
         */
        @Override
        public boolean hasNext() {
            return searchNext() != null;
        }

        /**
         * Поиск следующего элемента
         * Средняя
         *
         * Трудоёмкость в худшем случае: O(N); Трудоёмкость в лучшем случае: O(logN); Ресурсоёмкость: O(1)
         */
        @Override
        public T next() {
            current = searchNext();

            if (current != null)
                return current.value;
            else
                return null;
        }

        private Node<T> searchNext() {
            if (root == null)
                return null;

            if (current == null)
                return minimum(root);

            Node<T> next = null;
            Node<T> node = root;

            while (node != null) {
                if (node.value.compareTo(current.value) > 0) {
                    next = node;
                    node = node.left;
                } else
                    node = node.right;

            } return next;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         *
         * Трудоёмкость в худшем случае: O(N); Трудоёмкость в лучшем случае: O(logN); Ресурсоёмкость: O(1)
         */
        @Override
        public void remove() {
            uniRemove(current.value);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}

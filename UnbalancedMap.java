package edu.sdsu.cs.datastructures;

import java.util.LinkedList;
import java.util.List;

/**
 * Natalia Alvarez
 */

public class UnbalancedMap<K extends Comparable<K>, V> implements IMap<K, V> {
    public UnbalancedMap() {
    }

    public UnbalancedMap(IMap<K, V> source) {
        for (K key : source.keyset()) {
            add(key, source.getValue(key));
        }
    }

    Node root = null;
    int size = 0;

    V valueOfDeleted = null;
    boolean wasLeft = false;
    boolean wasLeftTwo = false;

    List<K> allKeys = new LinkedList<>();
    List<K> keys = new LinkedList<>();
    List<V> toReturnV = new LinkedList<>();
    List<V> valueList = new LinkedList<>();
    List<K> keyValues = new LinkedList<>();

    class Node {
        public K key;
        public V value;

        Node left;
        Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public boolean add(K k, V v) {
            int result = key.compareTo(k);
            if (result == 0) return false;
            else if (result > 0) {
                if (left == null) {
                    left = new Node(k, v);
                    size++;
                    return true;
                } else return left.add(k, v);
            } else if (result < 0) {
                if (right == null) {
                    right = new Node(k, v);
                    size++;
                    return true;
                } else return right.add(k, v);
            }
            return false;
        }

        public boolean contains(K k) {
            int result = key.compareTo(k);
            if (result == 0) return true;
            else if (result > 0) {
                if (left != null) return left.contains(k);
                return false;
            } else {
                if (right != null) return right.contains(k);
                return false;
            }
        }

        public V getValue(K key) {
            int result = this.key.compareTo(key);
            if (result == 0) return value;
            else if (result > 0) return left.getValue(key);
            else return right.getValue(key);
        }

        public K getKey(V v) {
            K theKey = null;
            V otherValue = getValue(key);
            if (root != null) {
                if (v == otherValue) theKey = key;
                else if (left != null) return left.getKey(v);
                else if (right != null) return right.getKey(v);
            }return theKey;
        }

        public Iterable<K> getKeys(V v) {
            if (root == null) return keyValues;
            else {
                V otherValue = getValue(key);
                if (root != null) {
                    if (left != null) left.getKeys(v);
                    if (right != null) right.getKeys(v);
                    if (v == otherValue) keyValues.add(key);
                }
            }return keyValues;
        }

        public Iterable<K> keySet(Node node) {
            if (node != null) {
                keySet(node.left);
                allKeys.add(node.key);
                keySet(node.right);
            }return allKeys;
        }

        public Iterable<V> value(Node node) {
            if (node != null) {
                value(node.left);
                toReturnV.add(node.value);
                value(node.right);
            }return toReturnV;
        }

        public V delete(K k) {
            int result = key.compareTo(k);
            if (result > 0) left.delete(k);
            else if (result < 0) right.delete(k);
            else {
                if (left == null && right == null) {
                    noChildRemove(k);
                    return valueOfDeleted;
                }
                if ((left != null && right == null)) {
                    if (root.key == k) {
                        valueOfDeleted = root.value;
                        root = root.left;
                        size--;
                        return valueOfDeleted;
                    }
                    Node parentNode = parent(root, k);
                    Node nodeToDelete;
                    if (wasLeft) {
                        nodeToDelete = parentNode.left;
                        valueOfDeleted = nodeToDelete.value;
                        parentNode.left = nodeToDelete.left;
                    } else {
                        nodeToDelete = parentNode.right;
                        valueOfDeleted = nodeToDelete.value;
                        parentNode.right = nodeToDelete.left;
                    }size--;
                }
                if ((left == null && right != null)) {
                    if (root.key == k) {
                        valueOfDeleted = root.value;
                        root = root.right;
                        size--;
                        return valueOfDeleted;
                    }
                    Node parentNode = parent(root, k);
                    Node nodeToDelete;
                    if (wasLeft) {
                        nodeToDelete = parentNode.left;
                        valueOfDeleted = nodeToDelete.value;
                        parentNode.left = nodeToDelete.right;
                    } else {
                        nodeToDelete = parentNode.right;
                        valueOfDeleted = nodeToDelete.value;
                        parentNode.right = nodeToDelete.right;
                    }size--;
                }
                if (left != null && right != null) {
                    if (root.key == k) {
                        Node node = root.right;
                        Node min = minValueRoot(node);
                        twoChildRemove(k, min, root);
                        return valueOfDeleted;
                    }
                    Node parentNode = parentTwo(root, k);
                    Node nodeToDelete;
                    if (wasLeftTwo) nodeToDelete = parentNode.left;
                    else nodeToDelete = parentNode.right;
                    Node node = nodeToDelete.right;
                    Node min = minValueRoot(node);
                    twoChildRemove(k, min, nodeToDelete);
                    return valueOfDeleted;
                }
            }return valueOfDeleted;
        }

        public Node parent(Node parentNode, K k) {
            if (parentNode == null) return null;
            int result = parentNode.key.compareTo(k);
            if (parentNode.left != null && parentNode.right != null) {
                if (parentNode.left.key == k) {
                    wasLeft = true;
                    return parentNode;
                }
                if (parentNode.right.key == k) {
                    wasLeft = false;
                    return parentNode;
                }
            } else if (parentNode.left == null && parentNode.right != null) {
                if (parentNode.right.key == k) return parentNode;
            } else if (parentNode.left != null && parentNode.right == null) {
                if (parentNode.left.key == k) {
                    wasLeft = true;
                    return parentNode;
                }
            }
            if (result > 0) return parent(parentNode.left, k);
            else if (result < 0) return parent(parentNode.right, k);
            return parentNode;
        }

        public Node parentTwo(Node parentNode, K k) {
            if (parentNode == null) return null;
            int result = parentNode.key.compareTo(k);
            if (parentNode.left != null && parentNode.right != null) {
                if (parentNode.left.key == k) {
                    wasLeftTwo = true;
                    return parentNode;
                }
                if (parentNode.right.key == k) {
                    wasLeftTwo = false;
                    return parentNode;
                }
            } else if (parentNode.left == null && parentNode.right != null) {
                if (parentNode.right.key == k) return parentNode;
            } else if (parentNode.left != null && parentNode.right == null) {
                if (parentNode.left.key == k) {
                    wasLeftTwo = true;
                    return parentNode;
                }
            }
            if (result > 0) return parentTwo(parentNode.left, k);
            else if (result < 0) return parentTwo(parentNode.right, k);
            return parentNode;
        }

        private void noChildRemove(K k) {
            if (root.key == k) {
                valueOfDeleted = root.value;
                root = null;
                size = 0;
            } else {
                Node parentNode = parent(root, k);
                if (wasLeft == true) {
                    parentNode.left = null;
                    size--;
                } else {
                    parentNode.right = null;
                    size--;
                }
                wasLeft = false;
                valueOfDeleted = this.value;
            }
        }

        private void twoChildRemove(K k, Node min, Node nodeToDelete) {
            if (root.key == k) {
                Node replaceNode = min;
                delete(min.key);
                valueOfDeleted = root.value;
                replaceNode.left = root.left;
                replaceNode.right = root.right;
                root = replaceNode;
            } else {
                Node parentNode = parentTwo(root, k);
                Node replaceNode = min;
                delete(min.key);
                if (nodeToDelete.left != null) replaceNode.left = nodeToDelete.left;
                if (nodeToDelete.right != null) replaceNode.right = nodeToDelete.right;
                if (wasLeftTwo == true) {
                    valueOfDeleted = parentNode.left.value;
                    parentNode.left = replaceNode;
                } else {
                    valueOfDeleted = parentNode.right.value;
                    parentNode.right = replaceNode;
                }
            }
        }

        public Node minValueRoot(Node node) {
            if (node.left == null) return node;
            else {
                while (node.left != null) node = node.left;
            }return node;
        }
    }

    @Override
    public boolean contains(K key) {
        if (root == null) return false;
        else return root.contains(key);
    }

    @Override
    public boolean add(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return true;
        }return root.add(key, value);
    }

    @Override
    public V delete(K key) {
        if (root == null)return null;
        return root.delete(key);
    }

    @Override
    public V getValue(K key) {
        if (root == null) return null;
         else return root.getValue(key);
    }

    @Override
    public K getKey(V value) {
        K theKey;
        if (root == null) return null;
        theKey = root.getKey(value);
        if (theKey == null) theKey = root.right.getKey(value);
        return theKey;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        if (root == null) return keyValues;
        else root.getKeys(value);
        return keyValues;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Iterable<K> keyset() {
        allKeys.clear();
        if (root == null) return keys;
        for (K key : root.keySet(root)) keys.add(key);
        List<K> temp = new LinkedList<>();
        for (K k : keys) temp.add(k);
        keys.clear();
        return temp;
    }

    @Override
    public Iterable<V> values() {
        if (root == null) return valueList;
        for (V value : root.value(root)) valueList.add(value);
        return valueList;
    }
}

package net.minecraft.client.searchtree;

public interface MutableSearchTree<T> extends SearchTree<T>
{
    void add(T pObject);

    void clear();

    void refresh();
}

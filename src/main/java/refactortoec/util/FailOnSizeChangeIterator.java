package refactortoec.util;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Consumer;

public class FailOnSizeChangeIterator<T> implements Iterator<T>
{
    private int expectedSize;
    private Collection<T> collection;
    private Iterator<T> iterator;

    public FailOnSizeChangeIterator(Collection<T> collection)
    {
        this.collection = collection;
        this.expectedSize = collection.size();
        this.iterator = collection.iterator();
    }

    private void checkSizeChange()
    {
        if (expectedSize != collection.size())
        {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    public boolean hasNext()
    {
        this.checkSizeChange();
        return iterator.hasNext();
    }

    @Override
    public T next()
    {
        this.checkSizeChange();
        return iterator.next();
    }

    @Override
    public void remove()
    {
        this.checkSizeChange();
        iterator.remove();
        this.expectedSize = collection.size();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action)
    {
        this.checkSizeChange();
        this.iterator.forEachRemaining(action);
    }
}

package be.howest.sooa.o10.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hayk
 * @param <E>
 */
public class Page<E> {

    private static final int PER_PAGE = 4;

    private final List<E> entities = new ArrayList<>();
    private int currentPage = 0;
    private int perPage;
    private boolean lastPage;

    public Page() {
        this(PER_PAGE);
    }

    public Page(int perPage) {
        this.perPage = perPage;
    }

    public List<E> getEntities() {
        return entities;
    }
    
    public E getAt(int i) {
        if (i < size()) {
            return entities.get(i);
        }
        return null;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
    
    public int nextPage() {
        clear();
        return ++currentPage;
    }

    public int previousPage() {
        if (currentPage > 0) {
            clear();
            return --currentPage;
        }
        return currentPage;
    }

    public boolean isFirstPage() {
        return currentPage == 0;
    }

    public void clear() {
        entities.clear();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }

    public int size() {
        return entities.size();
    }

    public boolean isFull() {
        return entities.size() == perPage;
    }

    public boolean add(E entity) {
        if (!isFull()) {
            return entities.add(entity);
        }
        return false;
    }
    
    public boolean remove(E entity) {
        return entities.remove(entity);
    }
}

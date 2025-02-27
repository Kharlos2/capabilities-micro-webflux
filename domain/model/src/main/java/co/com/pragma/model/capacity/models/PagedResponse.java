package co.com.pragma.model.capacity.models;

import java.util.List;

public class PagedResponse<T> {

    private long count;
    private int page;
    private int size;
    private List<T> items;

    public PagedResponse(long count, int page, int size, List<T> items) {
        this.count = count;
        this.page = page;
        this.size = size;
        this.items = items;
    }

    // Getters y Setters
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}

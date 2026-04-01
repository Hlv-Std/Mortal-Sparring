package model;

import java.util.Arrays;

public class Array<T>{
    private T[] data;
    private int capacity;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public Array(int capacity, T... items){
        this.capacity = capacity;
        data = (T[]) new Object[capacity];
        for (T item : items){
            this.push(item);
        }
    }

    public T get(int index){
        if (index < 0 || index > capacity)
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        return data[index];
    }

    public void push(T item) throws ArrayIndexOutOfBoundsException{
        if (data.length < capacity){
            data[data.length] = item;
        }
        throw new ArrayIndexOutOfBoundsException(String.format("Max cap of %d reached\n", capacity));
    }

    public boolean contains(T item){
        for (T i : data){
            if (i != null){
                if (i.equals(item))
                    return true;
            }
        }
        return false;
    }

    public void remove(T item){
        for (int i = 0; i < data.length; i++){
            if (data[i].equals(item)){
                data[i] = null;
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void clear(){
        data = (T[]) new Object[capacity];
    }
}

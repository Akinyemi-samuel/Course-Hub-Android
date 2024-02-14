package com.example.coursehub.service;

public class Pair<F, S, R, E> {
    public final F first;
    public final S second;
    public final R third;
    public final E fourth;

    public Pair(F first, S second, R third, E fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }


    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public static <A, B> android.util.Pair<A, B> create(A a, B b) {
        throw new RuntimeException("Stub!");
    }
}


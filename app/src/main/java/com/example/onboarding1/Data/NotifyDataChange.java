package com.example.onboarding1.Data;

public interface NotifyDataChange<T> {
    public void OnDataChanged(T obj);

    public void onFailure(Exception exception);
}
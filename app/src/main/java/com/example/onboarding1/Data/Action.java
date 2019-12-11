package com.example.onboarding1.Data;

public interface Action<T>
{
    public void onSuccess(T obj);
    public void onFailure(Exception exception);
    public void onProgress(String status, double percent);
}
package com.mojang.realmsclient.exception;

public class RealmsHttpException extends RuntimeException
{
    public RealmsHttpException(String pMessage, Exception pCause)
    {
        super(pMessage, pCause);
    }
}
